package com.livestreaming.streamly.config.utils

import com.livestream.streamly.R
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.core.model.NavigationItem

object Constants {
    const val SPLASH_DELAY = 1500L //In Milliseconds
    const val AGORA_APP_ID = "363b0987a94541f3a315afd20adb5fa2"

    val BOTTOM_NAV_ITEMS = listOf(
        NavigationItem(
            name = R.string.home,
            route = Destination.Home,
            icon = "home",
        ),
        NavigationItem(
            name = R.string.live,
            route = Destination.StreamBroadcast,
            icon = "live",
        ),
        NavigationItem(
            name = R.string.profile,
            route = Destination.Profile,
            icon = "user",
        ),
    )
}