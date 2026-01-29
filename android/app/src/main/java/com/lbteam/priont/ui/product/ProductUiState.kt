package com.lbteam.priont.ui.product

import com.lbteam.priont.model.Region
import com.lbteam.priont.model.type.PeriodType
import com.lbteam.priont.model.type.RegionType
import com.lbteam.priont.ui.product.components.bar.PriceColumnChartData
import com.lbteam.priont.ui.product.components.line.PriceLineChartData

enum class ProductStatus {
    Loading, Success, Error, Empty
}

data class ProductUiState(
    val status: ProductStatus = ProductStatus.Loading,
    val productInfo: ProductInfo = ProductInfo(),
    val priceInfo: PriceInfo = PriceInfo(),
    val regionState: RegionState = RegionState(),
    val lineChartState: LineChartState = LineChartState(),
    val columnChartState: ColumnChartState = ColumnChartState()
) {
    companion object {
        fun dummy() = ProductUiState(
            status = ProductStatus.Success,
            productInfo = ProductInfo.dummy(),
            priceInfo = PriceInfo.dummy(),
            regionState = RegionState.dummy(),
            lineChartState = LineChartState.dummy(),
            columnChartState = ColumnChartState.dummy()
        )
    }
}

data class ProductInfo(
    val category: String = "",
    val name: String = "",
    val unit: String = "",
    val variety: String = "",
    val grade: String = "",
) {
    companion object {
        fun dummy() = ProductInfo(
            category = "식량작물",
            name = "쌀",
            unit = "20kg",
            variety = "20kg",
            grade = "상품",
        )
    }
}

data class PriceInfo(
    val price: Int = 0,
    val avg7d: Int = 0,
    val avg30d: Int = 0,
) {
    companion object {
        fun dummy() = PriceInfo(
            price = 65756,
            avg7d = 67233,
            avg30d = 63890,
        )
    }
}

data class RegionState(
    val selectedType: RegionType = RegionType.All,
    val allRegions: List<Region> = emptyList(),
    val selectedRegion: Region = Region(RegionType.All, emptyList(), emptyList()),
) {
    companion object {
        fun dummy() = RegionState(
            selectedType = RegionType.All,
            allRegions = List(24) { Region.dummy() },
            selectedRegion = Region.dummy(),
        )
    }
}

data class LineChartState(
    val selectedTabIndex: Int = 0,
    val dataMap: Map<PeriodType, PriceLineChartData> = emptyMap(),
) {
    val selectedData: PriceLineChartData
        get() = if (selectedTabIndex == 0) dataMap[PeriodType.DAY_7]
            ?: PriceLineChartData() else dataMap[PeriodType.DAY_30] ?: PriceLineChartData()

    companion object {
        fun dummy() = LineChartState(
            selectedTabIndex = 0,
            dataMap = mapOf(
                PeriodType.DAY_7 to PriceLineChartData.dummy7d(),
                PeriodType.DAY_30 to PriceLineChartData.dummy30d()
            )
        )
    }
}

data class ColumnChartState(
    val originData: Map<RegionType, Int> = emptyMap(),
    val data: PriceColumnChartData = PriceColumnChartData()
) {
    companion object {
        fun dummy() = ColumnChartState(
            originData = RegionType.entries.associateWith { 50000 },
            data = PriceColumnChartData(
                xValues = RegionType.entries,
                yValues = listOf(
                    42350, 48120, 45670, 41200, 49800, 43550, 47210, 44400,
                    46780, 40500, 42900, 48950, 45120, 41340, 47600, 43210,
                    49100, 40250, 46400, 44800, 42150, 48500, 45900, 41700
                )
            )
        )
    }
}
