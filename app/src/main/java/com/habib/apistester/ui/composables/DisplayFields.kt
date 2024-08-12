package com.habib.apistester.ui.composables

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habib.apistester.ApiUiModel
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.R
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.ui.history.toReadableString
import com.habib.apistester.ui.home.openFile

@Composable
fun DisplayCommonFields(apiModel: ApiUiModel) {
	if (apiModel.executionTime.toInt() != -1)
		Text(text = "Execution Time: ${apiModel.executionTime}ms")
	Text(text = "Request Type: ${apiModel.requestType}")
	Text(text = "URL: ${apiModel.requestUrl}")
	Text(text = "Headers: ${apiModel.requestHeaders.toReadableString()}")
	if (apiModel.queryParams.isNotEmpty())
		Text(text = "Query Params: ${apiModel.queryParams.toReadableString()}")
	if (apiModel.requestBody.isNotEmpty())
		Text(text = "Body: ${apiModel.requestBody}")
}

@Composable
fun DisplaySuccess(successApiUiModel: SuccessApiUiModel) {
	val context = LocalContext.current
	Column(modifier = Modifier.padding(16.dp)) {
		DisplayCommonFields(successApiUiModel)
		Text(text = "Response", fontWeight = FontWeight.Bold)
		Text(text = "Status Code: ${successApiUiModel.responseCode}")
		Text(text = "Response Headers: ${successApiUiModel.responseHeaders.toReadableString()}")
		if (successApiUiModel.responseBody.isNotEmpty())
			Text(text = "Response Body: ${successApiUiModel.responseBody}")

		successApiUiModel.fileUriString?.let { fileUri ->
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

@Composable
fun DisplayError(errorApiUiModel: ErrorApiUiModel) {
	Column(modifier = Modifier.padding(16.dp)) {
		Text(
			text = "Error",
			fontWeight = FontWeight.Bold,
			color = Color.Red,
			fontSize = 20.sp
		)
		DisplayCommonFields(errorApiUiModel)
		Text(text = "Response Code: ${errorApiUiModel.responseCode}")
		if (errorApiUiModel.error.isNotEmpty())
			Text(text = "Error Message: ${errorApiUiModel.error}")
	}
}
