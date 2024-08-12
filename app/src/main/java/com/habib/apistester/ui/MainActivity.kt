package com.habib.apistester.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.ui.composables.DisplaySuccess
import com.habib.apistester.ui.history.HistoryScreen
import com.habib.apistester.ui.history.HistoryViewModel
import com.habib.apistester.ui.home.HomeScreen
import com.habib.apistester.ui.theme.APIsTesterTheme


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			APIsTesterTheme {
				MainNavigation()
			}
		}
	}
}


@Composable
fun MainNavigation() {
	val navController = rememberNavController()

	NavHost(navController = navController, startDestination = "home") {
		composable("home") {
			HomeScreen(onNavigateToHistory = {
				navController.navigate("history")
			})
		}
		composable(route = "history") {
			val viewModel: HistoryViewModel = viewModel(
				factory = ViewModelFactory()
			)
			val selectedDisplayOption = remember { viewModel.selectedDisplayOption }
			val apiCalls = remember { viewModel.apiCallsState }

			HistoryScreen(
				selectedDisplayOption = selectedDisplayOption,
				apiCalls = apiCalls,
				getAllCachedApiCalls = { viewModel.getAllCachedApiCalls() },
				getApiCallsSortedByExecutionTime = { viewModel.getApiCallsSortedByExecutionTime() },
				getGetApiCalls = { viewModel.getGetApiCalls() },
				getPostApiCalls = { viewModel.getPostApiCalls() },
				getSuccessApiCalls = { viewModel.getSuccessApiCalls() },
				getFailedApiCalls = { viewModel.getFailedApiCalls() },
				onBackPressed = {
					navController.popBackStack()
				})
		}
	}
}

@Preview(showBackground = true)
@Composable
fun APIsTester() {
	APIsTesterTheme {
		val sampleUIModel = SuccessApiUiModel(
			executionTime = 12,
			requestType = "GET",
			requestUrl = "https://api.example.com/data",
			requestHeaders = mapOf("Content-Type" to "application/json"),
			requestBody = "{\"key\": \"value\"}",
			queryParams = emptyMap(),
			responseCode = 200,
			responseHeaders = mapOf("Content-Type" to "application/json"),
			responseBody = "{\"result\": true}",
			fileUriString = null
		)

		Column {
			DisplaySuccess(sampleUIModel)
		}
	}
}
