package com.habib.apistester.networking

import android.net.Uri
import com.habib.apistester.AppClass
import com.habib.apistester.getFileName
import java.io.DataOutputStream
import java.net.HttpURLConnection

sealed class PostAPINetworkingRequestImplementation(
	url: String,
	headers: Map<String, String>,
	requestBody: String,
	queryParams: Map<String, String>
) : APINetworkingRequestImplementation(url, headers, requestBody, queryParams) {
	override val requestMethod: HTTPMethod = HTTPMethod.POST

	class JsonPostAPINetworkingRequestImplementation(
		url: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	) : PostAPINetworkingRequestImplementation(url, headers, requestBody, queryParams) {
		override fun makeAPICall(): ResponseModel {
			var connection: HttpURLConnection? = null
			return try {
				connection = setupConnection()
				connection.doOutput = true
				val response = readResponse(connection)
				response
			} catch (ex: Exception) {
				ExceptionResponseModel(ex)
			} finally {
				cleanup(connection)
			}
		}
	}

	class MultipartPostAPINetworkingRequestImplementation(
		url: String,
		headers: Map<String, String>,
		private val fileUriString: String,
		requestBody: String,
		queryParams: Map<String, String>

	) : PostAPINetworkingRequestImplementation(url, headers, requestBody, queryParams) {
		override fun makeAPICall(): ResponseModel {
			val fileUri = Uri.parse(fileUriString)
			var connection: HttpURLConnection? = null
			val boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"
			val lineEnd = "\r\n"
			val twoHyphens = "--"
			return try {
				connection = setupConnection()
				connection.doOutput = true
				connection.setRequestProperty(
					"Content-Type",
					"multipart/form-data;boundary=$boundary"
				)
				DataOutputStream(connection.outputStream).use { outputStream ->
					outputStream.writeBytes(twoHyphens + boundary + lineEnd)
					outputStream.writeBytes(
						"Content-Disposition: form-data; name=\"file\";filename=\"${
							fileUri.getFileName()
						}\"$lineEnd"
					)
					outputStream.writeBytes(lineEnd)
					val buffer = ByteArray(1024)
					var bytesRead: Int

					val inputStream =
						AppClass.app.contentResolver.openInputStream(fileUri)
					while (inputStream?.read(buffer)
							.also { bytesRead = it ?: -1 } != -1
					) {
						outputStream.write(buffer, 0, bytesRead)
					}

					outputStream.writeBytes(lineEnd)
					outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
					inputStream?.close()
					outputStream.flush()
				}
				val response = readResponse(connection)
				response
			} catch (ex: Exception) {
				ExceptionResponseModel(ex)
			} finally {
				cleanup(connection)
			}
		}
	}
}
