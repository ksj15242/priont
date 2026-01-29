package com.lbteam.priont.ui.app

sealed interface AppUiState {
    data object Uninitialized : AppUiState
    data object Loading : AppUiState
    data class Success(val isInitialized: Boolean) : AppUiState
    data class Error(val message: String) : AppUiState
}