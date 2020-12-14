package com.commentsolddemo.awesomestore

import androidx.paging.PagingSource
import com.commentsolddemo.awesomestore.handlers.ProductHandler

class ProductsPagingSource(private val productHandler: ProductHandler) :
    PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 0
        val nextPage = page + 1
        val limit = params.loadSize

        val response = productHandler.list(page, limit)

        return LoadResult.Page(
            data = response.products,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (response.total > (nextPage * limit)) nextPage else null
        )
    }
}