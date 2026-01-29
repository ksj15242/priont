package com.lbteam.priont.ui.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.type.PeriodType
import com.lbteam.priont.ui.components.ProductTags
import com.lbteam.priont.ui.product.PriceInfo
import com.lbteam.priont.ui.product.ProductInfo
import com.lbteam.priont.ui.product.ProductStatus
import com.lbteam.priont.ui.product.toPrice
import com.lbteam.priont.ui.product.toPriceSummary
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun ProductInfoSectionPreview() {
    PriontTheme {
        ProductInfoSection(
            status = ProductStatus.Success,
            productInfo = ProductInfo.dummy(),
            priceInfo = PriceInfo.dummy()
        )
    }
}

@Composable
fun ProductInfoSection(
    status: ProductStatus,
    productInfo: ProductInfo,
    priceInfo: PriceInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductInfoHeader(
            status = status,
            name = productInfo.name,
            unit = productInfo.unit,
            price = priceInfo.price
        )
        ProductTags(listOf(productInfo.variety, productInfo.grade))
        PriceComparisonSummary(
            status = status,
            price = priceInfo.price,
            avg7d = priceInfo.avg7d,
            avg30d = priceInfo.avg30d
        )
    }
}

@Composable
fun ProductInfoHeader(
    status: ProductStatus,
    name: String,
    unit: String,
    price: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.common_product_title, name, unit),
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if (status == ProductStatus.Success) price.toPrice() else stringResource(R.string.common_product_price_empty),
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun PriceComparisonSummary(
    status: ProductStatus,
    price: Int,
    avg7d: Int,
    avg30d: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (status == ProductStatus.Success) (price - avg7d).toPriceSummary(PeriodType.DAY_7.label) else "",
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .defaultMinSize(minHeight = 16.dp)
        )
        Text(
            text = if (status == ProductStatus.Success) (price - avg30d).toPriceSummary(PeriodType.DAY_30.label) else "",
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .defaultMinSize(minHeight = 16.dp)
        )
    }
}