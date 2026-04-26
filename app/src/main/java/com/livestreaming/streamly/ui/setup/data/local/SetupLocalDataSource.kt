package com.livestreaming.streamly.ui.setup.data.local

import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import javax.inject.Inject

class SetupLocalDataSource @Inject constructor(private val sharedPref: EncryptedSharedPref)