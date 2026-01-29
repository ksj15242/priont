package com.lbteam.priont.ui.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.type.PeriodType
import com.lbteam.priont.ui.product.LineChartState
import com.lbteam.priont.ui.product.ProductStatus
import com.lbteam.priont.ui.product.components.line.PriceLineChartConfig
import com.lbteam.priont.ui.product.components.line.PriceLineChartData
import com.lbteam.priont.ui.product.components.line.getMarkerText
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape

private val LINE_CHART_TABS = listOf(PeriodType.DAY_7, PeriodType.DAY_30)

@Composable
fun PriceLineChartSection(
    status: ProductStatus,
    lineChartState: LineChartState,
    onLineChartTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PriceLineChartTab(
            selectedLineChartTabIndex = lineChartState.selectedTabIndex,
            onLineChartTabSelected = onLineChartTabSelected
        )

        when (status) {
            ProductStatus.Success -> PriceLineChart(
                priceLineChartData = lineChartState.selectedData,
                modifier = Modifier.height(200.dp)
            )

            ProductStatus.Loading -> PriceLineChartLoading(
                modifier = Modifier.height(200.dp)
            )

            ProductStatus.Empty -> PriceLineChartEmpty(
                modifier = Modifier.height(200.dp)
            )

            ProductStatus.Error -> {}
        }
    }
}

@Composable
fun PriceLineChartEmpty(
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
            text = stringResource(R.string.product_line_chart_empty),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PriceLineChartLoading(
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
private fun PriceLineChartTab(
    selectedLineChartTabIndex: Int,
    onLineChartTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedLineChartTabIndex,
        edgePadding = 0.dp,
        containerColor = Color.White,
        divider = { HorizontalDivider(color = Color.White) },
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedLineChartTabIndex]),
                height = 2.dp,
                color = Color(0xFF1B4332)
            )
        }
    ) {
        LINE_CHART_TABS.forEachIndexed { index, tag ->
            val isSelected = selectedLineChartTabIndex == index
            Tab(
                selected = isSelected,
                onClick = { onLineChartTabSelected(index) },
                text = {
                    Text(
                        text = tag.label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
fun PriceLineChart(
    priceLineChartData: PriceLineChartData,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(
        priceLineChartData.xValues,
        priceLineChartData.yValues
    ) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = priceLineChartData.xValues,
                    y = priceLineChartData.yValues
                )
            }
        }
    }

    PriceLineChart(
        priceLineChartData = priceLineChartData,
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
private fun PriceLineChart(
    priceLineChartData: PriceLineChartData,
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val rangeProvider = remember(priceLineChartData.xAxisRange, priceLineChartData.yAxisRange) {
        CartesianLayerRangeProvider.fixed(
            minX = priceLineChartData.xAxisRange.first,
            maxX = priceLineChartData.xAxisRange.second,
            minY = priceLineChartData.yAxisRange.first,
            maxY = priceLineChartData.yAxisRange.second
        )
    }

    val lineFill = remember(PriceLineChartConfig.Chart.lineColor) {
        LineCartesianLayer.LineFill.single(fill(PriceLineChartConfig.Chart.lineColor))
    }

    val areaFill = remember(PriceLineChartConfig.Chart.areaColor) {
        LineCartesianLayer.AreaFill.single(fill(ShaderProvider.verticalGradient(PriceLineChartConfig.Chart.areaColor)))
    }

    val pointShape = rememberShapeComponent(
        fill = fill(PriceLineChartConfig.Chart.pointFillColor),
        shape = CorneredShape.Pill,
        strokeFill = fill(PriceLineChartConfig.Chart.pointStrokeColor),
        strokeThickness = PriceLineChartConfig.Chart.pointStrokeThickness
    )

    val pointProvider = remember(pointShape) {
        LineCartesianLayer.PointProvider.single(LineCartesianLayer.point(pointShape))
    }

    val maxMarker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(),
        labelPosition = DefaultCartesianMarker.LabelPosition.Top,
        valueFormatter = DefaultCartesianMarker.ValueFormatter.default(PriceLineChartConfig.Chart.maxTagFormat),
    )

    val minMarker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(),
        labelPosition = DefaultCartesianMarker.LabelPosition.Bottom,
        valueFormatter = DefaultCartesianMarker.ValueFormatter.default(PriceLineChartConfig.Chart.minTagFormat),
    )

    val averageLine = averageHorizontalLine(priceLineChartData.average)
    val decorations = remember(averageLine) { listOf(averageLine) }

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = lineFill,
                            areaFill = areaFill,
                            pointProvider = pointProvider,
                        )
                    ),
                rangeProvider = rangeProvider,
            ),
            persistentMarkers = {
                maxMarker at priceLineChartData.maxIndex
                minMarker at priceLineChartData.minIndex
            },
            marker = rememberMarker(
                valueFormatter = { _, targets ->
                    targets.firstOrNull()?.let {
                        priceLineChartData.getMarkerText(it.x.toInt())
                    } ?: "-"
                },
                showIndicator = true
            ),
            decorations = decorations
        ),
        modelProducer,
        modifier
            .background(Color.White)
            .padding(8.dp),
        rememberVicoScrollState(scrollEnabled = false),
    )
}

@Composable
private fun averageHorizontalLine(yValue: Double): HorizontalLine {

    val line = rememberLineComponent(
        fill = fill(PriceLineChartConfig.AverageLine.avgLineColor),
        thickness = PriceLineChartConfig.AverageLine.avgLineThickness,
        shape = DashedShape()
    )

    val labelComponent = rememberTextComponent(
        color = PriceLineChartConfig.AverageLine.avgLabelColor,
        margins = PriceLineChartConfig.AverageLine.avgLabelMargin,
        padding = PriceLineChartConfig.AverageLine.avgLabelPadding,
        background =
            shapeComponent(
                fill(PriceLineChartConfig.AverageLine.avgLabelBackgroundColor),
                CorneredShape.rounded(4.dp)
            ),
        textSize = PriceLineChartConfig.AverageLine.avgLabelTextSize
    )

    return remember(yValue) {
        HorizontalLine(
            y = { yValue },
            line = line,
            labelComponent = labelComponent,
            label = { PriceLineChartConfig.AverageLine.AVG_LABEL },
            verticalLabelPosition = Position.Vertical.Top
        )
    }
}