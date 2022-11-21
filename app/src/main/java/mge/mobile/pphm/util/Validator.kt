package mge.mobile.pphm.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
class Validator {

    companion object {

        const val READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val CALL_PHONE_PERMISSION = Manifest.permission.CALL_PHONE

        fun checkPermission(permission: String, context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }

        private const val RANDOM_STRING_ID_MIN_LENGTH = 1
        private const val RANDOM_STRING_ID_MAX_LENGTH = 10
        private val RANDOM_STRING_CHAR_POOL: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun genId(): String {
            return (RANDOM_STRING_ID_MIN_LENGTH..RANDOM_STRING_ID_MAX_LENGTH)
                .map { kotlin.random.Random.nextInt(0, RANDOM_STRING_CHAR_POOL.size) }
                .map(RANDOM_STRING_CHAR_POOL::get)
                .joinToString("")
        }
    }

}