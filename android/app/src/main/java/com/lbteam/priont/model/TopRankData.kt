package com.lbteam.priont.model

import kotlinx.serialization.Serializable

@Serializable
data class TopRankData(
    val weekly: TopRankSection,
    val monthly: TopRankSection
) {
    companion object {
        fun dummy() = TopRankData(
            weekly = TopRankSection.dummy(),
            monthly = TopRankSection.dummy()
        )
    }
}
