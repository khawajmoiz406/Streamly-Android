package com.livestreaming.streamly.ui.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.ui.home.domain.usecase.ItemLiveStream
import com.livestreaming.streamly.ui.home.presentation.component.HeadingRow
import com.livestreaming.streamly.ui.home.presentation.component.HomeAppBar
import com.livestreaming.streamly.ui.home.presentation.component.NoStreamAvailable
import ir.kaaveh.sdpcompose.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    val liveStreams = viewModel.liveStreams.collectAsState()
    val navController = LocalParentNavController.current

    Scaffold(topBar = { HomeAppBar() }) { insetPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = insetPadding.calculateBottomPadding(),
                    top = insetPadding.calculateTopPadding(),
                    start = 10.sdp,
                    end = 10.sdp
                )
                .fillMaxWidth()
        ) {
            Spacer(Modifier.height(10.sdp))

            HeadingRow(liveStreams.value?.size ?: 0)

            Spacer(Modifier.height(10.sdp))

            when {
                uiState.value.isLoading && liveStreams.value == null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )

                !uiState.value.isLoading && liveStreams.value.isNullOrEmpty() -> NoStreamAvailable {
                    navigate(navController, Destination.StreamBroadcast)
                }

                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.sdp)) {
                    items(liveStreams.value?.size ?: 0) { index ->
                        val item = liveStreams.value?.get(index)
                        ItemLiveStream(item!!) {
                            navigate(navController, Destination.StreamViewer(item.id))
                        }
                    }
                }
            }
        }
    }
}

private fun navigate(navController: NavController?, route: Any) {
    navController?.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}