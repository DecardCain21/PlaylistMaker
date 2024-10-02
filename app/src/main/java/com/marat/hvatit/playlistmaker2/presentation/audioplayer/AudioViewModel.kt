package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddPlaylistTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.FetchPlaylistsUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AudioViewModel(
    previewUrl: String,
    private val interactor: AudioPlayerInteractor,
    private val interactorDb: FavoritesInteractor,
    private val getPlaylistsUseCase: FetchPlaylistsUseCase,
    private val addCrossRefUseCase: AddCrossRefUseCase,
    private val getCrossRefUseCase: GetCrossRefUseCase,
    private val addPlaylistTrackUseCase: AddPlaylistTrackUseCase
) :
    ViewModel(),
    AudioPlayerCallback {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private var loadingLiveData = MutableLiveData(playerState)
    private var timerJob: Job? = null
    private var favoriteState: FavoriteState = FavoriteState.IsFavorite(false)
    private var loadingFavoriteData = MutableLiveData(favoriteState)
    private var playlistsState: BottomPlaylistsState = BottomPlaylistsState.Data(emptyList())
    private var loadingPlaylistsData = MutableLiveData(playlistsState)
    fun getPlaylistsState(): LiveData<BottomPlaylistsState> = loadingPlaylistsData

    companion object {
        private const val TIMER_DELAY = 300L
    }

    init {
        interactor.setPreviewUrl(previewUrl)
        interactor.setCallback(this)

    }

    fun getFavoriteState(): LiveData<FavoriteState> = loadingFavoriteData
    fun getLoadingLiveData(): LiveData<MediaPlayerState> = loadingLiveData

    fun playbackControl() {
        var result = interactor.playbackControl()
        if (result is MediaPlayerState.Disconnected) {
            loadingLiveData.postValue(interactor.playbackControl())
            // установка значения LiveData из фонового потока
        }
        loadingLiveData.value = result
        //установка значения LiveData из текущего потока
        //Log.e("MediaState", "loadingLiveData:${loadingLiveData.value}")
        if (loadingLiveData.value is MediaPlayerState.Playing) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    fun onPausePlayer() {
        stopTimer()
        interactor.stopPlayer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    fun onDestroyPlayer() {
        stopTimer()
        interactor.destroyPlayer()
    }

    private fun startTimer() {
        loadingLiveData.value = MediaPlayerState.Playing(interactor.updateTimer())
        timerJob = viewModelScope.launch {
            delay(TIMER_DELAY)
            startTimer()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        loadingLiveData.value = MediaPlayerState.Paused
    }

    override fun trackIsDone() {
        stopTimer()
        loadingLiveData.postValue(MediaPlayerState.Completed())
    }

    override fun playerPrepared() {
        loadingLiveData.value = MediaPlayerState.Prepared
    }

    fun setFavoriteState(track: Track) {
        val trackId = track.trackId
        viewModelScope.launch(Dispatchers.IO) {
            interactorDb.addFavorite().catch { exception -> changeFavorite(false) }
                .map { tracks -> tracks.any { it.trackId == trackId } }
                .collect { isFavorite ->
                    if (isFavorite) {
                        deleteTrackDb(track)
                        changeFavorite(false)
                    } else {
                        saveTrackDb(track)
                        changeFavorite(true)
                    }
                }
        }

    }

    fun defaultFavoriteState(track: Track) {
        viewModelScope.launch {
            changeFavorite(interactorDb.isFavorite(track))
        }
    }

    private fun changeFavorite(boolean: Boolean) {
        loadingFavoriteData.postValue(FavoriteState.IsFavorite(boolean))
    }

    private suspend fun deleteTrackDb(track: Track) {
        interactorDb.deleteFavorite(track)
    }

    private suspend fun saveTrackDb(track: Track) {
        interactorDb.saveFavoriteTrack(track)
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCase.execute().collect { playlists ->
                setDataState(playlists)
                Log.e("Playlists", "ViewModel,getPlaylists:$playlists")
            }
        }
    }

    fun getCrossRef(playlistId: String){
        val tracks = mutableListOf<Track>()
        viewModelScope.launch(Dispatchers.IO) {
            getCrossRefUseCase.execute(playlistId).collect{
                Log.e("CrossRef","$playlistId:${it}")
                //tracks.add()
            }
        }
    }

    fun addToCrossRef(playlistId: String, trackId: String) {
        viewModelScope.launch {
            addCrossRefUseCase.execute(playlistId, trackId)
            //Перезаписать плейлист в дб, +1 трек
        }
    }

    fun addPlaylistTrack(track: Track){
        viewModelScope.launch {
            addPlaylistTrackUseCase.execute(track)
        }
    }

    private fun setDataState(data: List<Playlist>) {
        if (data.isEmpty()) {
            loadingPlaylistsData.postValue(BottomPlaylistsState.EmptyState)
        } else {
            loadingPlaylistsData.postValue(BottomPlaylistsState.Data(data))
        }
    }

}