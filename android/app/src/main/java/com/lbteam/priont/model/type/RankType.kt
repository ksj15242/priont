package com.lbteam.priont.model.type

import androidx.compose.ui.graphics.Color

enum class RankType {
    INCREASE,
    DECREASE
}

val RankType.title: String
    get() = when (this) {
        RankType.INCREASE -> "가격 상승 TOP3"
        RankType.DECREASE -> "가격 하락 TOP3"
    }

val RankType.color: Color
    get() = when (this) {
        RankType.INCREASE -> Color.Red
        RankType.DECREASE -> Color.Blue
    }