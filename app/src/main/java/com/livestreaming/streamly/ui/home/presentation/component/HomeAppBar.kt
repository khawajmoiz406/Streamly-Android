package com.livestreaming.streamly.ui.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.components.layout.UserImage
import com.livestreaming.streamly.core.model.User
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(user: User, onUserClicked: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.sdp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.sdp)
                        )
                ) {
                    SvgImage(
                        asset = "live",
                        modifier = Modifier.size(15.sdp)
                    )
                }

                Spacer(modifier = Modifier.width(8.sdp))

                Text(
                    fontSize = 15.ssp,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onUserClicked.invoke() },
            ) {
                UserImage(user.name ?: "", user.avatar, 28.sdp)
            }

            Spacer(modifier = Modifier.width(8.sdp))
        }
    )
}