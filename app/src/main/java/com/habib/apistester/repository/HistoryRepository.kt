package com.habib.apistester.repository

import com.habib.apistester.ApiUiModel
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.caching.ApiDataModel
import com.habib.apistester.caching.ErrorApiDataModel
import com.habib.apistester.caching.LocalDataSource
import com.habib.apistester.caching.SuccessApiDataModel

class HistoryRepository(private val localDataSource: LocalDataSource) {

	fun getCachedApiCalls(onDataRetrieved: (List<ApiUiModel>) -> Unit) {
		localDataSource.getAllApiCalls { apiCalls ->
			onDataRetrieved(apiCalls.toUi())
		}
	}
}

fun List<ApiDataModel>.toUi() = this.map { apiDataModel ->
	when (apiDataModel) {
		is SuccessApiDataModel -> apiDataModel.mapToSuccessUiModel()
		is ErrorApiDataModel -> apiDataModel.mapToErrorUiModel()
	}
}

fun SuccessApiDataModel.mapToSuccessUiModel(): SuccessApiUiModel {
	return SuccessApiUiModel(
		executionTime = executionTime,
		requestType = requestType,
		requestUrl = requestUrl,
		requestHeaders = requestHeaders,
		requestBody = requestBody,
		queryParams = queryParams,
		responseCode = responseCode,
		responseHeaders = responseHeaders,
		responseBody = responseBody,
		fileUriString = fileUri
	)
}

fun ErrorApiDataModel.mapToErrorUiModel(): ErrorApiUiModel {
	return ErrorApiUiModel(
		executionTime = executionTime,
		requestType = requestType,
		requestUrl = requestUrl,
		requestHeaders = requestHeaders,
		requestBody = requestBody,
		queryParams = queryParams,
		responseCode = responseCode,
		error = error
	)
}
