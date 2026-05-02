package com.livestreaming.streamly.ui.watch.data.local

import com.livestreaming.streamly.core.pref.EncryptedSharedPref
import javax.inject.Inject

class WatchLocalDataSource @Inject constructor(private val sharedPref: EncryptedSharedPref)