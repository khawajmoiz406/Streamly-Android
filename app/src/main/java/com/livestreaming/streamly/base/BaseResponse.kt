package com.ecommerce.shoppy.base


open class BaseResponse(
    val total: Int? = null,
    val skip: Int? = null,
    val limit: Int? = null,
)