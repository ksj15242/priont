package com.lbteam.priont.ui.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.model.type.RegionType
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun RegionSelectorPreview() {
    PriontTheme {
        RegionSection(
            selectedRegion = RegionType.All,
            onRegionSelected = {},
        )
    }
}

@Composable
fun RegionSection(
    selectedRegion: RegionType,
    onRegionSelected: (RegionType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(RegionType.entries) { region ->
            RegionChip(
                label = region.label,
                isSelected = selectedRegion == region,
                onRegionSelected = { onRegionSelected(region) }
            )
        }
    }
}

@Composable
fun RegionChip(
    label: String,
    isSelected: Boolean,
    onRegionSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier.width(60.dp),
        selected = isSelected,
        onClick = onRegionSelected,
        label = {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        },
        shape = RoundedCornerShape(12.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = Color.LightGray,
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedContainerColor = Color(0xFF2E6045),
            selectedLabelColor = Color.White,
            labelColor = Color.Black
        )
    )
}