package uz.targetsoftwaredevelopment.myapplication.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import uz.targetsoftwaredevelopment.myapplication.R
import uz.targetsoftwaredevelopment.myapplication.data.enums.SplashOpenScreenTypes
import uz.targetsoftwaredevelopment.myapplication.databinding.ScreenSplashBinding
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.screensviewmodel.SplashScreenViewModel
import uz.targetsoftwaredevelopment.myapplication.presentation.viewmodels.screensviewmodel.impl.SplashScreenViewModelImpl

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private val viewModel: SplashScreenViewModel by viewModels<SplashScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            delay(2000L)
            viewModel.splashOpenScreenLiveData.observe(viewLifecycleOwner, splashOpenScreenObserver)
            viewModel.getSplashOpenScreen()
        }
    }

    private val splashOpenScreenObserver = Observer<SplashOpenScreenTypes> {
        if (it == SplashOpenScreenTypes.BASE_SCREEN) {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToBasicScreen())
        } else {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToAuthScreen())
        }
    }
}
