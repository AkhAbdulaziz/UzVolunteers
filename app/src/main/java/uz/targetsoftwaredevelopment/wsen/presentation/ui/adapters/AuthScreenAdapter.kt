package uz.targetsoftwaredevelopment.wsen.presentation.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.targetsoftwaredevelopment.wsen.data.remote.requests.LoginUserRequest
import uz.targetsoftwaredevelopment.wsen.data.remote.requests.RegisterUserRequest
import uz.targetsoftwaredevelopment.wsen.presentation.ui.pages.LoginPage
import uz.targetsoftwaredevelopment.wsen.presentation.ui.pages.RegisterPage

class AuthScreenAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private var registerBtnClickListener: ((RegisterUserRequest) -> Unit)? = null
    fun setRegisterBtnClickListener(f: (RegisterUserRequest) -> Unit) {
        registerBtnClickListener = f
    }

    private var loginBtnClickListener: ((LoginUserRequest) -> Unit)? = null
    fun setLoginBtnClickListener(f: (LoginUserRequest) -> Unit) {
        loginBtnClickListener = f
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                LoginPage().apply {
                    setLoginBtnClickListener {
                        loginBtnClickListener?.invoke(it)
                    }
                }
            }
            else -> {
                RegisterPage().apply {
                    setRegisterBtnClickListener {
                        registerBtnClickListener?.invoke(it)
                    }
                }
            }
        }
}
