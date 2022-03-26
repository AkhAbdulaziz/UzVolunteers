package uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.pagesvidemodel

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.myapplication.data.remote.requests.UserData

interface ProfilePageViewModel {
    val getUserDataLiveData: LiveData<UserData>
    val errorLiveData: LiveData<String>
    val getUserPhoneNumberLiveData: LiveData<String>
    val editUserDataLiveData: LiveData<UserData>

    fun getUserData()

    fun getUserPhoneNumber()

    fun setUserPhoneNumber(phoneNumber: String)

    fun editUserData(userData: UserData)
}