package com.lbteam.priont.model

import kotlinx.serialization.Serializable

@Serializable
data class TopRankItem(
    val id: String,
    val category: String,
    val name: String,
    val variety: String,
    val unit: String,
    val grade: String,
    val price: Int,
    val rate: String
) {
    companion object {
        fun dummyIncrease() = TopRankItem(
            id = "2002470004",
            category = "채소류",
            name = "생강",
            variety = "국산",
            unit = "1kg",
            grade = "상품",
            price = 11523,
            rate = "12.44"
        )

        fun dummyDecrease() = TopRankItem(
            id = "6006190521",
            category = "수산물",
            name = "물오징어",
            variety = "원양(냉동)",
            unit = "1마리",
            grade = "中",
            price = 4438,
            rate = "-7.93"
        )
    }
}
