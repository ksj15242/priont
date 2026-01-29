package com.lbteam.priont.data

import com.lbteam.priont.data.local.LocalDataSource
import com.lbteam.priont.data.remote.StorageDataSource
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val storageDataSource: StorageDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun syncData() {
        val storageVersion = storageDataSource.getVersion()
        val localVersion = localDataSource.getVersion()

        if (storageVersion != localVersion) {
            if (isFilesChanged(storageVersion)) {
                syncFiles(storageVersion)
            }

            localDataSource.updateVersion(storageVersion)
        }
    }

    private suspend fun isFilesChanged(version: String): Boolean {
        return storageDataSource.getFilesMetaData(version) != localDataSource.getFilesMetaData()
    }

    private suspend fun syncFiles(version: String) {
        val filesMap = storageDataSource.getStorageFiles(version)
        localDataSource.saveStorageFiles(filesMap)

        val storageFiles = storageDataSource.getFilesMetaData(version)
        localDataSource.updateMetadata(storageFiles)
    }
}