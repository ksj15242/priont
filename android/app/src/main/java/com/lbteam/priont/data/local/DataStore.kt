package com.lbteam.priont.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val DATASTORE_NAME = "version"

val Context.dataStore by preferencesDataStore(
    name = DATASTORE_NAME
)