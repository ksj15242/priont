package com.lbteam.priont.data.remote

import com.google.firebase.storage.FirebaseStorage
import com.lbteam.priont.data.DataConstants
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

@Singleton
class StorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    private companion object {
        private const val VERSION_MAX_SIZE = 1L * 1024 * 1024
        private const val FILE_MAX_SIZE = 5L * 1024 * 1024
    }

    suspend fun getVersion(): String {
        val storageRef = storage.reference.child(DataConstants.FILE_VERSION)
        val bytes = storageRef.getBytes(VERSION_MAX_SIZE).await()
        val jsonString = bytes.toString(Charsets.UTF_8)

        return JSONObject(jsonString).getString("version")
    }

    suspend fun getFilesMetaData(path: String): Map<String, String> = coroutineScope {
        val storageRef = storage.reference.child(path)
        val storageFiles = storageRef.listAll().await()

        storageFiles.items.map { file ->
            async {
                val metadata = file.metadata.await()
                file.name to (metadata.generation ?: "")
            }
        }.awaitAll().toMap()
    }

    suspend fun getStorageFiles(version: String): Map<String, ByteArray> = coroutineScope {
        DataConstants.ALL_FILES.map { fileName ->
            async {
                val fileRef = storage.reference.child("$version/$fileName")
                fileName to fileRef.getBytes(FILE_MAX_SIZE).await()
            }
        }.awaitAll().toMap()
    }
}