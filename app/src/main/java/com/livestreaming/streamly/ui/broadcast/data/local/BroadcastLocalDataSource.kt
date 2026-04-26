package com.livestreaming.streamly.ui.broadcast.data.local

import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import javax.inject.Inject

class BroadcastLocalDataSource @Inject constructor(private val sharedPref: EncryptedSharedPref)