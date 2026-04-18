package com.livestreaming.streamly.ui.auth.presentation.login

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
import com.livestreaming.streamly.core.model.AccountType
import com.livestreaming.streamly.ui.auth.presentation.login.component.ForgotPasswordContent
import com.livestreaming.streamly.ui.auth.presentation.login.component.LoginForm
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = LocalParentNavController.current
    val focusManager = LocalFocusManager.current
    val forgotPasswordBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(lifecycleOwner) {
        navController?.let {
            handleEvents(
                navController = it,
                viewModel = viewModel,
                forgotPassSheet = forgotPasswordBottomSheet
            )
        }
    }

    Scaffold { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 15.sdp,
                    end = 15.sdp,
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                )
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                }
        ) {

            Spacer(Modifier.height(50.sdp))

            AuthHeader(subHeading = stringResource(R.string.app_name_caption))

            Spacer(Modifier.height(30.sdp))

            LoginForm(uiState.value) { str, fieldUpdater ->
                viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
            }

            Spacer(modifier = Modifier.height(5.sdp))

            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 11.ssp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { scope.launch { forgotPasswordBottomSheet.show() } }
            )

            Spacer(modifier = Modifier.height(5.sdp))

            AppLoadingButton(
                loading = uiState.value.isSigningWithEmail,
                onClick = { viewModel.login(AccountType.Email) },
                modifier = Modifier.height(35.sdp),
                label = stringResource(R.string.login),
            )

            Spacer(modifier = Modifier.height(20.sdp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.sdp)
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                )

                Text(
                    text = stringResource(R.string.continue_with),
                    color = MaterialTheme.colorScheme.disabledContent,
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.weight(1f),
                    thickness = 0.5.dp,
                )
            }

            Spacer(modifier = Modifier.height(20.sdp))

            AppLoadingButton(
                leadingIcon = "google",
                loading = uiState.value.isSigningWithGoogle,
                buttonColor = Color.Transparent,
                onClick = { viewModel.login(AccountType.Google) },
                label = stringResource(R.string.sign_in_with_google),
                modifier = Modifier
                    .height(35.sdp)
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.sdp)
                    ),
            )

            Spacer(modifier = Modifier.height(5.sdp))

            Text(
                fontSize = 11.ssp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.disabledContent,
                modifier = Modifier.clickable {
                    navController?.navigate(Destination.Register) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                text = buildAnnotatedString {
                    append(stringResource(R.string.dont_have_an_account))
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                        )
                    ) { append(" ${stringResource(R.string.sign_up)}") }
                },
            )
        }

        if (forgotPasswordBottomSheet.isVisible || forgotPasswordBottomSheet.currentValue != SheetValue.Hidden) {
            ModalBottomSheet(
                sheetState = forgotPasswordBottomSheet,
                onDismissRequest = { scope.launch { forgotPasswordBottomSheet.hide() } },
                content = {
                    ForgotPasswordContent(
                        uiState = uiState.value,
                        onSendResetLinkClicked = { viewModel.sendResetPasswordLink() },
                        onFieldChange = { str, fieldUpdater ->
                            viewModel.onFieldChange(value = str, fieldUpdater = fieldUpdater)
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun handleEvents(
    navController: NavController,
    viewModel: LoginViewModel,
    forgotPassSheet: SheetState
) {
    viewModel.events.collect { event ->
        when (event) {
            is LoginEvents.OnError -> {
                SnackbarUtils.show(message = event.error, snackbarType = SnackbarType.Error)
            }

            is LoginEvents.OnLRestLinkSent -> {
                forgotPassSheet.hide()
                SnackbarUtils.show(
                    message = navController.context.getString(R.string.reset_link_sent),
                    snackbarType = SnackbarType.Success
                )
            }

            is LoginEvents.OnLoginSuccess -> {
                navController.navigate(Destination.MainGraph) {
                    popUpTo(Destination.Login) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
