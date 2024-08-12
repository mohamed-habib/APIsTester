package com.habib.apistester.networking

import android.os.Handler
import android.os.Looper
import android.util.Base64
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.habib.apistester.AppClass
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

interface APINetworkingRequest {
	val requestMethod: HTTPMethod
	fun execute(
		onSuccess: (SuccessResponseModel) -> Unit,
		onError: (ErrorResponseModel) -> Unit
	)

	@WorkerThread
	fun makeAPICall(): ResponseModel
}

//todo: add logging as external dependency and add logging lines at each step
abstract class APINetworkingRequestImplementation(
	private val urlString: String,
	private val headers: Map<String, String>,
	private val requestBody: String,
	private val queryParams: Map<String, String>
) : APINetworkingRequest {
	private val executor = Executors.newFixedThreadPool(4)
	private val handler = Handler(Looper.getMainLooper())

	protected fun setupConnection(): HttpURLConnection {
		val url = URL(addQueryToUrl(urlString, queryParams))
		val connection = url.openConnection() as HttpURLConnection
		connection.requestMethod = requestMethod.methodName
		headers.forEach { (key, value) -> connection.setRequestProperty(key, value) }
		if (requestBody.isNotEmpty())
			BufferedOutputStream(connection.outputStream).use { outputStream ->
				outputStream.write(requestBody.toByteArray())
				outputStream.flush()
			}
		return connection
	}

	private fun addQueryToUrl(
		url: String,
		queryParams: Map<String, String>
	) = if (queryParams.isNotEmpty()) {
		val query = StringBuilder()
		query.append("?")
		queryParams.forEach {
			query.append(it.key + "=" + it.value)
			query.append("&")
		}
		url + query.toString()
	} else url

	protected fun readResponse(connection: HttpURLConnection): ResponseModel {
		val startTime = System.nanoTime()
		when (val responseCode = connection.responseCode) {
			HttpURLConnection.HTTP_OK -> {
				val responseBody = StringBuilder()
				var fileData: String? = null
				BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
					var line: String?
					while (reader.readLine().also { line = it } != null) {
						// Detect the "fileData" key in the JSON structure
						// then don't add it to the response body to avoid having
						// loaded string displayed.
						if (line?.trim()?.startsWith("\"file\"") == true) {
							fileData = extractValue(line)
						} else {
							responseBody.append(line)
						}
					}
				}
				if (fileData != null) {
					// Decode and save the file
					val file = decodeAndSaveFile(
						fileData!!, "downloaded_file"
					).toUri()
					//todo: save the file and send the file path
					// with the response
				}
				val responseHeaders: Map<String, String> =
					connection.headerFields.mapValues { (_, values) ->
						values.firstOrNull() ?: ""
					}
				val endTime = System.nanoTime()
				val executionTime = (endTime - startTime) / 1_000_000

				return SuccessResponseModel(
					responseCode,
					responseHeaders,
					responseBody.toString(),
					executionTime
				)
			}
			else -> {
				val endTime = System.nanoTime()
				val executionTime = (endTime - startTime) / 1_000_000
				return FailedResponseModel(responseCode, "", executionTime)
			}
		}
	}

	override fun execute(
		onSuccess: (SuccessResponseModel) -> Unit,
		onError: (ErrorResponseModel) -> Unit
	) {
		executor.submit {
			try {
				when (val result = makeAPICall()) {
					is SuccessResponseModel -> handler.post { onSuccess(result) }
					is ErrorResponseModel -> handler.post { onError(result) }
				}
			} catch (e: Exception) {
				handler.post {
					onError(ExceptionResponseModel(e))
				}
			}
		}
	}

	protected fun cleanup(connection: HttpURLConnection?) {
		connection?.disconnect()
		executor.shutdown()
	}

	private fun extractValue(line: String?): String {
		return line?.substringAfter(":")?.trim()?.replace("\"", "") ?: ""
	}

	private fun decodeAndSaveFile(
		base64Data: String,
		fileName: String
	): File {
		val file = File(AppClass.app.baseContext.cacheDir, fileName)
		val fileData = Base64.decode(removeDataUrlPrefix(base64Data), Base64.DEFAULT)

		FileOutputStream(file).use { output ->
			output.write(fileData)
		}

		return file
	}

	private fun removeDataUrlPrefix(dataUrl: String): String {
		return if (dataUrl.startsWith("data:")) {
			dataUrl.substringAfter(",")
		} else {
			dataUrl
		}
	}
}

sealed class ResponseModel

data class SuccessResponseModel(
	val responseCode: Int,
	val responseHeaders: Map<String, String>,
	val responseBody: String,
	val executionTime: Long
) : ResponseModel()

sealed class ErrorResponseModel : ResponseModel()

data class FailedResponseModel(
	val responseCode: Int,
	val errorMessage: String,
	val executionTime: Long
) : ErrorResponseModel()

data class ExceptionResponseModel(
	val ex: Exception
) : ErrorResponseModel()
