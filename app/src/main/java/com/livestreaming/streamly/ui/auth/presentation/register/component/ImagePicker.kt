package com.livestreaming.streamly.ui.auth.presentation.register.component

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.config.utils.PermissionUtils
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePicker(selectedImage: Uri?, onImagePicked: (Uri) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagePickerBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) pendingCameraUri?.let { onImagePicked(it) } }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onImagePicked(it) } }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { input ->
            PermissionUtils.handlePermissionResult(
                result = input,
                onDenied = { },
                onPermanentlyDenied = { },
                activity = context as Activity,
                onGranted = {
                    val uri = createImageUri(context)
                    pendingCameraUri = uri
                    pendingCameraUri?.let { cameraLauncher.launch(it) }
                },
            )
        }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(90.sdp)
                    .background(MaterialTheme.colorScheme.disabledContainer, CircleShape)
                    .clip(CircleShape)
                    .clickable { scope.launch { imagePickerBottomSheet.show() } },
            ) {
                selectedImage?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: SvgImage(
                    asset = "user",
                    modifier = Modifier.size(45.sdp),
                    color = MaterialTheme.colorScheme.disabledContent,
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset { IntOffset(-10, -10) }
                    .size(22.sdp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
                    .clip(CircleShape)
                    .clickable { scope.launch { imagePickerBottomSheet.show() } },
            ) {
                SvgImage(
                    asset = "edit",
                    modifier = Modifier.size(12.sdp),
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(5.sdp))

        Text(
            text = stringResource(R.string.tap_to_upload_profile_picture),
            color = MaterialTheme.colorScheme.disabledContent,
            fontSize = 11.ssp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }

    if (imagePickerBottomSheet.isVisible || imagePickerBottomSheet.currentValue != SheetValue.Hidden) {
        ModalBottomSheet(
            sheetState = imagePickerBottomSheet,
            onDismissRequest = { scope.launch { imagePickerBottomSheet.hide() } },
            content = {
                ImageSelectionContent(
                    onGalleryClicked = {
                        scope.launch { imagePickerBottomSheet.hide() }
                        galleryLauncher.launch("image/*")
                    },
                    onCameraClicked = {
                        scope.launch { imagePickerBottomSheet.hide() }
                        PermissionUtils.requestPermissions(
                            PermissionUtils.corePermissions + PermissionUtils.legacyStoragePermissions,
                            cameraPermissionLauncher
                        )
                    }
                )
            }
        )
    }
}

private fun createImageUri(context: Context): Uri {
    val file = File(context.externalCacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

@Preview
@Composable
private fun PreviewImagePicker() {
    MyApplicationTheme {
        ImagePicker(null) { _ -> }
    }
}