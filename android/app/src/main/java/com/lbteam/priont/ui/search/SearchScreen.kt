package com.lbteam.priont.ui.search

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.lbteam.priont.ui.search.components.CustomSearchBar
import com.lbteam.priont.ui.search.components.SearchEmpty
import com.lbteam.priont.ui.search.components.SearchList
import com.lbteam.priont.ui.search.components.SearchLoading
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun SearchScreenPreview() {
    PriontTheme {
        SearchScreen(
            uiState = SearchUiState.dummy(),
            onQueryChanged = {},
            onProductClicked = {},
            onBack = {}
        )
    }
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onProductClicked: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current

    Box(
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
            .semantics { isTraversalGroup = true }
    ) {
        CustomSearchBar(
            query = uiState.query,
            onQueryChange = { onQueryChanged(it) },
            onBack = onBack,
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            when (uiState.status) {
                SearchStatus.Success, SearchStatus.Initial -> {
                    SearchList(
                        onProductClicked = onProductClicked,
                        items = uiState.searchResults
                    )
                }

                SearchStatus.Loading -> {
                    SearchLoading()
                }

                SearchStatus.Empty -> {
                    SearchEmpty(
                        query = uiState.query
                    )
                }
            }
        }
    }
}

