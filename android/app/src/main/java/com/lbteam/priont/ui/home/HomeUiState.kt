package com.lbteam.priont.ui.home

import com.lbteam.priont.model.TopRankData
import com.lbteam.priont.model.TopRankSection

data class HomeUiState(
    val topRankData: TopRankData,
    val selectedRankingTabIndex: Int = 0
) {
    val selectedTopRankSection: TopRankSection
        get() = if (selectedRankingTabIndex == 0) topRankData.weekly else topRankData.monthly

    companion object {
        fun dummy() = HomeUiState(
            topRankData = TopRankData.dummy(),
            selectedRankingTabIndex = 0
        )
    }
}