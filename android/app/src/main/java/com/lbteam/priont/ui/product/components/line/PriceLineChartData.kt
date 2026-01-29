package com.lbteam.priont.ui.product.components.line

import androidx.compose.runtime.Immutable

@Immutable
data class PriceLineChartData(
    val dates: List<String> = emptyList(),
    val prices: List<Int?> = emptyList(),

    val xValues: List<Int> = emptyList(),
    val yValues: List<Int> = emptyList(),

    val average: Double = 0.0,

    val minIndex: Double = 0.0,
    val maxIndex: Double = 0.0,

    val yAxisRange: Pair<Double, Double> = Pair(0.0, 0.0),
    val xAxisRange: Pair<Double, Double> = Pair(0.0, 0.0),
) {
    companion object {
        fun dummy7d() = PriceLineChartData(
            dates = listOf(
                "2025-11-24",
                "2025-11-25",
                "2025-11-26",
                "2025-11-27",
                "2025-11-28",
                "2025-12-01",
                "2025-12-02"
            ),
            prices = listOf(
                62530,
                62450,
                62440,
                null,
                62427,
                62451,
                62261
            ),
            xValues = listOf(0, 1, 2, 4, 5, 6),
            yValues = listOf(
                62530,
                62450,
                62440,
                62427,
                62451,
                62261
            ),
            minIndex = 6.0,
            maxIndex = 0.0,
            xAxisRange = Pair(0.0, 6.0),
            yAxisRange = Pair(62207.2, 62583.8),
            average = 62422.0
        )

        fun dummy30d() = PriceLineChartData(
            dates = listOf(
                "2025-10-22",
                "2025-10-23",
                "2025-10-24",
                "2025-10-27",
                "2025-10-28",
                "2025-10-29",
                "2025-10-30",
                "2025-10-31",
                "2025-11-03",
                "2025-11-04",
                "2025-11-05",
                "2025-11-06",
                "2025-11-07",
                "2025-11-10",
                "2025-11-11",
                "2025-11-12",
                "2025-11-13",
                "2025-11-14",
                "2025-11-17",
                "2025-11-18",
                "2025-11-19",
                "2025-11-20",
                "2025-11-21",
                "2025-11-24",
                "2025-11-25",
                "2025-11-26",
                "2025-11-27",
                "2025-11-28",
                "2025-12-01",
                "2025-12-02"
            ),
            prices = listOf(
                65756,
                65398,
                65380,
                65288,
                65286,
                65074,
                65118,
                65118,
                64872,
                64886,
                65094,
                64988,
                64914,
                64804,
                64794,
                64814,
                64516,
                64253,
                63606,
                63592,
                63610,
                62552,
                62402,
                62530,
                62450,
                62440,
                62396,
                62427,
                62451,
                62261
            ),
            average = 64223.0
        )
    }
}

fun PriceLineChartData.getMarkerText(index: Int): String {
    val date = dates.getOrNull(index) ?: return "-"
    val price = prices.getOrNull(index) ?: 0
    return "$date : ${PriceLineChartConfig.Chart.priceFormat.format(price)}"
}