package com.lbteam.priont.data.remote.dto

import com.lbteam.priont.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val data: List<Product>
)
