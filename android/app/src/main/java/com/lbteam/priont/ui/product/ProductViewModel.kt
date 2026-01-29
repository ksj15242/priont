package com.lbteam.priont.ui.product

import android.icu.text.DecimalFormat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbteam.priont.data.AppRepository
import com.lbteam.priont.model.Region
import com.lbteam.priont.model.type.PeriodType
import com.lbteam.priont.model.type.RegionType
import com.lbteam.priont.ui.product.components.bar.PriceColumnChartData
import com.lbteam.priont.ui.product.components.line.PriceLineChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: AppRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _productUiState = MutableStateFlow(ProductUiState())
    val productUiState: StateFlow<ProductUiState> = _productUiState.asStateFlow()

    init {
        loadProductDetail()
    }

    fun onLineChartTabSelected(index: Int) {
        _productUiState.update {
            it.copy(
                lineChartState = it.lineChartState.copy(
                    selectedTabIndex = index
                )
            )
        }
    }

    private fun getLineChartYaxisRange(
        yValues: List<Int>,
        paddingRatio: Double = 0.2,
    ): Pair<Double, Double> {
        val minValue = yValues.min()
        val maxValue = yValues.max()

        return if (minValue == maxValue) {
            val padding = minValue * paddingRatio
            (minValue - padding) to (minValue + padding)
        } else {
            val span = maxValue - minValue
            (minValue - span * paddingRatio) to (maxValue + span * paddingRatio)
        }
    }

    private fun getLineChartData(
        dates: List<String>,
        prices: List<Int?>
    ): Map<PeriodType, PriceLineChartData> {
        return PeriodType.entries.associateWith { periodType ->

            val takeCount = when (periodType) {
                PeriodType.DAY_7 -> 7
                PeriodType.DAY_30 -> 30
            }

            val slicedDates = dates.takeLast(takeCount)
            val slicedPrices = prices.takeLast(takeCount)

            val filteredData = slicedPrices.mapIndexedNotNull { index, price ->
                price?.let { index to it }
            }

            val xValues = filteredData.map { it.first }
            val yValues = filteredData.map { it.second }

            val average = if (yValues.isEmpty()) 0.0 else yValues.average()
            val minIndex = filteredData.minByOrNull { it.second }?.first?.toDouble() ?: 0.0
            val maxIndex = filteredData.maxByOrNull { it.second }?.first?.toDouble() ?: 0.0

            val xAxisRange = Pair(0.0, (takeCount - 1).toDouble())
            val yAxisRange = getLineChartYaxisRange(yValues)

            PriceLineChartData(
                dates = slicedDates,
                prices = slicedPrices,
                xValues = xValues,
                yValues = yValues,
                minIndex = minIndex,
                maxIndex = maxIndex,
                xAxisRange = xAxisRange,
                yAxisRange = yAxisRange,
                average = average,
            )
        }
    }

    private fun getColumnChartData(
        columnData: Map<RegionType, Int>,
        selectedRegion: RegionType
    ): PriceColumnChartData {

        val xValues = columnData.keys.sortedBy { it != selectedRegion }
        val yValues = xValues.map { columnData[it] ?: 0 }

        val yAxisRange = Pair(0.0, (yValues.maxOrNull() ?: 0).toDouble())

        return PriceColumnChartData(
            xValues = xValues,
            yValues = yValues,
            yAxisRange = yAxisRange
        )
    }

    private fun loadProductDetail() {
        viewModelScope.launch {
            runCatching {
                val product = repository.getProductById(productId)
                    ?: throw NoSuchElementException("상품 정보를 찾을 수 없습니다.")

                val region = product.regions.find { it.name == RegionType.All }
                    ?: throw IllegalStateException("전국 데이터가 누락되었습니다.")

                val initColumnChartData = RegionType.entries.associateWith { type ->
                    product.regions.find { it.name == type }?.prices?.lastOrNull() ?: 0
                }

                val columnChartData = getColumnChartData(initColumnChartData, region.name)

                val baseState = _productUiState.value.copy(
                    productInfo = ProductInfo(
                        category = product.category,
                        name = product.name,
                        unit = product.unit,
                        variety = product.variety,
                        grade = product.grade
                    ),
                    regionState = RegionState(
                        selectedType = region.name,
                        allRegions = product.regions,
                        selectedRegion = region,
                    ),
                    columnChartState = ColumnChartState(
                        originData = initColumnChartData,
                        data = columnChartData
                    ),
                )

                val lastPrice = region.prices.lastOrNull() ?: 0
                if (lastPrice <= 0) {
                    baseState.copy(
                        status = ProductStatus.Empty
                    )
                } else {
                    val lineChartData = getLineChartData(region.dates, region.prices)
                    val avg7dPrice = lineChartData.getValue(PeriodType.DAY_7).average.roundToInt()
                    val avg30dPrice = lineChartData.getValue(PeriodType.DAY_30).average.roundToInt()

                    baseState.copy(
                        status = ProductStatus.Success,
                        priceInfo = PriceInfo(
                            price = lastPrice,
                            avg7d = avg7dPrice,
                            avg30d = avg30dPrice
                        ),
                        lineChartState = baseState.lineChartState.copy(
                            dataMap = lineChartData
                        ),
                    )
                }
            }.onSuccess {
                _productUiState.value = it
            }.onFailure {
                _productUiState.update {
                    it.copy(
                        status = ProductStatus.Error
                    )
                }
            }
        }
    }

    fun selectRegion(region: RegionType) {
        _productUiState.update { uiState ->
            val selectedRegion = uiState.regionState.allRegions.find { it.name == region }
            val columnChartData = getColumnChartData(uiState.columnChartState.originData, region)
            val lastPrice = selectedRegion?.prices?.lastOrNull() ?: 0

            if (selectedRegion == null || lastPrice <= 0) {
                return@update uiState.copy(
                    status = ProductStatus.Empty,
                    regionState = uiState.regionState.copy(
                        selectedType = region,
                        selectedRegion = Region(region, emptyList(), emptyList())
                    ),
                    columnChartState = uiState.columnChartState.copy(
                        data = columnChartData
                    ),
                    priceInfo = PriceInfo()
                )
            }

            val lineChartData = getLineChartData(selectedRegion.dates, selectedRegion.prices)
            val avg7dPrice = lineChartData.getValue(PeriodType.DAY_7).average.roundToInt()
            val avg30dPrice = lineChartData.getValue(PeriodType.DAY_30).average.roundToInt()

            uiState.copy(
                status = ProductStatus.Success,
                regionState = uiState.regionState.copy(
                    selectedType = selectedRegion.name,
                    selectedRegion = selectedRegion
                ),
                columnChartState = uiState.columnChartState.copy(
                    data = columnChartData
                ),
                lineChartState = uiState.lineChartState.copy(
                    dataMap = lineChartData
                ),
                priceInfo = PriceInfo(
                    price = lastPrice,
                    avg7d = avg7dPrice,
                    avg30d = avg30dPrice
                ),
            )
        }
    }

}

fun Int.toPriceSummary(periodLabel: String): String {
    val diff = DecimalFormat("#,###").format(abs(this))
    return when {
        this == 0 -> "$periodLabel 평균가와 동일해요"
        this > 0 -> "$periodLabel 평균보다 ${diff}원 비싸요"
        else -> "$periodLabel 평균보다 ${diff}원 저렴해요"
    }
}

fun Int.toPrice(): String {
    if (this <= 0) return "-원"
    return DecimalFormat("#,###원").format(this)
}