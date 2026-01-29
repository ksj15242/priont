package com.lbteam.priont.ui.product.components.bar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.text.DecimalFormat

object PriceColumnChartConfig {

    object ColumnBar {
        val activeGradientColor = arrayOf(
            Color(0xFF3A86FF),
            Color(0xFF3A86FF).copy(alpha = 0.5f)
        )

        val gradientColor = arrayOf(
            Color(0xFF3A86FF).copy(alpha = 0.5f),
            Color(0xFF3A86FF).copy(alpha = 0.1f)
        )

        val thickness = 40.dp

        val shape = CorneredShape.rounded(
            topLeftPercent = 20,
            topRightPercent = 20,
            bottomLeftPercent = 0,
            bottomRightPercent = 0
        )

        val labelFormat = DecimalFormat("#,###")
        val labelTextSize = 10.sp
    }
}