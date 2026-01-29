package com.lbteam.priont.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lbteam.priont.data.DataConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

@Singleton
class LocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val versionKey = stringPreferencesKey("version")

    private fun metaDataKey(file: String) = stringPreferencesKey(file)

    suspend fun getVersion(): String =
        context.dataStore.data
            .map { it[versionKey] ?: "" }
            .first()

    suspend fun getFilesMetaData(): Map<String, String> =
        context.dataStore.data
            .map { prefs ->
                DataConstants.ALL_FILES.associateWith { file ->
                    prefs[metaDataKey(file)] ?: ""
                }
            }.first()

    suspend fun updateVersion(newVersion: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { it[versionKey] = newVersion }
    }

    suspend fun updateMetadata(storageMetadata: Map<String, String>) = withContext(Dispatchers.IO) {
        context.dataStore.edit { prefs ->
            storageMetadata.forEach { (file, value) ->
                prefs[metaDataKey(file)] = value
            }
        }
    }

    suspend fun saveStorageFiles(files: Map<String, ByteArray>) = withContext(Dispatchers.IO) {
        val tempFiles = mutableListOf<File>()

        runCatching {
            files.forEach { (name, bytes) ->
                val tempFile = File(context.filesDir, "$name.tmp").apply {
                    parentFile?.mkdirs()
                }

                tempFile.outputStream().use { fos ->
                    fos.write(bytes)
                    fos.fd.sync()
                }

                tempFiles.add(tempFile)
            }


            files.keys.forEach { name ->
                val tempFile = File(context.filesDir, "$name.tmp")
                val destFile = File(context.filesDir, name)

                if (destFile.exists() && !destFile.delete()) {
                    throw IOException("기존 파일(${name})을 삭제할 수 없습니다.")
                }

                if (!tempFile.renameTo(destFile)) {
                    tempFile.copyTo(destFile, overwrite = true)
                    tempFile.delete()
                }
            }
        }.onFailure { e ->
            tempFiles.forEach { file ->
                if (file.exists()) file.delete()
            }
            throw e
        }

    }
}