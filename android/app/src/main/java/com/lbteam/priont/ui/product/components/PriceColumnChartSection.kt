package com.lbteam.priont.ui.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.type.RegionType
import com.lbteam.priont.ui.product.components.bar.PriceColumnChartConfig
import com.lbteam.priont.ui.product.components.bar.PriceColumnChartData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider

@Composable
fun PriceColumnChartSection(
    selectedColumnChartData: PriceColumnChartData,
    currentRegion: RegionType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PriceColumnChartHeader()
        PriceColumnChart(
            selectedColumnChartData = selectedColumnChartData,
            currentRegion = currentRegion,
        )
    }
}

@Composable
private fun PriceColumnChartHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.product_column_chart_header),
        color = Color.Black,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        modifier = modifier
            .fillMaxWidth()
    )
}

private val BottomAxisLabelKey = ExtraStore.Key<List<RegionType>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()].label
}

@Composable
fun PriceColumnChart(
    selectedColumnChartData: PriceColumnChartData,
    currentRegion: RegionType,
    modifier: Modifier = Modifier
) {

    val normalBar = rememberColumnBar(false)
    val highlightBar = rememberColumnBar(true)

    val columnProvider = remember {
        object : ColumnCartesianLayer.ColumnProvider {
            override fun getColumn(
                entry: ColumnCartesianLayerModel.Entry,
                seriesIndex: Int,
                extraStore: ExtraStore,
            ) = if (entry.x.toInt() == 0) highlightBar else normalBar

            override fun getWidestSeriesColumn(
                seriesIndex: Int,
                extraStore: ExtraStore,
            ) = normalBar
        }
    }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit, currentRegion) {
        modelProducer.runTransaction {
            columnSeries { series(selectedColumnChartData.yValues) }
            extras { it[BottomAxisLabelKey] = selectedColumnChartData.xValues }
        }
    }

    PriceColumnChart(modelProducer, columnProvider, selectedColumnChartData.yAxisRange, modifier)
}

@Composable
private fun rememberColumnBar(isCurrentRegion: Boolean): LineComponent {
    val barColor = remember(isCurrentRegion) {
        if (isCurrentRegion) {
            ShaderProvider.verticalGradient(PriceColumnChartConfig.ColumnBar.activeGradientColor)
        } else {
            ShaderProvider.verticalGradient(PriceColumnChartConfig.ColumnBar.gradientColor)
        }
    }

    return rememberLineComponent(
        fill = fill(barColor),
        thickness = PriceColumnChartConfig.ColumnBar.thickness,
        shape = PriceColumnChartConfig.ColumnBar.shape
    )
}

@Composable
private fun PriceColumnChart(
    modelProducer: CartesianChartModelProducer,
    columnProvider: ColumnCartesianLayer.ColumnProvider,
    yAxisRange: Pair<Double, Double>,
    modifier: Modifier = Modifier
) {
    val rangeProvider = remember(yAxisRange) {
        if (yAxisRange.second == 0.0) CartesianLayerRangeProvider.auto()
        else CartesianLayerRangeProvider.fixed(
            minY = 0.0,
            maxY = yAxisRange.second * 1.15
        )
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                dataLabel = rememberTextComponent(
                    textSize = PriceColumnChartConfig.ColumnBar.labelTextSize,
                    margins = Insets(bottomDp = 8f)
                ),
                dataLabelValueFormatter = CartesianValueFormatter.decimal(PriceColumnChartConfig.ColumnBar.labelFormat),
                columnProvider = columnProvider,
                mergeMode = remember { { ColumnCartesianLayer.MergeMode.Stacked } },
                rangeProvider = rangeProvider
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
            ),
            layerPadding = { cartesianLayerPadding(8.dp, 8.dp) },
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .background(Color.White)
            .padding(start = 8.dp, bottom = 8.dp, end = 8.dp, top = 8.dp),
        scrollState = rememberVicoScrollState(scrollEnabled = true),
    )
}

