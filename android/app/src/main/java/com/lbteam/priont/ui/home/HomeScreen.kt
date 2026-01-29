package com.lbteam.priont.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.ui.home.components.HomeCategorySection
import com.lbteam.priont.ui.home.components.HomeFooterSection
import com.lbteam.priont.ui.home.components.HomeHeaderSection
import com.lbteam.priont.ui.home.components.HomeRankingSection
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    PriontTheme {
        HomeScreen(
            uiState = HomeUiState.dummy(),
            onRankingTabSelected = {},
            onSearchClicked = {},
            onCategoryClicked = {},
            onProductClicked = {}
        )
    }
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onRankingTabSelected: (Int) -> Unit,
    onSearchClicked: () -> Unit,
    onCategoryClicked: (String) -> Unit,
    onProductClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item { HomeHeaderSection(onSearchClicked = onSearchClicked) }
            item { HomeCategorySection(onCategoryClicked = onCategoryClicked) }
            item {
                HomeRankingSection(
                    topRankSection = uiState.selectedTopRankSection,
                    onRankingTabSelected = onRankingTabSelected,
                    selectedRankingTabIndex = uiState.selectedRankingTabIndex,
                    onProductClicked = onProductClicked
                )
            }
            item { HomeFooterSection() }
        }
    }
}


