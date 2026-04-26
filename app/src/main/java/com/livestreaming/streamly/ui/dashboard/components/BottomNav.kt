package com.livestreaming.streamly.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.Constants.BOTTOM_NAV_ITEMS
import com.livestreaming.streamly.core.model.NavigationItem
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BottomNav(navController: NavController) {
    val parentNavController = LocalParentNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .navigationBarsPadding()
                .height(integerResource(R.integer.bottom_nav_height).sdp)
        ) {
            BOTTOM_NAV_ITEMS.mapIndexed { index, item ->
                val centerItem = index == 1
                val nav = if (centerItem) parentNavController else navController

                BottomNavItem(
                    item = item,
                    bigItem = centerItem,
                    destination = destination,
                    onClick = { onItemClicked(item.route, nav) },
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    item: NavigationItem,
    bigItem: Boolean,
    destination: NavDestination?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected = destination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
    val color =
        if (!isSelected) MaterialTheme.colorScheme.disabledContent else MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable { onClick.invoke() }
    ) {
        if (bigItem)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .size(45.sdp)
            ) {
                SvgImage(
                    asset = item.icon,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(25.sdp),
                )
            }
        else {
            SvgImage(
                asset = item.icon,
                color = color,
                modifier = Modifier.size(16.sdp),
            )

            Spacer(Modifier.height(3.sdp))

            Text(
                text = stringResource(item.name),
                color = color,
                fontSize = 11.ssp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun onItemClicked(route: Any, navController: NavController?) {
    navController?.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    val navController = rememberNavController()
    MyApplicationTheme {
        BottomNav(navController)
    }
}