package com.lbteam.priont.ui.search.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.lbteam.priont.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(true) }

    BackHandler(enabled = expanded) {
        onBack()
    }

    LaunchedEffect(Unit) {
        if (query.isEmpty()) {
            focusRequester.requestFocus()
        }
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    keyboard?.hide()
                    focusManager.clearFocus()
                },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                expanded = expanded,
                onExpandedChange = {},
                placeholder = { Text(text = stringResource(R.string.search_searchbar_hint)) },
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            if (!it) onBack()
        },
        modifier = modifier.fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color(0xFF1B4332)
        ),
    ) {
        content()
    }
}