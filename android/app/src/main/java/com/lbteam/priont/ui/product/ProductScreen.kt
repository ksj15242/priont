package com.lbteam.priont.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.model.type.RegionType
import com.lbteam.priont.ui.components.ProductScreenTopBar
import com.lbteam.priont.ui.product.components.PriceColumnChartSection
import com.lbteam.priont.ui.product.components.PriceLineChartSection
import com.lbteam.priont.ui.product.components.ProductInfoSection
import com.lbteam.priont.ui.product.components.RegionSection
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun ProductScreenPreview() {
    PriontTheme {
        ProductScreen(
            uiState = ProductUiState.dummy(),
            onBack = {},
            onRegionSelected = {},
            onLineChartTabSelected = {}
        )
    }
}

@Composable
fun ProductScreen(
    uiState: ProductUiState,
    onLineChartTabSelected: (Int) -> Unit,
    onBack: () -> Unit,
    onRegionSelected: (RegionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ProductScreenTopBar(uiState.productInfo.category, onBack)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .background(Color.White)
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProductInfoSection(
                    status = uiState.status,
                    productInfo = uiState.productInfo,
                    priceInfo = uiState.priceInfo,
                    modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                RegionSection(
                    selectedRegion = uiState.regionState.selectedType,
                    onRegionSelected = onRegionSelected,
                )
            }
            item {
                PriceLineChartSection(
                    status = uiState.status,
                    lineChartState = uiState.lineChartState,
                    onLineChartTabSelected = onLineChartTabSelected,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                PriceColumnChartSection(
                    selectedColumnChartData = uiState.columnChartState.data,
                    currentRegion = uiState.regionState.selectedType,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

