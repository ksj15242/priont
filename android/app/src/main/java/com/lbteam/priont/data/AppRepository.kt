package com.lbteam.priont.data

import android.content.Context
import com.lbteam.priont.data.remote.dto.ProductResponse
import com.lbteam.priont.data.remote.dto.TopRankResponse
import com.lbteam.priont.model.Product
import com.lbteam.priont.model.TopRankData
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import java.io.File

@Singleton
class AppRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedProducts: List<Product> = emptyList()
    private var cachedTopRanks: TopRankData? = null

    fun getProducts(): List<Product> = cachedProducts

    fun getTopRanks(): TopRankData = cachedTopRanks!!

    fun loadData() {
        loadProducts()
        loadTopRanks()
    }

    fun hasData(): Boolean {
        return cachedProducts.isNotEmpty() && cachedTopRanks != null
    }

    private fun loadProducts() {
        cachedProducts = DataConstants.PRODUCT_FILES.flatMap { fileName ->
            val file = File(context.filesDir, fileName)
            val jsonString = file.readText()
            val response: ProductResponse = json.decodeFromString(jsonString)

            response.data
        }
    }

    private fun loadTopRanks() {
        val file = File(context.filesDir, DataConstants.FILE_RANK)
        val jsonString = file.readText()
        val response: TopRankResponse = json.decodeFromString(jsonString)
        cachedTopRanks = response.data
    }

    fun getProductsByCategory(category: String): List<Product> {
        return if (category == "000") {
            cachedProducts
        } else {
            cachedProducts.filter { it.id.take(3) == category }
        }
    }

    fun getProductById(productId: String): Product? {
        return cachedProducts.firstOrNull { it.id == productId }
    }
}