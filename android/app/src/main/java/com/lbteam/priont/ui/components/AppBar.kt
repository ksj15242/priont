package com.lbteam.priont.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreenTopBar(
    categoryLabel: String,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            scrolledContainerColor = Color.White
        ),
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        },
        title = {
            Text(
                text = categoryLabel,
                color = Color.White,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreenTopBar(
    categoryLabel: String,
    onBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            scrolledContainerColor = Color.White
        ),
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        },
        title = {
            Text(
                text = categoryLabel,
                color = Color.White
            )
        },
    )
}