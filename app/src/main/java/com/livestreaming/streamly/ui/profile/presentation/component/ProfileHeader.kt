package com.livestreaming.streamly.ui.profile.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.livestreaming.streamly.config.components.layout.UserImage
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.core.model.User
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ProfileHeader(user: User?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserImage(
            userName = user?.name ?: "",
            profilePicture = user?.avatar,
            size = 60.sdp,
        )

        Spacer(Modifier.height(8.sdp))

        Text(
            text = user?.name ?: "",
            fontSize = 16.ssp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(2.sdp))

        Text(
            text = user?.email ?: "",
            fontSize = 11.ssp,
            color = MaterialTheme.colorScheme.disabledContent,
        )
    }
}
