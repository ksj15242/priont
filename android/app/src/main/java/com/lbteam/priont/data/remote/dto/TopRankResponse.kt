package com.lbteam.priont.data.remote.dto

import com.lbteam.priont.model.TopRankData
import kotlinx.serialization.Serializable

@Serializable
data class TopRankResponse(
    val data: TopRankData
) {
    companion object {
        fun dummy() = TopRankResponse(
            data = TopRankData.dummy()
        )
    }
}