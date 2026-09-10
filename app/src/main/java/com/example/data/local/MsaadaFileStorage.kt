package com.example.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File

object MsaadaFileStorage {

    fun savePdf(context: Context, source: File, fileName: String): Uri {
        val resolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
                put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/MSAADA")
                put(MediaStore.Files.FileColumns.IS_PENDING, 1)
            }

            val uri = resolver.insert(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            ) ?: error("Imeshindikana kuunda PDF")

            try {
                resolver.openOutputStream(uri)!!.use { output ->
                    source.inputStream().use { input -> input.copyTo(output) }
                }

                resolver.update(
                    uri,
                    ContentValues().apply {
                        put(MediaStore.Files.FileColumns.IS_PENDING, 0)
                    },
                    null,
                    null
                )

                return uri
            } catch (e: Exception) {
                resolver.delete(uri, null, null)
                throw e
            }
        }

        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "MSAADA"
        )
        folder.mkdirs()

        val destination = File(folder, fileName)
        source.copyTo(destination, overwrite = true)

        return Uri.fromFile(destination)
    }

    fun saveImage(context: Context, source: File, fileName: String): Uri {
        val resolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MSAADA/Passport")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val uri = resolver.insert(
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            ) ?: error("Imeshindikana kuunda picha")

            try {
                resolver.openOutputStream(uri)!!.use { output ->
                    source.inputStream().use { input -> input.copyTo(output) }
                }

                resolver.update(
                    uri,
                    ContentValues().apply {
                        put(MediaStore.Images.Media.IS_PENDING, 0)
                    },
                    null,
                    null
                )

                return uri
            } catch (e: Exception) {
                resolver.delete(uri, null, null)
                throw e
            }
        }

        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MSAADA/Passport"
        )
        folder.mkdirs()

        val destination = File(folder, fileName)
        source.copyTo(destination, overwrite = true)

        return Uri.fromFile(destination)
    }
}
