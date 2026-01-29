package com.lbteam.priont.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.Product
import com.lbteam.priont.ui.components.ProductListItem

@Composable
fun SearchEmpty(
    query: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.search_result_empty, query),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SearchLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun SearchList(
    onProductClicked: (String) -> Unit,
    items: List<Product>,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    focusManager.clearFocus()
                }
            }
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            ProductListItem(
                onProductClicked = {
                    focusManager.clearFocus()
                    onProductClicked(it)
                },
                item = item,
            )
        }
    }
}