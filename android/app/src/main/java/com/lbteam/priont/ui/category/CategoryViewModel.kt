package com.lbteam.priont.ui.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lbteam.priont.data.AppRepository
import com.lbteam.priont.model.type.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: AppRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = checkNotNull(savedStateHandle["categoryId"])

    private val _categoryUiState = MutableStateFlow(
        CategoryUiState(
            categoryId = categoryId,
            categoryLabel = CategoryType.findLabelById(categoryId)
        )
    )
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    init {
        loadProductsByCategory()
    }

    private fun loadProductsByCategory() {
        val products = repository.getProductsByCategory(categoryId)

        _categoryUiState.update {
            it.copy(
                products = products,
                isLoading = false
            )
        }
    }
}