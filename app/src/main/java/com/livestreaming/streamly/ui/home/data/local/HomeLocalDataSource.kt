package com.livestreaming.streamly.ui.home.data.local

import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import javax.inject.Inject

class HomeLocalDataSource @Inject constructor(private val sharedPref: EncryptedSharedPref)