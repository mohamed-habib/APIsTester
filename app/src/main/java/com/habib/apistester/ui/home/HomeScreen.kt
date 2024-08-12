package com.habib.apistester.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habib.apistester.R
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.getFileName
import com.habib.apistester.isInternetAvailable
import com.habib.apistester.showToast
import com.habib.apistester.ui.ViewModelFactory
import com.habib.apistester.ui.composables.DisplayError
import com.habib.apistester.ui.composables.DisplaySuccess
import com.habib.apistester.ui.home.RequestType.GET
import com.habib.apistester.ui.home.RequestType.MULTIPART
import com.habib.apistester.ui.home.RequestType.POST

@Composable
fun HomeScreen(
	homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory()),
	onNavigateToHistory: () -> Unit
) {
	Scaffold(
		content = { padding ->
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding)
			) {
				HomeScreenHeader(onNavigateToHistory)
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(rememberScrollState())
				) {

					var selectedRequestType by remember { homeViewModel.selectedRequestType }
					RequestTypeSelector(homeViewModel) { requestType ->
						selectedRequestType = requestType
					}

					ApiInputForm(homeViewModel, selectedRequestType)

					when (selectedRequestType) {
						GET -> GetAPIComposable(homeViewModel)
						POST -> PostAPIComposable(homeViewModel)
						MULTIPART -> MultipartAPIComposable(homeViewModel)
					}

					Spacer(modifier = Modifier.height(16.dp))
				}
			}
		}
	)
}

@Composable
fun ColumnScope.GetAPIComposable(homeViewModel: HomeViewModel) {
	Spacer(Modifier.padding(horizontal = 30.dp))
	val getAPICallFlow by remember { homeViewModel.getAPICallStatus }
	when (getAPICallFlow) {
		is FlowStatus.Error -> {
			val error = (getAPICallFlow as FlowStatus.Error).errorApiUiModel
			DisplayError(error)
		}
		FlowStatus.Loading -> {
			CircularProgressIndicator(
				modifier = Modifier.align(
					CenterHorizontally
				)
			)
		}
		is FlowStatus.Success -> {
			val uiModel =
				(getAPICallFlow as FlowStatus.Success).result as SuccessApiUiModel
			DisplaySuccess(uiModel)
		}
		FlowStatus.Idle -> {}
	}
}

@Composable
fun ColumnScope.PostAPIComposable(homeViewModel: HomeViewModel) {
	Spacer(Modifier.padding(horizontal = 30.dp))
	val postAPICallFlow by remember { homeViewModel.postAPICallStatus }
	when (postAPICallFlow) {
		is FlowStatus.Error -> {
			val uiModel =
				(postAPICallFlow as FlowStatus.Error).errorApiUiModel
			DisplayError(uiModel)
		}
		FlowStatus.Loading -> {
			CircularProgressIndicator(
				modifier = Modifier.align(
					CenterHorizontally
				)
			)
		}
		is FlowStatus.Success -> {
			val uiModel =
				(postAPICallFlow as FlowStatus.Success).result as SuccessApiUiModel
			DisplaySuccess(uiModel)
		}
		FlowStatus.Idle -> {}
	}
}

@Composable
fun ColumnScope.MultipartAPIComposable(homeViewModel: HomeViewModel) {
	Spacer(Modifier.padding(horizontal = 30.dp))
	val multipartAPICallStatus by remember { homeViewModel.multipartAPICallStatus }
	when (multipartAPICallStatus) {
		is FlowStatus.Error -> {
			val uiModel =
				(multipartAPICallStatus as FlowStatus.Error).errorApiUiModel
			DisplayError(uiModel)
		}
		FlowStatus.Loading -> {
			CircularProgressIndicator(
				modifier = Modifier.align(
					CenterHorizontally
				)
			)
		}
		is FlowStatus.Success -> {
			val uiModel =
				(multipartAPICallStatus as FlowStatus.Success).result as SuccessApiUiModel
			DisplaySuccess(uiModel)
		}
		FlowStatus.Idle -> {}
	}
}

enum class RequestType(@StringRes val label: Int) {
	GET(R.string.get), POST(R.string.post), MULTIPART(R.string.multipart)
}

@Composable
fun RequestTypeSelector(
	viewModel: HomeViewModel,
	onRequestTypeChanged: (RequestType) -> Unit
) {
	var selectedRequestType by remember { viewModel.selectedRequestType }

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Text(text = "Select Request Type:")
		Row(
			modifier = Modifier.align(Alignment.Start)
		) {
			RadioButton(
				selected = selectedRequestType == GET,
				onClick = { selectedRequestType = GET },
			)
			Text(
				text = "GET",
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
		Row(
			modifier = Modifier.align(Alignment.Start)
		) {
			RadioButton(
				selected = selectedRequestType == POST,
				onClick = { selectedRequestType = POST },
			)
			Text(
				text = "POST",
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
		Row(
			modifier = Modifier.align(Alignment.Start)
		) {
			RadioButton(
				selected = selectedRequestType == MULTIPART,
				onClick = { selectedRequestType = MULTIPART },
			)
			Text(
				text = "POST with Multipart",
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
		// Callback to notify parent component about the selected request type
		onRequestTypeChanged(selectedRequestType)
	}
}

@Composable
fun ApiInputForm(
	viewModel: HomeViewModel,
	selectedRequestType: RequestType
) {
	val context = LocalContext.current
	Column(modifier = Modifier.padding(16.dp)) {
		var url by remember { mutableStateOf("https://httpbin.org/get") }
		var headers by remember { mutableStateOf("Authorization:Bearer token") }
		var requestBody by remember { mutableStateOf("") }
		var queryParams by remember { mutableStateOf("") }

		TextField(
			value = url,
			onValueChange = { url = it },
			label = { Text("Enter URL") },
			modifier = Modifier.fillMaxWidth()
		)

		TextField(
			value = headers,
			onValueChange = { headers = it },
			label = { Text("Enter Headers (comma-separated)") },
			modifier = Modifier.fillMaxWidth()
		)

		TextField(
			value = requestBody,
			onValueChange = { requestBody = it },
			label = { Text("Enter Request Body") },
			modifier = Modifier.fillMaxWidth()
		)

		TextField(
			value = queryParams,
			onValueChange = { queryParams = it },
			label = { Text("Enter Query Params (comma-separated)") },
			modifier = Modifier.fillMaxWidth()
		)
		val selectedFileUri by remember { viewModel.selectedFileUriString }
		val launcher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.StartActivityForResult()
		) { result ->
			if (result.resultCode == Activity.RESULT_OK) {
				val uri = result.data?.data
				uri?.let {
					context.contentResolver.takePersistableUriPermission(
						it,
						Intent.FLAG_GRANT_READ_URI_PERMISSION
					)
					viewModel.onFilePicked(it.toString())
				}
			}
		}
		if (selectedRequestType == MULTIPART) {
			Row(
				modifier = Modifier.padding(vertical = 20.dp),
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically
			) {
				Button(
					onClick = {
						launcher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
							addCategory(Intent.CATEGORY_OPENABLE)
							type = "*/*"
						})
					},
					colors = ButtonDefaults.buttonColors(
						containerColor = Color(
							0xFF6200EE
						)
					),
					contentPadding = ButtonDefaults.ContentPadding,
				) {
					Text(
						text = stringResource(R.string.attach_multipart),
						fontSize = 12.sp,
						modifier = Modifier
							.align(Alignment.CenterVertically)
					)
				}
				Column(modifier = Modifier.padding(5.dp)) {
					selectedFileUri?.let { uriString ->
						val uri = Uri.parse(uriString)
						Text(
							text = uri.getFileName() ?: "",
							fontSize = 12.sp,
						)
						Button(
							onClick = {
								openFile(context, uri)
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
		Button(
			modifier = Modifier
				.padding(top = 20.dp)
				.align(CenterHorizontally),
			onClick = {
				val headerMap = viewModel.formatHeaders(headers)
				val queryMap = viewModel.formatQueryParams(queryParams)
				if (!context.isInternetAvailable()) {
					context.showToast(R.string.no_intenet_connection)
					return@Button
				}

				when (selectedRequestType) {
					GET -> viewModel.makeGetApiCall(
						url,
						headerMap,
						requestBody,
						queryMap
					)
					POST -> viewModel.makePostApiCall(
						url,
						headerMap,
						requestBody,
						queryMap
					)
					MULTIPART -> {
						selectedFileUri?.let { file ->
							viewModel.makeMultipartApiCall(
								url,
								headerMap,
								requestBody,
								queryMap,
								file.toString()
							)
						} ?: run {
							context.showToast(R.string.attachment_required)
						}
					}
				}

			}) {
			Text("Make API Call")
		}
	}
}

@Composable
fun HomeScreenHeader(
	onClick: () -> Unit
) {
	Row(
		modifier = Modifier
			.height(64.dp)
			.fillMaxWidth()
			.background(color = Color(0xFF061744))
			.padding(4.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			modifier = Modifier
				.weight(1f)
				.padding(start = 10.dp),
			text = "API Tester",
			textAlign = TextAlign.Start,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = MaterialTheme.typography.titleMedium,
			color = Color.White
		)

		Button(
			onClick = onClick,
			modifier = Modifier
				.background(
					color = Color(0xFFF0F0F0),
					shape = RoundedCornerShape(16.dp)
				),
			colors = ButtonDefaults.buttonColors(
				containerColor = Color.Transparent
			),
			elevation = ButtonDefaults.buttonElevation(
				defaultElevation = 0.dp
			)
		) {
			Text(
				text = stringResource(R.string.history),
				fontWeight = FontWeight.Bold,
				color = Color.Black
			)
		}

	}
}

fun openFile(context: Context, uri: Uri) {
	val intent = Intent(Intent.ACTION_VIEW)
	intent.setDataAndType(uri, context.contentResolver.getType(uri))
	intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
	context.startActivity(intent)
}