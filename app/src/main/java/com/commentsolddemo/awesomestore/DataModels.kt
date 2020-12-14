package com.commentsolddemo.awesomestore

data class AuthenticationResult(
    val error: Int,
    val token: String
)

data class ProductListResult(
    val count: Int,
    val products: List<Product>,
    val total: Int
)

data class ProductResult(val product: Product)

data class ProductModifiedResult(
    val message: String,
    val product_id: Int
)

data class StylesResult(val styles: Array<String>)

data class ColorsResult(val colors: Array<String>)

data class Product(
    val id: Int?,
    val product_name: String?,
    val description: String?,
    val style: String?,
    val brand: String?,
    val created_at: String?,
    val updated_at: String?,
    val url: String?,
    val product_type: String?,
    val shipping_price: Int?,
    val note: String?,
    val admin_id: Int?
)


data class Inventory(
    val id: Int,
    val product_id: Int,
    val quantity: Int,
    val color: String,
    val size: String,
    val weight: Int,
    val price_cents: Int,
    val sale_price_cents: Int,
    val cost_cents: Int,
    val sku: String,
    val length: Int,
    val width: Int,
    val height: Int,
    val note: String
)