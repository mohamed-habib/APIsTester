package com.habib.apistester

import com.habib.apistester.caching.ApiDataModel
import com.habib.apistester.caching.ErrorApiDataModel
import com.habib.apistester.caching.LocalDataSource
import com.habib.apistester.caching.SuccessApiDataModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FakeDataSource : LocalDataSource {

	 val apiCalls = mutableListOf<ApiDataModel>()

	override fun cacheSuccessGetAPICall(successApiDataModel: SuccessApiDataModel) {
		apiCalls.add(successApiDataModel)
	}

	override fun cacheFailedGetAPICall(errorApiDataModel: ErrorApiDataModel) {
		apiCalls.add(errorApiDataModel)
	}

	override fun cacheSuccessPostAPICall(successApiDataModel: SuccessApiDataModel) {
		apiCalls.add(successApiDataModel)
	}

	override fun cacheFailedPostAPICall(errorApiDataModel: ErrorApiDataModel) {
		apiCalls.add(errorApiDataModel)
	}

	override fun getAllApiCalls(onDataRetrieved: (List<ApiDataModel>) -> Unit) {
		Executors.newSingleThreadExecutor().execute {
			TimeUnit.SECONDS.sleep(1)
			onDataRetrieved(apiCalls)
		}
	}

	fun populate(mockApiCalls: List<ApiDataModel>) {
		apiCalls.clear()
		apiCalls.addAll(mockApiCalls)
	}

}

val mockErrorApiDataModels = listOf(
	ErrorApiDataModel(
		executionTime = 1678893600000, // Example timestamp
		requestType = "GET",
		requestUrl = "https://www.example.com/api/posts/123",
		requestHeaders = emptyMap(),
		requestBody = "",
		queryParams = emptyMap(),
		responseCode = 404,
		error = "Not Found"
	),
	ErrorApiDataModel(
		executionTime = 1678897200000, // Example timestamp
		requestType = "PUT",
		requestUrl = "https://www.example.com/api/users/456",
		requestHeaders = mapOf("Content-Type" to "application/json"),
		requestBody = """
            {
              "name": "Updated Name"
            }
        """.trimIndent(),
		queryParams = emptyMap(),
		responseCode = 500,
		error = "Internal Server Error"
	)
)

val mockSuccessApiDataModels = listOf(
	SuccessApiDataModel(
		executionTime = 1678886400000, // Example timestamp
		requestType = "GET",
		requestUrl = "https://www.example.com/api/users",
		requestHeaders = mapOf("Authorization" to "Bearer token"),
		requestBody = "",
		queryParams = mapOf("page" to "1", "limit" to "10"),
		responseCode = 200,
		responseHeaders = mapOf("Content-Type" to "application/json"),
		responseBody = """
            [
              {
                "id": 1,
                "name": "John Doe"
              },
              {
                "id": 2,
                "name": "Jane Doe"
              }
            ]
        """.trimIndent(),
		fileUri = null
	),
	SuccessApiDataModel(
		executionTime = 1678890000000, // Example timestamp
		requestType = "POST",
		requestUrl = "https://www.example.com/api/products",
		requestHeaders = mapOf("Content-Type" to "application/json"),
		requestBody = """
            {
              "name": "New Product",
              "description": "This is a new product"
            }
        """.trimIndent(),
		queryParams = emptyMap(),
		responseCode = 201,
		responseHeaders = mapOf("Location" to "/api/products/123"),
		responseBody = "",
		fileUri = "content://com.android.providers.media.documents/document/image%3A123"
	)
)

val mockApiCalls = mockErrorApiDataModels + mockSuccessApiDataModels
