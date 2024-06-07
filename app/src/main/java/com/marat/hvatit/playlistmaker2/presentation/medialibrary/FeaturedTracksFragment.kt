package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marat.hvatit.playlistmaker2.databinding.FeaturedtracksFragmentBinding

class FeaturedTracksFragment : Fragment() {
    companion object {
        private const val NUMBER = "number"
        fun newInstance(number: Int) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }

    private lateinit var binding: FeaturedtracksFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FeaturedtracksFragmentBinding.inflate(inflater, container, false)
        return binding.root/* super.onCreateView(inflater, container, savedInstanceState)*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}