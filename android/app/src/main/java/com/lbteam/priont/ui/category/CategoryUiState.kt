package com.lbteam.priont.ui.category

import com.lbteam.priont.model.Product

enum class CategoryStatus {
    Loading, Success, Empty
}

data class CategoryUiState(
    val isLoading: Boolean = true,
    val categoryId: String = "",
    val categoryLabel: String = "",
    val products: List<Product> = emptyList()
) {
    val status: CategoryStatus
        get() = when {
            isLoading -> CategoryStatus.Loading
            products.isEmpty() -> CategoryStatus.Empty
            else -> CategoryStatus.Success
        }

    companion object {
        fun dummy() = CategoryUiState(
            categoryId = "100",
            categoryLabel = "식량작물",
            products = List(10) { Product.dummy("$it") }
        )
    }
}