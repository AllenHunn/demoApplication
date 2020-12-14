package com.commentsolddemo.awesomestore

import com.commentsolddemo.awesomestore.handlers.ProductHandler

object Constants {
    const val BASE_URL = "https://cscodetest.herokuapp.com/api/"

    suspend fun init() {
        val productHandler = ProductHandler()
        val colorsResult = productHandler.listColors()
        val stylesResult = productHandler.listStyles()

        colors = colorsResult.colors
        styles = stylesResult.styles
    }

    lateinit var styles: Array<String>
    lateinit var colors: Array<String>
}