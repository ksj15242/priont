package com.lbteam.priont.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lbteam.priont.R
import com.lbteam.priont.ui.theme.PriontTheme

@Composable
@Preview(showBackground = true)
private fun HomeFooterSectionPreview() {
    PriontTheme {
        HomeFooterSection()
    }
}

@Composable
fun HomeFooterSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.home_footer_msg),
            style = MaterialTheme.typography.labelMedium.copy(
                lineHeight = 18.sp,
                letterSpacing = (-0.5).sp
            ),
            color = Color.Gray.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}