package com.habib.apistester

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.annotation.StringRes

fun Uri.getFileName(): String {
	var result: String? = null
	if (this.scheme == "content") {
		AppClass.app.contentResolver.query(
			this,
			null, null, null, null
		).use { cursor ->
			if (cursor != null && cursor.moveToFirst()) {
				val index =
					cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
				if (index >= 0)
					result = cursor.getString(index)
			}
		}
	}
	if (result == null) {
		result = this.path
		val cut = result?.lastIndexOf('/')
		if (cut != -1) {
			result = result?.substring(cut!! + 1)
		}
	}
	return result ?: "file"
}

fun Context.isInternetAvailable(): Boolean {
	val connectivityManager =
		getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val activeNetwork = connectivityManager.activeNetwork ?: return false
	val capabilities =
		connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

	return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

fun Context.showToast(@StringRes strRes: Int) {
	Toast.makeText(
		this,
		this.getString(strRes),
		Toast.LENGTH_LONG
	).show()

}
