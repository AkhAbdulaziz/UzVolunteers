package uz.targetsoftwaredevelopment.myapplication.presentation.ui.pages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.myapplication.R
import uz.targetsoftwaredevelopment.myapplication.data.remote.responses.VideoData
import uz.targetsoftwaredevelopment.myapplication.databinding.PageAllVideoBinding
import uz.targetsoftwaredevelopment.myapplication.presentation.ui.adapters.AllVideoRvAdapter
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.pagesvidemodel.AllVideoPageViewModel
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.pagesvidemodel.impl.AllVideoPageViewModelImpl
import uz.targetsoftwaredevelopment.myapplication.utils.scope

@AndroidEntryPoint
class AllVideoPage : Fragment(R.layout.page_all_video) {
    private val binding by viewBinding(PageAllVideoBinding::bind)
    private val viewModel: AllVideoPageViewModel by viewModels<AllVideoPageViewModelImpl>()
    private lateinit var allVideoRvAdapter: AllVideoRvAdapter
//    private var list = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        loadAllVideoData()
        viewModel.getAllVideos()
        viewModel.allVideosLiveData.observe(viewLifecycleOwner, allVideosObserver)
    }

    private fun loadAllVideoData() {
        allVideoRvAdapter =
            AllVideoRvAdapter(requireContext(), object : AllVideoRvAdapter.OnItemClickListener {
                override fun onItemClick(item: Int) {
                    findNavController().navigate(R.id.watchVideoScreen)
                }

                override fun onShareClick(item: Int) {
                    Toast.makeText(requireContext(), "send", Toast.LENGTH_SHORT).show()
                }

                override fun onMenuClick(item: Int) {
                    Toast.makeText(requireContext(), "spam", Toast.LENGTH_SHORT).show()
                }
            })

//        allVideoRvAdapter.submitList(list as List<Any>?)
        binding.carouselRv.adapter = allVideoRvAdapter
    }

    private val allVideosObserver = Observer<List<VideoData?>?> {
        allVideoRvAdapter.submitList(it)
    }
}