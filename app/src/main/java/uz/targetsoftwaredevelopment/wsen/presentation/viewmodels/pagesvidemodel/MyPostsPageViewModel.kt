package uz.targetsoftwaredevelopment.wsen.presentation.viewmodels.pagesvidemodel

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.wsen.data.remote.responses.VideoData

interface MyPostsPageViewModel {
    val allMyVideosLiveData: LiveData<List<VideoData?>>
    val errorLiveData: LiveData<String>

    fun getAllMyVideos()
//    fun deleteMyVideo()
}