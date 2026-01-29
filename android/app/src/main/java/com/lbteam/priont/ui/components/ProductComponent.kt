package com.lbteam.priont.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.Product
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
fun ProductItemPreview() {
    PriontTheme {
        ProductListItem(
            item = Product.dummy(),
            onProductClicked = {}
        )
    }
}

@Composable
fun ProductListItem(
    item: Product,
    onProductClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.common_product_title, item.name, item.unit),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp)
                )
            },
            trailingContent = {
                ProductTags(
                    tags = listOf(item.variety),
                    modifier = Modifier.padding(end = 16.dp)
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .clickable {
                    onProductClicked(item.id)
                }
        )
    }
}

@Composable
fun ProductTags(
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        tags.forEach { tag ->
            ProductTagChip(tag)
        }
    }
}

@Composable
fun ProductTagChip(
    tag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFEAF2EF),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier.height(24.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF374743),
                textAlign = TextAlign.Center
            )
        }

    }
}