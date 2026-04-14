package com.livestreaming.streamly.base

abstract class BaseRequest {
    abstract fun toMap(): HashMap<String, Any>
}