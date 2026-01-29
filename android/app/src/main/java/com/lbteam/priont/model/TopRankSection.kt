package com.lbteam.priont.model

import kotlinx.serialization.Serializable

@Serializable
data class TopRankSection(
    val increase: List<TopRankItem>,
    val decrease: List<TopRankItem>
) {

    val pages get() = listOf(increase, decrease)
    val pageCount get() = pages.size

    companion object {
        fun dummy() = TopRankSection(
            increase = List(3) { TopRankItem.dummyIncrease() },
            decrease = List(3) { TopRankItem.dummyDecrease() }
        )
    }
}