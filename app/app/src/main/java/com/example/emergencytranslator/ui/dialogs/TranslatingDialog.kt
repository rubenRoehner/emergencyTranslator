package com.example.emergencytranslator.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.emergencytranslator.R

@Composable
fun TranslatingDialog(present: Boolean) {
    if (present) {
        FullScreenDialog {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.undraw_around_the_world),
                    contentDescription = "",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                        .padding(LocalConfiguration.current.screenWidthDp.dp / 10)
                        .size(LocalConfiguration.current.screenWidthDp.dp / 2)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = stringResource(id = R.string.dialogs_translating),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    LoadingAnimation(
                        modifier = Modifier.padding(bottom = 4.dp),
                        circleSize = 5.dp,
                        spaceBetween = 2.dp,
                        travelDistance = 4.dp
                    )
                }
            }
        }
    }
}