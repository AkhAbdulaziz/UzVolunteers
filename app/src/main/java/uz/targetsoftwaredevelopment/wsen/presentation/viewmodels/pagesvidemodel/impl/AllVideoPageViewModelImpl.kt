package uz.targetsoftwaredevelopment.wsen.presentation.viewmodels.pagesvidemodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.wsen.R
import uz.targetsoftwaredevelopment.wsen.app.App
import uz.targetsoftwaredevelopment.wsen.data.remote.responses.LikeVideResponseData
import uz.targetsoftwaredevelopment.wsen.data.remote.responses.VideoData
import uz.targetsoftwaredevelopment.wsen.domain.repository.BaseRepository
import uz.targetsoftwaredevelopment.wsen.presentation.viewmodels.pagesvidemodel.AllVideoPageViewModel
import uz.targetsoftwaredevelopment.wsen.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class AllVideoPageViewModelImpl @Inject constructor(private val baseRepository: BaseRepository) :
    ViewModel(), AllVideoPageViewModel {
    override val allVideosLiveData = MutableLiveData<List<VideoData?>?>()
    override val errorLiveData = MutableLiveData<String>()

    override val changeLikeLiveData = MutableLiveData<LikeVideResponseData?>()

    override fun getAllVideos() {
        if (isConnected()) {
            baseRepository.getAllVideos().onEach {
                it.onSuccess {
                    allVideosLiveData.value = it
                }
                it.onFailure {
                    errorLiveData.value = App.instance.getString(R.string.some_error)
                }
            }.launchIn(viewModelScope)
        } else {
            errorLiveData.value = App.instance.getString(R.string.internet_disconnected)
        }
    }

    override fun changeLikeVideo(videoData : VideoData) {
        if(isConnected()){
            baseRepository.changeLike(videoData).onEach {
                it.onSuccess {likeVideoResponse->
                    changeLikeLiveData.value = likeVideoResponse.data
                }
                it.onFailure {
                    errorLiveData.value = App.instance.getString(R.string.some_error)
                }
            }.launchIn(viewModelScope)
        }else{
            errorLiveData.value = App.instance.getString(R.string.internet_disconnected)
        }
    }
}