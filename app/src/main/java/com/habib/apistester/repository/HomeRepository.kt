package com.habib.apistester.repository

import androidx.compose.runtime.MutableState
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.caching.ErrorApiDataModel
import com.habib.apistester.caching.LocalDataSource
import com.habib.apistester.caching.SuccessApiDataModel
import com.habib.apistester.networking.APICaller
import com.habib.apistester.networking.ExceptionResponseModel
import com.habib.apistester.networking.FailedResponseModel
import com.habib.apistester.ui.home.FlowStatus

class HomeRepository(
	private val localDataSource: LocalDataSource,
	private val apiCaller: APICaller
) {

	fun makeGetAPICall(
		getAPICallFlow: MutableState<FlowStatus>,
		urlString: String,
		requestHeaders: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): MutableState<FlowStatus> {
		getAPICallFlow.value = FlowStatus.Loading
		val getRequest = apiCaller.createGetRequest(
			urlString, requestHeaders,
			requestBody, queryParams
		)

		getRequest.execute({ responseModel ->
			getAPICallFlow.value = FlowStatus.Success(
				SuccessApiUiModel(
					executionTime = responseModel.executionTime,
					requestType = getRequest.requestMethod.methodName,
					requestUrl = urlString,
					requestHeaders = requestHeaders,
					requestBody = requestBody,
					queryParams = queryParams,
					responseCode = responseModel.responseCode,
					responseHeaders = responseModel.responseHeaders,
					responseBody = responseModel.responseBody,
					fileUriString = null
				)
			)

			localDataSource.cacheSuccessGetAPICall(
				SuccessApiDataModel(
					responseModel.executionTime,
					getRequest.requestMethod.methodName,
					urlString,
					requestHeaders,
					requestBody,
					queryParams,
					responseModel.responseCode,
					responseModel.responseHeaders,
					responseModel.responseBody
				)
			)
		}, { errorResponseModel ->
			val errorApiDataModel: ErrorApiDataModel
			val errorApiUiModel: ErrorApiUiModel
			when (errorResponseModel) {
				is ExceptionResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = -1,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = -1,
						requestType = getRequest.requestMethod.methodName,
						error = errorResponseModel.ex.message.toString()
					)

					errorApiDataModel = ErrorApiDataModel(
						-1,
						getRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						-1,
						error = errorResponseModel.ex.message.toString()
					)
				}
				is FailedResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = errorResponseModel.responseCode,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = errorResponseModel.executionTime,
						requestType = getRequest.requestMethod.methodName,
						error = errorResponseModel.errorMessage
					)
					errorApiDataModel = ErrorApiDataModel(
						errorResponseModel.executionTime,
						getRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						errorResponseModel.responseCode,
						errorResponseModel.errorMessage
					)
				}
			}

			getAPICallFlow.value = FlowStatus.Error(errorApiUiModel)
			localDataSource.cacheFailedGetAPICall(errorApiDataModel)
		})
		return getAPICallFlow
	}

	fun makePostApiCall(
		postAPICallFlow: MutableState<FlowStatus>,
		urlString: String,
		requestHeaders: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	): MutableState<FlowStatus> {
		postAPICallFlow.value = FlowStatus.Loading
		val postRequest = apiCaller.createPostRequest(
			urlString, requestHeaders,
			requestBody, queryParams
		)

		postRequest.execute({ responseModel ->
			postAPICallFlow.value = FlowStatus.Success(
				SuccessApiUiModel(
					executionTime = responseModel.executionTime,
					requestType = postRequest.requestMethod.methodName,
					requestUrl = urlString,
					requestHeaders = requestHeaders,
					requestBody = requestBody,
					queryParams = queryParams,
					responseCode = responseModel.responseCode,
					responseHeaders = responseModel.responseHeaders,
					responseBody = responseModel.responseBody,
					fileUriString = null
				)
			)

			localDataSource.cacheSuccessGetAPICall(
				SuccessApiDataModel(
					responseModel.executionTime,
					postRequest.requestMethod.methodName,
					urlString,
					requestHeaders,
					requestBody,
					queryParams,
					responseModel.responseCode,
					responseModel.responseHeaders,
					responseModel.responseBody
				)
			)
		}, { errorResponseModel ->
			val errorApiDataModel: ErrorApiDataModel
			val errorApiUiModel: ErrorApiUiModel
			when (errorResponseModel) {
				is ExceptionResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = -1,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = -1,
						requestType = postRequest.requestMethod.methodName,
						error = errorResponseModel.ex.message.toString()
					)

					errorApiDataModel = ErrorApiDataModel(
						-1,
						postRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						-1,
						error = errorResponseModel.ex.message.toString()
					)
				}
				is FailedResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = errorResponseModel.responseCode,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = errorResponseModel.executionTime,
						requestType = postRequest.requestMethod.methodName,
						error = errorResponseModel.errorMessage
					)
					errorApiDataModel = ErrorApiDataModel(
						errorResponseModel.executionTime,
						postRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						errorResponseModel.responseCode,
						errorResponseModel.errorMessage
					)
				}
			}

			postAPICallFlow.value = FlowStatus.Error(errorApiUiModel)
			localDataSource.cacheFailedGetAPICall(errorApiDataModel)
		})
		return postAPICallFlow
	}

	fun makeMultipartApiCall(
		multipartAPICallStatus: MutableState<FlowStatus>,
		urlString: String,
		requestHeaders: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>,
		fileUri: String
	): MutableState<FlowStatus> {
		val multipartRequest = apiCaller.createMultipartRequest(
			urlString, requestHeaders,
			requestBody, queryParams, fileUri
		)

		multipartAPICallStatus.value = FlowStatus.Loading
		multipartRequest.execute({ responseModel ->
			multipartAPICallStatus.value = FlowStatus.Success(
				SuccessApiUiModel(
					responseCode = responseModel.responseCode,
					responseHeaders = responseModel.responseHeaders,
					responseBody = responseModel.responseBody,
					requestUrl = urlString,
					queryParams = queryParams,
					requestHeaders = requestHeaders,
					requestBody = requestBody,
					executionTime = responseModel.executionTime,
					requestType = multipartRequest.requestMethod.methodName,
					fileUriString = fileUri
				)
			)

			localDataSource.cacheSuccessGetAPICall(
				SuccessApiDataModel(
					responseModel.executionTime,
					multipartRequest.requestMethod.methodName,
					urlString,
					requestHeaders,
					requestBody,
					queryParams,
					responseModel.responseCode,
					responseModel.responseHeaders,
					responseModel.responseBody,
					fileUri.toString()
				)
			)
		}, { errorResponseModel ->
			val errorApiDataModel: ErrorApiDataModel
			val errorApiUiModel: ErrorApiUiModel
			when (errorResponseModel) {
				is ExceptionResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = -1,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = -1,
						requestType = multipartRequest.requestMethod.methodName,
						error = errorResponseModel.ex.message.toString()
					)

					errorApiDataModel = ErrorApiDataModel(
						-1,
						multipartRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						-1,
						error = errorResponseModel.ex.message.toString()
					)
				}
				is FailedResponseModel -> {
					errorApiUiModel = ErrorApiUiModel(
						responseCode = errorResponseModel.responseCode,
						requestUrl = urlString,
						queryParams = queryParams,
						requestHeaders = requestHeaders,
						requestBody = requestBody,
						executionTime = errorResponseModel.executionTime,
						requestType = multipartRequest.requestMethod.methodName,
						error = errorResponseModel.errorMessage
					)
					errorApiDataModel = ErrorApiDataModel(
						errorResponseModel.executionTime,
						multipartRequest.requestMethod.methodName,
						urlString,
						requestHeaders,
						requestBody,
						queryParams,
						errorResponseModel.responseCode,
						errorResponseModel.errorMessage
					)
				}
			}
			multipartAPICallStatus.value = FlowStatus.Error(errorApiUiModel)
			localDataSource.cacheFailedGetAPICall(errorApiDataModel)
		})
		return multipartAPICallStatus
	}
}
