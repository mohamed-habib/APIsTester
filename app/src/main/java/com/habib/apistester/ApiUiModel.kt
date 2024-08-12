package com.habib.apistester

sealed interface ApiUiModel {
	val executionTime: Long
	val requestType: String
	val requestUrl: String
	val requestHeaders: Map<String, String>
	val requestBody: String
	val queryParams: Map<String, String>
}

data class SuccessApiUiModel(
	override val executionTime: Long,
	override val requestType: String,
	override val requestUrl: String,
	override val requestHeaders: Map<String, String>,
	override val requestBody: String,
	override val queryParams: Map<String, String>,
	val responseCode: Int,
	val responseHeaders: Map<String, String>,
	val responseBody: String,
	val fileUriString: String?
) : ApiUiModel

data class ErrorApiUiModel(
	override val executionTime: Long,
	override val requestType: String,
	override val requestUrl: String,
	override val requestHeaders: Map<String, String>,
	override val requestBody: String,
	override val queryParams: Map<String, String>,
	val responseCode: Int,
	val error: String
) : ApiUiModel
