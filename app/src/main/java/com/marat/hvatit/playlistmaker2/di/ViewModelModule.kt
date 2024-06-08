package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioPlayerController
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioViewModel
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.controller.AudioPlayerControllerImpl
import com.marat.hvatit.playlistmaker2.presentation.main.MainViewModel
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.FeaturedViewModel
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.PlaylistsViewModel
import com.marat.hvatit.playlistmaker2.presentation.search.SearchViewModel
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigatorImpl
import com.marat.hvatit.playlistmaker2.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<IntentNavigator> {
        IntentNavigatorImpl(get())
    }

    factory<AudioPlayerController> {
        AudioPlayerControllerImpl()
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (previewUrl: String) ->
        AudioViewModel(previewUrl, get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {(str:String)->
        FeaturedViewModel(str)
    }

    viewModel {(str:String)->
        PlaylistsViewModel(str)
    }

}