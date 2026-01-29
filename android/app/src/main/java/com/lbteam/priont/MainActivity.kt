package com.lbteam.priont

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lbteam.priont.ui.app.AppErrorScreen
import com.lbteam.priont.ui.app.AppLoadingScreen
import com.lbteam.priont.ui.app.AppUiState
import com.lbteam.priont.ui.app.AppViewModel
import com.lbteam.priont.ui.app.PriontApp
import com.lbteam.priont.ui.theme.PriontTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            PriontTheme {
                val viewModel: AppViewModel = hiltViewModel()

                val appUiState by viewModel.appUiState.collectAsStateWithLifecycle()

                when (appUiState) {
                    is AppUiState.Success -> PriontApp()
                    is AppUiState.Loading -> AppLoadingScreen()
                    is AppUiState.Error -> AppErrorScreen(
                        onRetry = { viewModel.syncAndLoadData() },
                        onExit = { finish() }
                    )

                    is AppUiState.Uninitialized -> {}
                }
            }

        }
    }
}