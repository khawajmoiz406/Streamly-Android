package com.livestreaming.streamly.ui.profile.domain.usecase

import com.livestreaming.streamly.base.FlowUseCase
import com.livestreaming.streamly.core.model.ProfileData
import com.livestreaming.streamly.ui.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProfileDataUseCase @Inject constructor(private val repo: ProfileRepository) :
    FlowUseCase<ProfileData, String> {
    override fun invoke(params: String): Flow<ProfileData> {
        return repo.observeProfileData(params)
    }
}