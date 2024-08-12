package com.habib.apistester.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.repository.HomeRepository
import com.habib.apistester.ui.home.FlowStatus.Error
import com.habib.apistester.ui.home.FlowStatus.Idle
import com.habib.apistester.ui.home.FlowStatus.Loading
import com.habib.apistester.ui.home.FlowStatus.Success
import com.habib.apistester.ui.home.RequestType.GET

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
	val selectedFileUriString = mutableStateOf<String?>(null)

	var selectedRequestType = mutableStateOf(GET)
	var getAPICallStatus = mutableStateOf<FlowStatus>(Idle)

	fun onFilePicked(uriString: String) {
		selectedFileUriString.value = uriString
	}

	fun makeGetApiCall(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	) {
		repository.makeGetAPICall(
			getAPICallStatus,
			urlString,
			headers,
			requestBody,
			queryParams
		)
	}

	var postAPICallStatus = mutableStateOf<FlowStatus>(Idle)
	fun makePostApiCall(
		urlString: String,
		headers: Map<String, String>,
		jsonBody: String,
		queryParams: Map<String, String>
	) {
		repository.makePostApiCall(
			postAPICallStatus,
			urlString,
			headers,
			jsonBody,
			queryParams
		)
	}

	var multipartAPICallStatus = mutableStateOf<FlowStatus>(Idle)
	fun makeMultipartApiCall(
		url: String,
		headerMap: Map<String, String>,
		requestBody: String,
		queryMap: Map<String, String>,
		fileUri: String
	) {
		repository.makeMultipartApiCall(
			multipartAPICallStatus,
			url,
			headerMap,
			requestBody,
			queryMap,
			fileUri
		)
	}

	fun formatHeaders(headers: String): Map<String, String> =
		headers.takeIf { it.isNotEmpty() }
			?.split(",")?.associate {
				val (key, value) = it.split(":")
				key.trim() to value.trim()
			} ?: emptyMap()

	fun formatQueryParams(queryParams: String): Map<String, String> =
		queryParams.takeIf { it.isNotEmpty() }?.split(",")
			?.associate {
				val (key, value) = it.split("=")
				key.trim() to value.trim()
			} ?: emptyMap()

}

/**
 * The statuses of a flow after making an API call or a request that might take
 * time.
 *
 * The status changes happens in the following way, initially the default value
 * of the status is [Idle] when there's no action happened and we're not waiting
 * for a result, When an action happen e.g. user makes API call or an async
 * request has been made, The status should be [Loading] until a result arrives
 * either a [Success] or an [Error] that is a terminal state.
 */
sealed interface FlowStatus {
	data object Idle : FlowStatus

	data class Success(val result: Any? = null) : FlowStatus

	data object Loading : FlowStatus

	data class Error(val errorApiUiModel: ErrorApiUiModel) : FlowStatus
}
