package com.lbteam.priont.ui.home

import androidx.lifecycle.ViewModel
import com.lbteam.priont.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _homeUiState =
        MutableStateFlow(HomeUiState(topRankData = repository.getTopRanks()))

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    fun onRankingTabSelected(index: Int) {
        _homeUiState.update {
            it.copy(selectedRankingTabIndex = index)
        }
    }
}