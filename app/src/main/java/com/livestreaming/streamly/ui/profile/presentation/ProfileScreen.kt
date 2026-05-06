package com.livestreaming.streamly.ui.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.input.AppSwitch
import com.livestreaming.streamly.config.components.layout.ConfirmationDialog
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.config.theme.ThemeMode
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.AppUtils
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.ui.profile.presentation.component.ProfileHeader
import com.livestreaming.streamly.ui.profile.presentation.component.ProfileInfoCard
import com.livestreaming.streamly.ui.profile.presentation.component.RecentStreamsSection
import com.livestreaming.streamly.ui.profile.presentation.component.StatsRow
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val navController = LocalParentNavController.current
    val uiState by viewModel.uiState.collectAsState()
    val profileData by viewModel.profileData.collectAsState()
    val currentUser = AppUtils.getCurrentUser(context)

    var showSignOutConfirm by remember { mutableStateOf(false) }
    var darkModeChecked by remember { mutableStateOf(AppUtils.isDarkTheme(context)) }

    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { viewModel.observeProfile(it) }
    }

    LaunchedEffect(Unit) {
        handleEvents(viewModel, navController)
    }

    Scaffold { insetPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = insetPadding.calculateTopPadding())
        ) {
            when {
                uiState.isLoading && profileData == null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )

                else -> Column(
                    verticalArrangement = Arrangement.spacedBy(12.sdp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 12.sdp, vertical = 16.sdp),
                ) {
                    Spacer(Modifier.height(10.sdp))

                    ProfileHeader(currentUser)

                    Spacer(Modifier.height(4.sdp))

                    StatsRow(
                        streamCount = profileData?.streamCount ?: 0,
                        totalStreamTimeMs = profileData?.totalStreamTimeMs ?: 0L,
                        avgViewers = profileData?.avgViewers ?: 0,
                    )

                    if (!profileData?.streams.isNullOrEmpty()) {
                        RecentStreamsSection(streams = profileData!!.streams.take(5))
                    }

                    ProfileInfoCard(
                        label = stringResource(R.string.email),
                        value = currentUser?.email ?: "",
                        iconAsset = "email",
                    )

                    ProfileInfoCard(
                        label = stringResource(R.string.theme),
                        value = stringResource(if (darkModeChecked) R.string.dark_mode else R.string.light_mode),
                        iconAsset = "brightness",
                        trailing = {
                            AppSwitch(
                                isChecked = darkModeChecked,
                                onCheckedChange = {
                                    val mode = if (it) ThemeMode.Dark else ThemeMode.Light
                                    viewModel.changeThemeMode(mode)
                                    darkModeChecked = it
                                },
                            )
                        },
                    )

                    AppLoadingButton(
                        fontSize = 12.ssp,
                        trailingIcon = "logout",
                        loading = uiState.isSigningOut,
                        buttonColor = Color.Transparent,
                        label = stringResource(R.string.sign_out),
                        onClick = { showSignOutConfirm = true },
                        shape = RoundedCornerShape(5.sdp),
                        labelColor = MaterialTheme.colorScheme.error,
                        trailingIconColor = MaterialTheme.colorScheme.error,
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.sdp),
                    )
                }
            }
        }

        if (showSignOutConfirm) {
            Dialog(onDismissRequest = { showSignOutConfirm = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.sdp),
                    contentAlignment = Alignment.Center,
                ) {
                    ConfirmationDialog(
                        title = stringResource(R.string.sign_out_title),
                        description = stringResource(R.string.sign_out_msg),
                        positiveButtonLabel = stringResource(R.string.sign_out),
                        negativeButtonLabel = stringResource(R.string.cancel),
                        negativeClick = { showSignOutConfirm = false },
                        positionClick = {
                            showSignOutConfirm = false
                            viewModel.signOut()
                        },
                    )
                }
            }
        }
    }
}

private suspend fun handleEvents(
    viewModel: ProfileViewModel,
    navController: NavController?,
) {
    viewModel.events.collect { event ->
        when (event) {
            is ProfileEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }

            ProfileEvents.SignedOut -> {
                navController?.navigate(Destination.AuthGraph) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
