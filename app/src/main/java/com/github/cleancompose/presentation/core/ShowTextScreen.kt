package com.github.cleancompose.presentation.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ShowTextScreen(message: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxHeight()
    ) {
        Text(
            text = stringResource(message),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(alignment = Alignment.Center),
            style = MaterialTheme.typography.h4,
        )
    }
}
