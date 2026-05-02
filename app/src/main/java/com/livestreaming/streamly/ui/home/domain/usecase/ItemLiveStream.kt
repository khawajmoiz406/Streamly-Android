package com.livestreaming.streamly.ui.home.domain.usecase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.livestream.streamly.R
import com.livestreaming.streamly.config.components.image.SvgImage
import com.livestreaming.streamly.config.components.layout.UserImage
import com.livestreaming.streamly.config.theme.MyApplicationTheme
import com.livestreaming.streamly.config.theme.disabledContainer
import com.livestreaming.streamly.config.theme.disabledContent
import com.livestreaming.streamly.core.model.Stream
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ItemLiveStream(stream: Stream, onClick: () -> Unit) {
    val rounded = RoundedCornerShape(10.sdp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, rounded)
            .border(1.sdp, MaterialTheme.colorScheme.disabledContainer, rounded)
            .padding(10.sdp)
            .clip(rounded)
            .clickable { onClick.invoke() }
    ) {
        UserImage(userName = stream.hostName, profilePicture = stream.hostPhotoUrl, size = 40.sdp)

        Spacer(Modifier.width(10.sdp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(20.sdp)
                        )
                        .padding(horizontal = 6.sdp, vertical = 2.sdp)
                ) {
                    Text(
                        text = stringResource(R.string.live).uppercase(),
                        fontSize = 8.ssp,
                        lineHeight = 8.ssp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Spacer(Modifier.width(5.sdp))

                Text(
                    text = stream.hostName,
                    fontSize = 11.ssp,
                    lineHeight = 11.ssp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.disabledContent,
                )
            }

            Spacer(Modifier.height(5.sdp))

            Text(
                text = stream.title,
                fontSize = 12.ssp,
                lineHeight = 12.ssp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(5.sdp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SvgImage(
                    asset = "eye_open",
                    color = MaterialTheme.colorScheme.disabledContent,
                    modifier = Modifier.size(10.sdp)
                )

                Spacer(Modifier.width(5.sdp))

                Text(
                    text = "${stream.viewerCount} ${stringResource(R.string.watching)}",
                    fontSize = 10.ssp,
                    lineHeight = 10.ssp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.disabledContent,
                )

                Spacer(Modifier.width(5.sdp))

                Text(
                    text = "•",
                    fontSize = 10.ssp,
                    lineHeight = 10.ssp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.disabledContent,
                )

                Spacer(Modifier.width(5.sdp))

                Text(
                    text = "2h ago",
                    fontSize = 10.ssp,
                    lineHeight = 10.ssp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.disabledContent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewItemLiveStream() {
    MyApplicationTheme {
        ItemLiveStream(
            Stream(
                hostName = "Super Admin",
                title = "Building a SaaS in 24 hours",
                viewerCount = 10
            )
        ) {}
    }
}