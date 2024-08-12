package com.habib.apistester.networking

import java.net.HttpURLConnection

class GetAPINetworkingRequest(
	url: String,
	headers: Map<String, String>,
	requestBody: String,
	queryParams: Map<String, String>
) : APINetworkingRequestImplementation(url, headers, requestBody, queryParams) {
	override val requestMethod: HTTPMethod = HTTPMethod.GET
	override fun makeAPICall(): ResponseModel {
		var connection: HttpURLConnection? = null
		return try {
			connection = setupConnection()
			val response = readResponse(connection)
			response
		} catch (ex: Exception) {
			ExceptionResponseModel(ex)
		} finally {
			cleanup(connection)
		}
	}
}
