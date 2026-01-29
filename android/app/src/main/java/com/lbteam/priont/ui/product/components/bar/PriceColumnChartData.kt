package com.lbteam.priont.ui.product.components.bar

import com.lbteam.priont.model.type.RegionType

data class PriceColumnChartData(
    val xValues: List<RegionType> = emptyList(),
    val yValues: List<Int> = emptyList(),

    val yAxisRange: Pair<Double, Double> = Pair(0.0, 0.0),
)
