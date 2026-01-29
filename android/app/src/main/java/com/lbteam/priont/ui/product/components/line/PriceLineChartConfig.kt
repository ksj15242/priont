package com.lbteam.priont.ui.product.components.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.common.insets
import java.text.DecimalFormat

object PriceLineChartConfig {

    object AverageLine {
        val avgLineColor = Color(0xFF78909C)
        val avgLabelBackgroundColor = Color.Transparent
        val avgLineThickness = 2.dp

        const val AVG_LABEL = "평균가격"
        val avgLabelColor = Color(0xFF455A64)
        val avgLabelMargin = insets(start = 2.dp)
        val avgLabelPadding = insets(4.dp)
        val avgLabelTextSize = 10.sp
    }

    object Chart {
        val lineColor = Color(0xFF3A86FF)
        val areaColor = arrayOf(
            Color(0xFF3A86FF).copy(alpha = 0.2f),
            Color(0xFF3A86FF).copy(alpha = 0.0f)
        )
        val pointFillColor = Color.White
        val pointStrokeColor = Color(0xFF0077B6)


        val pointStrokeThickness = 2.dp

        val maxTagFormat = DecimalFormat("'최고가 '#,###원")
        val minTagFormat = DecimalFormat("'최저가 '#,###원")
        val priceFormat = DecimalFormat("#,###,###원")
    }
}