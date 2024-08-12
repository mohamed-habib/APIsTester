package com.habib.apistester.ui.home

import FakeApiCaller
import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.FakeDataSource
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.networking.HTTPMethod
import com.habib.apistester.repository.HomeRepository
import com.habib.apistester.ui.home.FlowStatus.Loading
import fakeExceptionResponseModel
import fakeFailureResponseModel
import fakeSuccessResponseModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

	private lateinit var viewModel: HomeViewModel
	private lateinit var fakeApiCaller: FakeApiCaller

	@Before
	fun setup() {
		fakeApiCaller = FakeApiCaller()
		viewModel = HomeViewModel(HomeRepository(FakeDataSource(), fakeApiCaller))
	}

	@After
	fun tearDown() {
	}

	@Test
	fun `onFilePicked updates selectedFileUri`() {
		viewModel.onFilePicked("uri")
		assertEquals("uri", viewModel.selectedFileUriString.value)
	}

	@Test
	fun `makeGetAPICall updates getAPICallStatus with Success`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeSuccessResponseModel

		val expectedSuccessApiUiModel = SuccessApiUiModel(
			fakeSuccessResponseModel.executionTime,
			requestType = HTTPMethod.GET.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeSuccessResponseModel.responseCode,
			fakeSuccessResponseModel.responseHeaders,
			fakeSuccessResponseModel.responseBody,
			null
		)

		viewModel.makeGetApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.getAPICallStatus.value)

		Thread.sleep(1100)

		val expectedSuccessStatus = FlowStatus.Success(expectedSuccessApiUiModel)
		assertEquals(expectedSuccessStatus, viewModel.getAPICallStatus.value)
	}

	@Test
	fun `makeGetAPICall updates getAPICallStatus with Failure`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeFailureResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			fakeFailureResponseModel.executionTime,
			requestType = HTTPMethod.GET.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeFailureResponseModel.responseCode,
			fakeFailureResponseModel.errorMessage
		)
		viewModel.makeGetApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.getAPICallStatus.value)

		Thread.sleep(1100)

		val expectedStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedStatus, viewModel.getAPICallStatus.value)
	}

	@Test
	fun `makeGetAPICall updates getAPICallStatus with Exception`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeExceptionResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			-1,
			requestType = HTTPMethod.GET.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			-1,
			error = fakeExceptionResponseModel.ex.message.toString()
		)
		viewModel.makeGetApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.getAPICallStatus.value)

		Thread.sleep(1100)

		val expectedStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedStatus, viewModel.getAPICallStatus.value)
	}

	@Test
	fun `makePostApiCall updates postAPICallStatus with Success`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeSuccessResponseModel

		val expectedSuccessApiUiModel = SuccessApiUiModel(
			fakeSuccessResponseModel.executionTime,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeSuccessResponseModel.responseCode,
			fakeSuccessResponseModel.responseHeaders,
			fakeSuccessResponseModel.responseBody,
			null
		)

		viewModel.makePostApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.postAPICallStatus.value)

		Thread.sleep(1100)

		val expectedSuccessStatus = FlowStatus.Success(expectedSuccessApiUiModel)
		assertEquals(expectedSuccessStatus, viewModel.postAPICallStatus.value)
	}

	@Test
	fun `makePostAPICall updates postAPICallStatus with Failure`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeFailureResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			fakeFailureResponseModel.executionTime,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeFailureResponseModel.responseCode,
			fakeFailureResponseModel.errorMessage
		)
		viewModel.makePostApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.postAPICallStatus.value)

		Thread.sleep(1100)

		val expectedStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedStatus, viewModel.postAPICallStatus.value)
	}

	@Test
	fun `makePostAPICall updates postAPICallStatus with Exception`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()

		fakeApiCaller.expectedResponseModel = fakeExceptionResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			-1,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			-1,
			error = fakeExceptionResponseModel.ex.message.toString()
		)
		viewModel.makePostApiCall(urlString, headers, requestBody, queryParams)
		assertEquals(Loading, viewModel.postAPICallStatus.value)

		Thread.sleep(1100)

		val expectedStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedStatus, viewModel.postAPICallStatus.value)
	}

	@Test
	fun `makeMultipartApiCall updates multipartAPICallStatus with Success`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()
		val fileUri = "fileUri"

		fakeApiCaller.expectedResponseModel = fakeSuccessResponseModel

		val expectedSuccessApiUiModel = SuccessApiUiModel(
			fakeSuccessResponseModel.executionTime,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeSuccessResponseModel.responseCode,
			fakeSuccessResponseModel.responseHeaders,
			fakeSuccessResponseModel.responseBody,
			fileUri
		)

		viewModel.makeMultipartApiCall(
			urlString, headers, requestBody,
			queryParams, fileUri
		)
		assertEquals(Loading, viewModel.multipartAPICallStatus.value)

		Thread.sleep(1100)

		val expectedSuccessStatus = FlowStatus.Success(expectedSuccessApiUiModel)
		assertEquals(expectedSuccessStatus, viewModel.multipartAPICallStatus.value)
	}

	@Test
	fun `makeMultipartApiCall updates multipartAPICallStatus with Failure`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()
		val fileUri = "fileUri"

		fakeApiCaller.expectedResponseModel = fakeFailureResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			fakeFailureResponseModel.executionTime,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			fakeFailureResponseModel.responseCode,
			fakeFailureResponseModel.errorMessage
		)

		viewModel.makeMultipartApiCall(
			urlString, headers, requestBody,
			queryParams, fileUri
		)
		assertEquals(Loading, viewModel.multipartAPICallStatus.value)

		Thread.sleep(1100)

		val expectedSuccessStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedSuccessStatus, viewModel.multipartAPICallStatus.value)
	}

	@Test
	fun `makeMultipartApiCall updates multipartAPICallStatus with Exception`() {
		val urlString = "url"
		val headers = emptyMap<String, String>()
		val requestBody = ""
		val queryParams = emptyMap<String, String>()
		val fileUri = "fileUri"

		fakeApiCaller.expectedResponseModel = fakeExceptionResponseModel

		val expectedErrorApiUiModel = ErrorApiUiModel(
			-1,
			requestType = HTTPMethod.POST.methodName,
			urlString,
			headers,
			requestBody,
			queryParams,
			-1,
			error = fakeExceptionResponseModel.ex.message.toString()
		)

		viewModel.makeMultipartApiCall(
			urlString, headers, requestBody,
			queryParams, fileUri
		)
		assertEquals(Loading, viewModel.multipartAPICallStatus.value)

		Thread.sleep(1100)

		val expectedSuccessStatus = FlowStatus.Error(expectedErrorApiUiModel)
		assertEquals(expectedSuccessStatus, viewModel.multipartAPICallStatus.value)
	}

	@Test
	fun `formatHeaders returns empty map for empty string`() {
		val headers = ""
		val result = viewModel.formatHeaders(headers)
		assertEquals(emptyMap<String, String>(), result)
	}

	@Test
	fun `formatHeaders returns correct map for non-empty string`() {
		val headers = "key1: value1, key2: value2"
		val expected = mapOf("key1" to "value1", "key2" to "value2")
		val result = viewModel.formatHeaders(headers)
		assertEquals(expected, result)
	}

	@Test
	fun `formatQueryParams returns empty map for empty string`() {
		val queryParams = ""
		val result = viewModel.formatQueryParams(queryParams)
		assertEquals(emptyMap<String, String>(), result)
	}

	@Test
	fun `formatQueryParams returns correct map for non-empty string`() {
		val queryParams = "key1=value1,key2=value2"
		val expected = mapOf("key1" to "value1", "key2" to "value2")
		val result = viewModel.formatQueryParams(queryParams)
		assertEquals(expected, result)
	}

}