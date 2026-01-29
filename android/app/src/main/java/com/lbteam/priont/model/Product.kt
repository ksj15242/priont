package com.lbteam.priont.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val category: String,
    val name: String,
    val variety: String,
    val unit: String,
    val grade: String,
    val regions: List<Region>
) {
    companion object {
        fun dummy(id: String = "1001110104") = Product(
            id = id,
            category = "식량작물",
            name = "쌀",
            variety = "20kg",
            unit = "20kg",
            grade = "상품",
            regions = List(24) { Region.dummy() }
        )
    }
}
