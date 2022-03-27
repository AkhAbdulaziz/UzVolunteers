package uz.targetsoftwaredevelopment.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.targetsoftwaredevelopment.myapplication.data.enums.SplashOpenScreenTypes
import uz.targetsoftwaredevelopment.myapplication.data.remote.requests.*
import uz.targetsoftwaredevelopment.myapplication.data.remote.responses.*

interface BaseRepository {

    fun getSplashOpenScreen(): SplashOpenScreenTypes

    fun setSplashOpenScreen(type: SplashOpenScreenTypes)

    fun registerUser(data: RegisterUserRequest): Flow<Result<RegisterUserResponse?>>

    fun loginUser(data: LoginUserRequest): Flow<Result<LoginUserResponse?>>

    fun getMainPageData(): Flow<Result<MainPageDataResponse?>>

    fun getAllVideos(): Flow<Result<List<VideoData?>?>>

    fun addVideo(data: AddVideoRequest): Flow<Result<AddVideoResponse?>>

    fun setRegisterErrorListener(f: (String) -> Unit)

    fun setLoginErrorListener(f: (String) -> Unit)

    fun getUserData(): Flow<Result<UserData>>

    fun getUserPhoneNumber(): String

    fun setUserPhoneNumber(phoneNumber: String)

    fun editUserData(userData: UserData): Flow<Result<UserData>>

    fun getAllMyVideos(): Flow<Result<List<VideoData?>>>

    fun editMyVideo(videoData: EditVideoRequest): Flow<Result<EditVideoResponse>>

    fun getAllFavouriteVideos(): Flow<Result<AllFavouriteVideosResponse>>

    fun changeLike(videoData: VideoData): Flow<Result<LikeVideoResponse>>
}
