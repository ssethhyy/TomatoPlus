package com.ssethhyy.tomatoplus.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssethhyy.tomatoplus.R
import com.ssethhyy.tomatoplus.ui.theme.TomatoPlusTheme

@Preview(showBackground = false)
@Composable
fun LogoPreview() {
    TomatoPlusTheme(dynamicColor = false) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(512.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(512.dp)
            )
        }
    }
}
