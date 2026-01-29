package com.lbteam.priont.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbteam.priont.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val allProducts = repository.getProducts()

    private val _searchUiState = MutableStateFlow(
        SearchUiState(allProducts = allProducts, searchResults = allProducts)
    )

    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onQueryChange(newQuery: String) {
        _searchUiState.update { it.copy(query = newQuery, isLoading = true) }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchUiState
                .map { it.query }
                .distinctUntilChanged()
                .debounce(300L)
                .collect { debouncedQuery ->
                    searchProducts(debouncedQuery)
                }
        }
    }

    private fun searchProducts(query: String) {
        val trimmed = query.trim()

        _searchUiState.update { curState ->
            val filtered = if (trimmed.isEmpty()) {
                curState.allProducts
            } else {
                curState.allProducts.filter { it.name.contains(trimmed, ignoreCase = true) }
            }

            curState.copy(
                searchResults = filtered,
                isLoading = false
            )
        }
    }
}