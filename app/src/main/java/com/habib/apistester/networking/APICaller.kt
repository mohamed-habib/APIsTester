package com.habib.apistester.networking

import android.net.Uri

interface APICaller {
	fun createGetRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): APINetworkingRequest

	fun createPostRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): APINetworkingRequest

	fun createMultipartRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>,
		fileUri: String
	): APINetworkingRequest
}

class DefaultAPICaller : APICaller {
	override fun createGetRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): GetAPINetworkingRequest = GetAPINetworkingRequest(
		urlString,
		headers,
		requestBody,
		queryParams
	)

	override fun createPostRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): PostAPINetworkingRequestImplementation =
		PostAPINetworkingRequestImplementation.JsonPostAPINetworkingRequestImplementation(
			urlString,
			headers,
			requestBody,
			queryParams
		)

	override fun createMultipartRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>,
		fileUri: String
	): PostAPINetworkingRequestImplementation =
		PostAPINetworkingRequestImplementation.MultipartPostAPINetworkingRequestImplementation(
			urlString, headers, fileUri,
			requestBody, queryParams
		)
}
