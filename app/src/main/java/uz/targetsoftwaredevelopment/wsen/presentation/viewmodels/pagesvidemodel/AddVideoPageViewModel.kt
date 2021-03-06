package uz.targetsoftwaredevelopment.wsen.presentation.viewmodels.pagesvidemodel

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.wsen.data.remote.requests.AddVideoRequest
import uz.targetsoftwaredevelopment.wsen.data.remote.responses.AddVideoResponse
import java.io.File

interface AddVideoPageViewModel {
    val addVideoResponseLiveData: LiveData<AddVideoResponse>
    val errorLiveData: LiveData<Unit>
    val videoCompressedLiveData: LiveData<File>

    fun addVideo(data: AddVideoRequest)
    fun videoCompressed(file : File)
}