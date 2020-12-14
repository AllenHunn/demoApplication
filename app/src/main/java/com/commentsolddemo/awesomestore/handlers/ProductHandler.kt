package com.commentsolddemo.awesomestore.handlers

import com.commentsolddemo.awesomestore.*
import retrofit2.http.*

interface ProductService {
    @GET("products")
    suspend fun list(
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 50
    ): ProductListResult

    @GET("styles")
    suspend fun listStyles(): StylesResult

    @GET("colors")
    suspend fun listColors(): ColorsResult

    @GET("product/{id}")
    suspend fun get(@Path("id") id: Int): ProductResult

    @POST("product")
    suspend fun create(@Body() product: Product): ProductModifiedResult

    @PUT("product/{id}")
    suspend fun update(@Path("id") id: Int, @Body() product: Product): ProductModifiedResult

    @DELETE("product/{id}")
    suspend fun delete(@Path("id") id: Int): ProductModifiedResult
}

class ProductHandler : BaseHandler(true) {
    private val service = retrofit.create(ProductService::class.java)

    suspend fun list(page: Int = 0, limit: Int = 50): ProductListResult {
        return service.list(page, limit)
    }

    suspend fun get(id: Int): Product {
        val productResult = service.get(id)
        return productResult.product
    }

    suspend fun listColors(): ColorsResult {
        return service.listColors()
    }

    suspend fun listStyles(): StylesResult {
        return service.listStyles()
    }

    suspend fun create(product: Product): Product {
        val result = service.create(product)
        return product.copy(id = result.product_id)
    }

    suspend fun update(product: Product): Unit {
        service.update(product.id!!, product)
    }

    suspend fun delete(product: Product): Unit {
        service.delete(product.id!!)
    }
}