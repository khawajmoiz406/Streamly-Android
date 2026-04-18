package com.livestreaming.streamly.ui.auth.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.button.AppLoadingButton
import com.livestreaming.streamly.config.components.layout.AuthHeader
import com.livestreaming.streamly.config.navigation.Destination
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.AppCompositionLocals.LocalParentNavController
import com.livestreaming.streamly.config.utils.SnackbarType
import com.livestreaming.streamly.config.utils.SnackbarUtils
import com.livestreaming.streamly.ui.auth.presentation.register.component.ImagePicker
import com.livestreaming.streamly.ui.auth.presentation.register.component.RegisterForm
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: RegisterViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = LocalParentNavController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(lifecycleOwner) {
        navController?.let { handleEvents(it, viewModel) }
    }

    Scaffold { padding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                .padding(
                    start = 15.sdp,
                    end = 15.sdp,
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                )
        ) {

            item { Spacer(Modifier.height(30.sdp)) }

            item {
                AuthHeader(
                    heading = stringResource(R.string.create_your_account),
                    subHeading = stringResource(R.string.create_account_msg)
                )
            }

            item { Spacer(Modifier.height(20.sdp)) }

            item {
                ImagePicker(uiState.value.profilePicture) {
                    viewModel.onPictureChanged(it)
                }
            }

            item { Spacer(Modifier.height(20.sdp)) }

            item {
                RegisterForm(uiState.value) { str, fieldUpdater ->
                    viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
                }
            }

            item { Spacer(Modifier.height(20.sdp)) }

            item {
                AppLoadingButton(
                    loading = uiState.value.isLoading,
                    onClick = { viewModel.register() },
                    modifier = Modifier.height(35.sdp),
                    label = stringResource(R.string.create_account),
                )
            }

            item { Spacer(Modifier.height(5.sdp)) }

            item {
                Text(
                    fontSize = 11.ssp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.clickable { navController?.popBackStack() },
                    text = buildAnnotatedString {
                        append(stringResource(R.string.already_have_an_account))
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                            )
                        ) { append(" ${stringResource(R.string.login)}") }
                    },
                )
            }

            item { Spacer(Modifier.height(10.sdp)) }
        }
    }
}

private suspend fun handleEvents(navController: NavController, viewModel: RegisterViewModel) {
    viewModel.events.collect { event ->
        when (event) {
            is RegisterEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }

            is RegisterEvents.OnRegisterSuccess -> {
                navController.navigate(Destination.MainGraph) {
                    popUpTo(Destination.Login) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
