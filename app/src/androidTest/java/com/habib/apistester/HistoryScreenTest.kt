package com.habib.apistester

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.habib.apistester.caching.ApiDataModel
import com.habib.apistester.caching.ErrorApiDataModel
import com.habib.apistester.caching.LocalDataSource
import com.habib.apistester.caching.SuccessApiDataModel
import com.habib.apistester.repository.HistoryRepository
import com.habib.apistester.ui.MainActivity
import com.habib.apistester.ui.history.HistoryDisplayOption
import com.habib.apistester.ui.history.HistoryScreen
import com.habib.apistester.ui.history.HistoryViewModel
import com.habib.apistester.ui.home.FlowStatus
import com.habib.apistester.ui.theme.APIsTesterTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {
	@get:Rule
	val composeTestRule = createAndroidComposeRule<MainActivity>()

	@Before
	fun setUp() {
		// Setup or mock your ViewModel or dependencies here
	}

	private fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.clearAndSetContent(
		content: @Composable () -> Unit
	) {
		(this.activity.findViewById<ViewGroup>(android.R.id.content)
			?.getChildAt(0) as? ComposeView)?.setContent(content)
			?: this.setContent(content)
	}

	@Test
	fun testDisplayOptionsDropdownAndContent() {
		composeTestRule.clearAndSetContent {
			APIsTesterTheme {
				HistoryScreen(
					remember { mutableStateOf(HistoryDisplayOption.ALL) },
					remember { mutableStateOf(FlowStatus.Loading) },
					{}, {},
					{}, {},
					{}, {},
					{}
				)
			}
		}

		// Check if dropdown is displayed
		composeTestRule.onNodeWithText("All Cached Requests/Responses")
			.assertExists()
		composeTestRule.onNodeWithText("All Cached Requests/Responses")
			.performClick()
		// Check if dropdown items are displayed
		composeTestRule.onNodeWithText("Sorted by Execution Time").assertExists()
		composeTestRule.onNodeWithText("Filtered by GET").assertExists()
		composeTestRule.onNodeWithText("Filtered by POST").assertExists()
		composeTestRule.onNodeWithText("Filtered by Success Status")
			.assertExists()
		composeTestRule.onNodeWithText("Filtered by Failed Status")
			.assertExists()

		// Select an option
		composeTestRule.onNodeWithText("Sorted by Execution Time").performClick()
	}

	class FakeHistoryViewModel :
		HistoryViewModel(HistoryRepository(object : LocalDataSource {
			override fun cacheSuccessGetAPICall(successApiDataModel: SuccessApiDataModel) {
			}

			override fun cacheFailedGetAPICall(errorApiDataModel: ErrorApiDataModel) {
			}

			override fun cacheSuccessPostAPICall(successApiDataModel: SuccessApiDataModel) {
			}

			override fun cacheFailedPostAPICall(errorApiDataModel: ErrorApiDataModel) {
			}

			override fun getAllApiCalls(onDataRetrieved: (List<ApiDataModel>) -> Unit) {
				onDataRetrieved(
					listOf()
				)
			}
		})) {
	}

	@Test
	fun useAppContext() {
		// Context of the app under test.
		val appContext = InstrumentationRegistry.getInstrumentation().targetContext
		assertEquals("com.habib.apistester", appContext.packageName)
	}


}
