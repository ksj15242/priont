package com.lbteam.priont.model.type

import androidx.annotation.DrawableRes
import com.lbteam.priont.R

enum class CategoryType(val category: String, val label: String, @DrawableRes val icon: Int) {
    CATEGORY_ALL("000", "전체", R.drawable.icon_000_24),
    CATEGORY_100("100", "곡물", R.drawable.icon_100_24),
    CATEGORY_200("200", "채소", R.drawable.icon_200_24),
    CATEGORY_300("300", "특용", R.drawable.icon_300_24),
    CATEGORY_400("400", "과일", R.drawable.icon_400_24),
    CATEGORY_600("600", "수산", R.drawable.icon_600_24);

    companion object {
        fun findLabelById(id: String): String = entries.find { it.category == id }?.label ?: "-"
    }
}