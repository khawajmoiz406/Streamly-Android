package com.livestreaming.streamly.base

import kotlinx.coroutines.flow.Flow

sealed interface BaseUseCase<out T, in P>

// Single shot operation
interface SuspendUseCase<out T, in P> : BaseUseCase<T, P> {
    suspend operator fun invoke(params: P): Result<T>
}

// Stream operation
interface FlowUseCase<out T, in P> : BaseUseCase<T, P> {
    operator fun invoke(params: P): Flow<T>
}