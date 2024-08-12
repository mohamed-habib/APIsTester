package com.habib.apistester.ui.history

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.repository.HistoryRepository
import com.habib.apistester.ui.home.FlowStatus
import com.habib.apistester.ui.home.FlowStatus.Idle
import com.habib.apistester.ui.home.RequestType

open class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
	var apiCallsState = mutableStateOf<FlowStatus>(Idle)

	val selectedDisplayOption = mutableStateOf(HistoryDisplayOption.ALL)

	fun getAllCachedApiCalls() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(apiUiModels)
		}
	}

	fun getApiCallsSortedByExecutionTime() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(
				apiUiModels.filter { it.executionTime > -1 }
					.sortedBy { it.executionTime }
			)
		}
	}

	fun getGetApiCalls() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(
				apiUiModels.filter {
					it.requestType == RequestType.GET.name
				}
			)
		}
	}

	fun getPostApiCalls() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(
				apiUiModels.filter {
					it.requestType == RequestType.POST.name
				}
			)
		}
	}

	fun getSuccessApiCalls() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(
				apiUiModels.filterIsInstance<SuccessApiUiModel>()
			)
		}
	}

	fun getFailedApiCalls() {
		apiCallsState.value = FlowStatus.Loading
		repository.getCachedApiCalls { apiUiModels ->
			apiCallsState.value = FlowStatus.Success(
				apiUiModels.filterIsInstance<ErrorApiUiModel>()
			)
		}
	}

}
