package com.lbteam.priont.ui.search

import com.lbteam.priont.model.Product

enum class SearchStatus {
    Initial, Loading, Success, Empty
}

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val allProducts: List<Product>,
    val searchResults: List<Product> = emptyList()
) {
    val status: SearchStatus
        get() = when {
            isLoading -> SearchStatus.Loading
            query.isEmpty() -> SearchStatus.Initial
            searchResults.isEmpty() -> SearchStatus.Empty
            else -> SearchStatus.Success
        }

    companion object {
        fun dummy() = SearchUiState(
            allProducts = List(10) { Product.dummy(id = "$it") },
            searchResults = List(3) { Product.dummy(id = "$it") }
        )
    }
}