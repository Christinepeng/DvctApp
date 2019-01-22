package com.divercity.android.helpers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Base64
import com.divercity.android.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by lucas on 06/03/2018.
 */
class TakePictureHelper @Inject
constructor() {

    companion object {
        val REQUEST_TAKE_PHOTO = 203
        val PERMISSION_CAMERA_REQUEST_CODE = 100
    }

    lateinit var activity : FragmentActivity
    var currentPhotoPath : String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun deleteCurrentFile(file : String?) {
        activity.deleteFile(file)
    }

    fun checkCameraPermissionAndTakePicture() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        } else {
            requestCameraPermission()
        }
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA_REQUEST_CODE)
    }

    fun decodeSampledBitmapFromResource(photoPath : String, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoPath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(photoPath, options)
    }

    fun getStringBase64(reqWidth: Int, reqHeight: Int): String {
        val baos = ByteArrayOutputStream()
        getBitmap(reqWidth, reqHeight)?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getBitmap(reqWidth: Int, reqHeight: Int): Bitmap? {
        currentPhotoPath?.let {
            val bitmap = decodeSampledBitmapFromResource(it, reqWidth, reqHeight)
            val rotatedBitmap = getRotatedBitmap(bitmap, it)
            if (bitmap != rotatedBitmap)
                copyRotatedImageToFile(rotatedBitmap, it)
            return rotatedBitmap
        }
        return null
    }

    private fun getRotatedBitmap(bitmap: Bitmap, photoPath: String): Bitmap {
        var rotatedBitmap = bitmap
        try {
            val ei = ExifInterface(photoPath)
            val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)

                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)

                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)

                ExifInterface.ORIENTATION_NORMAL -> {
                }

                else -> rotatedBitmap = bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0,
                source.width, source.height,
                matrix, true)
    }

    private fun copyRotatedImageToFile(bitmap: Bitmap, photoPath: String) {
        try {
            val file = File(photoPath)
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            try {
                val photoFile = createImageFile()
                val photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID.plus(".provider"),
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            } catch (ex: IOException) {

            }
        }
    }
}