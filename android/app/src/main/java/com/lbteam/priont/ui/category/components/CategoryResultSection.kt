package com.lbteam.priont.ui.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.Product
import com.lbteam.priont.ui.components.ProductListItem

@Composable
fun CategoryEmpty(
    categoryLabel: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.category_empty_msg, categoryLabel),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CategoryLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        )
    }
}

@Composable
fun CategoryList(
    onProductClicked: (String) -> Unit,
    items: List<Product>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            ProductListItem(
                onProductClicked = onProductClicked,
                item = item,
            )
        }
    }
}