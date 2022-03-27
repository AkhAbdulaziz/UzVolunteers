package uz.targetsoftwaredevelopment.myapplication.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.myapplication.R
import uz.targetsoftwaredevelopment.myapplication.data.remote.responses.VideoData
import uz.targetsoftwaredevelopment.myapplication.databinding.ScreenFavouriteVideosBinding
import uz.targetsoftwaredevelopment.myapplication.presentation.ui.adapters.MyWishAdapter
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.pagesvidemodel.WishPageViewModel
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.pagesvidemodel.impl.WishPageViewModelImpl
import uz.targetsoftwaredevelopment.myapplication.utils.scope

@AndroidEntryPoint
class FavouriteVideosScreen : Fragment(R.layout.screen_favourite_videos) {
    private val binding by viewBinding(ScreenFavouriteVideosBinding::bind)
    private lateinit var myWishAdapter: MyWishAdapter
    private val viewModel: WishPageViewModel by viewModels<WishPageViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavouriteVideos()

        myWishAdapter =
            MyWishAdapter(requireContext(), object : MyWishAdapter.OnWishItemTouchListener {
                override fun onWishClick(videoData: VideoData) {
                    viewModel.changeLike(videoData)
                }

                override fun onPostClick(videoData: VideoData) {
                    findNavController().navigate(
                        FavouriteVideosScreenDirections.actionFavouriteVideosScreenToWatchVideoScreen(
                            videoData
                        )
                    )
                }
            })

        viewModel.favouriteVideosLiveData.observe(viewLifecycleOwner, favouriteVideosObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
    }

    private val favouriteVideosObserver = Observer<List<VideoData?>?> {
        myWishAdapter.submitList(it)
    }

    private val errorObserver = Observer<String> { errorMessage ->
        // TODO : HUMOYUN AKA, errorni toast qilish kerak
        if (errorMessage.equals(getString(R.string.internet_disconnected))) {
            // error internet yo'q bo'ladi bu yerda, screen ochiladi
        } else {
            // errorMessage ni chiqarib qo'ya qoling
        }
    }
}