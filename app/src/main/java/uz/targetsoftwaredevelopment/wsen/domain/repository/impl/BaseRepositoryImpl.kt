package uz.targetsoftwaredevelopment.wsen.domain.repository.impl

import android.R.attr.description
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.targetsoftwaredevelopment.wsen.data.enums.SplashOpenScreenTypes
import uz.targetsoftwaredevelopment.wsen.data.local.LocalStorage
import uz.targetsoftwaredevelopment.wsen.data.local.SafeStorage
import uz.targetsoftwaredevelopment.wsen.data.remote.api.BaseApi
import uz.targetsoftwaredevelopment.wsen.data.remote.requests.*
import uz.targetsoftwaredevelopment.wsen.data.remote.responses.*
import uz.targetsoftwaredevelopment.wsen.domain.repository.BaseRepository
import java.lang.NullPointerException
import javax.inject.Inject


class BaseRepositoryImpl @Inject constructor(
    private val baseApi: BaseApi,
    private val localStorage: LocalStorage,
    private val safeStorage: SafeStorage
) : BaseRepository {
    private val gson = Gson()

    private var registerErrorListener: ((List<String>) -> Unit)? = null
    override fun setRegisterErrorListener(f: (List<String>) -> Unit) {
        registerErrorListener = f
    }

    private var loginErrorListener: ((String) -> Unit)? = null
    override fun setLoginErrorListener(f: (String) -> Unit) {
        loginErrorListener = f
    }

    override fun getSplashOpenScreen(): SplashOpenScreenTypes {
        return if (localStorage.splashOpenScreen == SplashOpenScreenTypes.BASE_SCREEN.name) {
            SplashOpenScreenTypes.BASE_SCREEN
        } else {
            SplashOpenScreenTypes.AUTH_SCREEN
        }
    }

    override fun setSplashOpenScreen(type: SplashOpenScreenTypes) {
        localStorage.splashOpenScreen = type.name
    }

    override fun registerUser(data: RegisterUserRequest): Flow<Result<RegisterUserResponse?>> =
        flow {
            val response = baseApi.registerUser(data)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                var errorKeysList: List<String> = ArrayList()
                /*response.errorBody()?.let {
                    errorKeysList = gson.fromJson(
                        it.string(),
                        ErrorRegisterResponse::class.java
                    ).errorsDictionary.keys().toList()

                    registerErrorListener?.invoke(errorKeysList)

                }*/
                emit(Result.failure(Throwable("bad")))
            }
        }.flowOn(Dispatchers.IO)

    override fun loginUser(data: LoginUserRequest): Flow<Result<LoginUserResponse?>> =
        flow {
            val response = baseApi.loginUser(data)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
                if (response.body()!!.token != null) {
                    localStorage.token = "Token ${response.body()!!.token!!}"
                    localStorage.userId = "${response.body()!!.user?.id}"
                }
                setSplashOpenScreen(SplashOpenScreenTypes.BASE_SCREEN)
            } else {
                var message = "Xatolik yuzaga keldi"
                /*if (response.errorBody() != null) {
                    message = gson.fromJson(
                        response.errorBody()!!.string(),
                        ErrorEmailResponse::class.java
                    ).email?.first().toString()
                }*/
                loginErrorListener?.invoke(message)
                emit(Result.failure(Throwable(message)))
            }
        }.flowOn(Dispatchers.IO)

    override fun logoutUser(): Flow<Result<LogoutResponse>> = flow {
        val response = baseApi.logoutUser(localStorage.token)
        Log.d("LOGOUTDDD", "logout repository")
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
            setSplashOpenScreen(SplashOpenScreenTypes.AUTH_SCREEN)
            Log.d("LOGOUTDDD", "logout repository success ")
        }
    }.flowOn(Dispatchers.IO)

    override fun getMainPageData(): Flow<Result<MainPageDataResponse?>> =
        flow {
            val response = baseApi.getMainPageData(localStorage.token)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAllVideos(): Flow<Result<List<VideoData?>?>> =
        flow {
            val response = baseApi.getAllVideos(localStorage.token)
            if (response.isSuccessful) {
                emit(Result.success(response.body()?.results))
            }
        }.flowOn(Dispatchers.IO)

    override fun addVideo(data: AddVideoRequest): Flow<Result<AddVideoResponse?>> =
        flow {

            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(),  data.video!!)
            val response = baseApi.addVideo(
                token = localStorage.token,
                video = MultipartBody.Part.createFormData(
                    "video",
                    data.video.name,
                    requestBody
                ),
                category = RequestBody.create("text/plain".toMediaTypeOrNull(), data.category.toString()),
                desc = RequestBody.create("text/plain".toMediaTypeOrNull(), data.desc.toString()),
                location = RequestBody.create("text/plain".toMediaTypeOrNull(), data.location.toString()),
                title = RequestBody.create("text/plain".toMediaTypeOrNull(), data.title.toString()),
            )
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                //TOTO Handler
                emit(Result.failure(NullPointerException()))
            }
        }.flowOn(Dispatchers.IO)

    override fun getUserData(): Flow<Result<UserDataResponse>> = flow {
        val response =
            baseApi.getUserData(
                localStorage.token,
                "${safeStorage.base_url}client/me/${localStorage.userId}/"
            )
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserPhoneNumber(): String {
        return localStorage.userPhoneNumber
    }

    override fun setUserPhoneNumber(phoneNumber: String) {
        localStorage.userPhoneNumber = phoneNumber
    }

    override fun editUserData(userDataRequest: UserDataRequest): Flow<Result<UserDataResponse>> =
        flow {
            val response = baseApi.editUserData(
                localStorage.token,
                "${safeStorage.base_url}client/me/${localStorage.userId}/", userDataRequest
            )
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            }
        }.flowOn(Dispatchers.IO)

    override fun getAllMyVideos(): Flow<Result<List<VideoData?>>> = flow {
        val response = baseApi.getAllMyVideos(localStorage.token)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.results!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun editMyVideo(videoData: EditVideoRequest): Flow<Result<EditVideoResponse>> = flow {
        val response =
            baseApi.editMyVideo(
                localStorage.token,
                "${safeStorage.base_url}api/my-post/${videoData.id}/",
                videoData
            )
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllFavouriteVideos(): Flow<Result<AllFavouriteVideosResponse>> = flow {
        val response = baseApi.getAllFavouriteVideos(localStorage.token)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun changeLike(videoData: VideoData): Flow<Result<LikeVideoResponse>> = flow {
        val response = baseApi.likeVideo(
            localStorage.token,
            "${safeStorage.base_url}api/wish-event/${videoData.id}/"
        )
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteMyVideo(videoData: VideoData): Flow<Result<String>> = flow {
        val response = baseApi.deleteMyVideo(
            localStorage.token,
            "${safeStorage.base_url}api/my-post/${videoData.id}/"
        )
        if (response.code() == 204) {
            emit(Result.success("Deleted"))
        }
    }.flowOn(Dispatchers.IO)

    override fun spamVideo(spamVideoRequest: SpamVideoRequest): Flow<Result<SpamVideoResponse>> =
        flow {
            val response = baseApi.spamVideo(localStorage.token, spamVideoRequest)
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            }
        }
}
