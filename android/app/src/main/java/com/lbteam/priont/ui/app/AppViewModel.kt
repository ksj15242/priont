package com.lbteam.priont.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbteam.priont.data.AppRepository
import com.lbteam.priont.data.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: AppRepository,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _appUiState = MutableStateFlow<AppUiState>(AppUiState.Uninitialized)
    val appUiState = _appUiState.asStateFlow()

    init {
        syncAndLoadData()
    }

    fun syncAndLoadData() {
        viewModelScope.launch {
            _appUiState.update {
                AppUiState.Loading
            }

            val result = runCatching {
                syncManager.syncData()
                repository.loadData()

                if (!repository.hasData()) {
                    throw IllegalStateException("Data error")
                }
            }

            _appUiState.update {
                result.fold(
                    onSuccess = { AppUiState.Success(isInitialized = true) },
                    onFailure = { AppUiState.Error(it.message ?: "Error") }
                )
            }
        }
    }
}