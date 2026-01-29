package com.lbteam.priont.ui.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lbteam.priont.ui.category.components.CategoryEmpty
import com.lbteam.priont.ui.category.components.CategoryList
import com.lbteam.priont.ui.category.components.CategoryLoading
import com.lbteam.priont.ui.components.ProductListScreenTopBar
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun CategoryScreenPreview() {
    PriontTheme {
        CategoryScreen(
            uiState = CategoryUiState.dummy(),
            onProductClicked = {},
            onBack = {}
        )
    }
}

@Composable
fun CategoryScreen(
    uiState: CategoryUiState,
    onProductClicked: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ProductListScreenTopBar(
                uiState.categoryLabel,
                onBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        when (uiState.status) {
            CategoryStatus.Loading -> CategoryLoading(modifier = Modifier.padding(innerPadding))

            CategoryStatus.Empty -> CategoryEmpty(
                categoryLabel = uiState.categoryLabel,
                modifier = Modifier.padding(innerPadding)
            )

            CategoryStatus.Success -> CategoryList(
                onProductClicked = onProductClicked,
                items = uiState.products,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}