package com.habib.apistester.ui.history

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habib.apistester.ApiUiModel
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.R
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.ui.composables.Header
import com.habib.apistester.ui.history.HistoryDisplayOption.ALL
import com.habib.apistester.ui.history.HistoryDisplayOption.FILTERED_BY_GET
import com.habib.apistester.ui.history.HistoryDisplayOption.FILTERED_BY_POST
import com.habib.apistester.ui.history.HistoryDisplayOption.FILTER_BY_FAILED
import com.habib.apistester.ui.history.HistoryDisplayOption.FILTER_BY_SUCCESS
import com.habib.apistester.ui.history.HistoryDisplayOption.SORT_BY_EXECUTION_TIME
import com.habib.apistester.ui.home.FlowStatus
import com.habib.apistester.ui.home.openFile

@Composable
fun CachedApiCallsList(apiUiModels: List<ApiUiModel>) {
	LazyColumn(
		modifier = Modifier.padding(20.dp),
		contentPadding = PaddingValues(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(apiUiModels) { apiUiModel ->
			when (apiUiModel) {
				is SuccessApiUiModel -> {
					SuccessApiItem(apiUiModel)
					Spacer(modifier = Modifier.height(8.dp))
				}
				is ErrorApiUiModel -> {
					ErrorApiItem(apiUiModel)
					Spacer(modifier = Modifier.height(8.dp))
				}
			}
		}
	}
}

@Composable
fun SuccessApiItem(apiUiModel: SuccessApiUiModel) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		)
	) {
		val context = LocalContext.current
		Column(modifier = Modifier.padding(16.dp)) {
			ApiItemText("Request Type:", apiUiModel.requestType)
			ApiItemText("Request URL:", apiUiModel.requestUrl)
			ApiItemText(
				"Request Headers:",
				apiUiModel.requestHeaders.toReadableString()
			)
			ApiItemText("Request Body:", apiUiModel.requestBody)
			ApiItemText("Query Params:", apiUiModel.queryParams.toReadableString())
			ApiItemText("Response Code:", apiUiModel.responseCode.toString())
			ApiItemText(
				"Response Headers:",
				apiUiModel.responseHeaders.toReadableString()
			)
			ApiItemText("Response Body:", apiUiModel.responseBody)
			if (apiUiModel.executionTime.toInt() != -1) {
				ApiItemText("Execution Time:", "${apiUiModel.executionTime} ms")
			}
			apiUiModel.fileUriString?.let { fileUri ->
				Button(
					onClick = {
						openFile(context, Uri.parse(fileUri))
					},
					colors = ButtonDefaults.buttonColors(
						containerColor = Color.DarkGray
					),
					contentPadding = ButtonDefaults.ContentPadding,
				) {
					Text(
						text = stringResource(R.string.open_file),
						fontSize = 12.sp,
						modifier = Modifier
							.align(Alignment.CenterVertically)
					)
				}

			}
		}
	}
}

@Composable
fun ErrorApiItem(apiUiModel: ErrorApiUiModel) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		)

	) {
		Column(modifier = Modifier.padding(16.dp)) {
			ApiItemText("Request Type:", apiUiModel.requestType)
			ApiItemText("Request URL:", apiUiModel.requestUrl)
			ApiItemText(
				"Request Headers:",
				apiUiModel.requestHeaders.toReadableString()
			)
			if (apiUiModel.requestBody.isNotEmpty()) {
				ApiItemText("Request Body:", apiUiModel.requestBody)
			}
			if (apiUiModel.queryParams.isNotEmpty()) {
				ApiItemText(
					"Query Params:",
					apiUiModel.queryParams.toReadableString()
				)
			}
			ApiItemText("Response Code:", apiUiModel.responseCode.toString())
			ApiItemText("Error:", apiUiModel.error)
			if (apiUiModel.executionTime.toInt() != -1) {
				ApiItemText("Execution Time:", "${apiUiModel.executionTime} ms")
			}
		}
	}
}

@Composable
fun ApiItemText(label: String, value: String) {
	if (value.isNotEmpty())
		Column(modifier = Modifier.padding(vertical = 4.dp)) {
			Text(
				text = label,
				style = MaterialTheme.typography.titleMedium,
				modifier = Modifier.padding(bottom = 2.dp)
			)
			Text(
				text = value,
				style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
				modifier = Modifier
					.background(Color.LightGray)
					.padding(4.dp)
			)
		}
}

@Composable
fun HistoryScreen(
	selectedDisplayOption: MutableState<HistoryDisplayOption>,
	apiCalls: MutableState<FlowStatus>,
	getAllCachedApiCalls: () -> Unit,
	getApiCallsSortedByExecutionTime: () -> Unit,
	getGetApiCalls: () -> Unit,
	getPostApiCalls: () -> Unit,
	getSuccessApiCalls: () -> Unit,
	getFailedApiCalls: () -> Unit,
	onBackPressed: () -> Unit,
) {
	Scaffold(
		content = { padding ->
			Column(Modifier.padding(padding)) {
				Header(stringResource(R.string.history_screen_title), onBackPressed)

				DisplayOptionsDropdown {
					selectedDisplayOption.value = it
				}

				when (selectedDisplayOption.value) {
					ALL -> getAllCachedApiCalls()
					SORT_BY_EXECUTION_TIME -> getApiCallsSortedByExecutionTime()
					FILTERED_BY_GET -> getGetApiCalls()
					FILTERED_BY_POST -> getPostApiCalls()
					FILTER_BY_SUCCESS -> getSuccessApiCalls()
					FILTER_BY_FAILED -> getFailedApiCalls()
				}
				when (apiCalls.value) {
					FlowStatus.Loading -> CircularProgressIndicator(
						modifier = Modifier.align(
							CenterHorizontally
						)
					)
					is FlowStatus.Success -> {
						val uiModel =
							(apiCalls as FlowStatus.Success).result as List<*>
						//using filter here to avoid the unchecked cast warning,
						//all the values should be of [ApiUiModel] anyways.
						CachedApiCallsList(uiModel.filterIsInstance<ApiUiModel>())
					}
					else -> {}
				}
			}
		})
}

@Composable
fun DisplayOptionsDropdown(onOptionSelected: (HistoryDisplayOption) -> Unit) {
	var expanded by remember { mutableStateOf(false) }
	var selectedOption by remember { mutableStateOf(ALL) }

	Box {
		TextButton(
			onClick = { expanded = true },
			modifier = Modifier
				.padding(start = 20.dp, top = 20.dp)
				.wrapContentSize()
				.background(MaterialTheme.colorScheme.primary),
		) {
			Row(
				verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
			) {
				Text(
					text = selectedOption.label,
					color = Color.White,
					modifier = Modifier.padding(end = 8.dp)
				)
				Icon(
					imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
					contentDescription = if (expanded) "Collapse" else "Expand",
					tint = Color.White
				)
			}
		}

		DropdownMenu(
			modifier = Modifier
				.padding(20.dp),
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			HistoryDisplayOption.entries.forEach { option ->
				DropdownMenuItem(
					onClick = {
						selectedOption = option
						onOptionSelected(option)
						expanded = false
					}, text = {
						Text(text = option.label)
					})
			}
		}
	}
}

enum class HistoryDisplayOption(val label: String) {
	ALL("All Cached Requests/Responses"),
	SORT_BY_EXECUTION_TIME("Sorted by Execution Time"),
	FILTERED_BY_GET("Filtered by GET"),
	FILTERED_BY_POST("Filtered by POST"),
	FILTER_BY_SUCCESS("Filtered by Success Status"),
	FILTER_BY_FAILED("Filtered by Failed Status")
}

fun Map<String, String>.toReadableString(): String {
	return this.entries.joinToString(", \n") { "${it.key}: ${it.value}" }
}
