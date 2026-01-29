package com.lbteam.priont.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.model.type.CategoryType
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
fun HomeCategorySectionPreview() {
    PriontTheme {
        HomeCategorySection({})
    }
}

@Composable
fun HomeCategorySection(
    onCategoryClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CategoryType.entries) { category ->
                CategoryItem(
                    onCategoryClicked = onCategoryClicked,
                    category = category,
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    onCategoryClicked: (String) -> Unit,
    category: CategoryType,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Surface(
            onClick = { onCategoryClicked(category.category) },
            modifier = Modifier
                .size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Image(
                painter = painterResource(category.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .padding(12.dp)
            )
        }
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}