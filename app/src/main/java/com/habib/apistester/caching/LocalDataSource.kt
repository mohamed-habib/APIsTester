package com.habib.apistester.caching

import androidx.annotation.WorkerThread
import java.io.Serializable

sealed class ApiDataModel : Serializable

data class SuccessApiDataModel(
	val executionTime: Long,
	val requestType: String,
	val requestUrl: String,
	val requestHeaders: Map<String, String>,
	val requestBody: String,
	val queryParams: Map<String, String>,
	val responseCode: Int,
	val responseHeaders: Map<String, String>,
	val responseBody: String,
	val fileUri: String? = null
) : ApiDataModel()

data class ErrorApiDataModel(
	val executionTime: Long,
	val requestType: String,
	val requestUrl: String,
	val requestHeaders: Map<String, String>,
	val requestBody: String,
	val queryParams: Map<String, String>,
	val responseCode: Int,
	val error: String
) : ApiDataModel()

interface LocalDataSource {
	fun cacheSuccessGetAPICall(successApiDataModel: SuccessApiDataModel)
	fun cacheFailedGetAPICall(errorApiDataModel: ErrorApiDataModel)
	fun cacheSuccessPostAPICall(successApiDataModel: SuccessApiDataModel)
	fun cacheFailedPostAPICall(errorApiDataModel: ErrorApiDataModel)
	@WorkerThread//todo: handle synchronization
	fun getAllApiCalls(onDataRetrieved: (List<ApiDataModel>) -> Unit)
}
