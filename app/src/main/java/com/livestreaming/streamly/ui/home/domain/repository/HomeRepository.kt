package com.livestreaming.streamly.ui.home.domain.repository

interface HomeRepository {
    suspend fun getHome(request: Unit): Result<Unit?>
}