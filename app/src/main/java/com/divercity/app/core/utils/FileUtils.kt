package com.divercity.app.core.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.InputStream

/**
 * Created by lucas on 26/11/2018.
 */

object FileUtils {

    fun getMimeType(context: Context, uri: Uri): String? = context.contentResolver.getType(uri)

    fun getFileFromUri(uri: Uri): File = File(uri.path)

    fun getFileExtension(file: File) = file.name.substring(file.name.lastIndexOf("."))

    fun getFileFromContentResolver(context: Context, uri: Uri): File? {
        return context.contentResolver.query(uri, null, null, null, null)?.let { cursor ->

            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val fileName = cursor.getString(nameIndex)
            cursor.close()

            val file = File(context.cacheDir, fileName)

            file.copyInputStreamToFile(context.contentResolver.openInputStream(uri)!!)
            file
        }
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        inputStream.use { input ->
            this.outputStream().use { fileOut ->
                input.copyTo(fileOut)
            }
        }
    }
}