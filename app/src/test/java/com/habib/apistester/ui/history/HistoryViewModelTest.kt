package com.habib.apistester.ui.history

import com.habib.apistester.ErrorApiUiModel
import com.habib.apistester.FakeDataSource
import com.habib.apistester.SuccessApiUiModel
import com.habib.apistester.mockApiCalls
import com.habib.apistester.repository.HistoryRepository
import com.habib.apistester.repository.toUi
import com.habib.apistester.ui.home.FlowStatus
import com.habib.apistester.ui.home.RequestType
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HistoryViewModelTest {

	private lateinit var viewModel: HistoryViewModel
	private lateinit var fakeDataSource: FakeDataSource
	private lateinit var repository: HistoryRepository

	@Before
	fun setup() {
		fakeDataSource = FakeDataSource()
		repository = HistoryRepository(fakeDataSource)
		viewModel = HistoryViewModel(repository)
	}

	@Test
	fun `getAllCachedApiCalls updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls = fakeDataSource.apiCalls.toUi()

		viewModel.getAllCachedApiCalls()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)

		Thread.sleep(2500)

		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}

	@Test
	fun `getApiCallsSortedByExecutionTime updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls =
			fakeDataSource.apiCalls.toUi().filter { it.executionTime > -1 }
				.sortedBy { it.executionTime }

		viewModel.getApiCallsSortedByExecutionTime()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)
		Thread.sleep(2500)
		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}

	@Test
	fun `getGetApiCalls updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls =
			fakeDataSource.apiCalls.toUi()
				.filter { it.requestType == RequestType.GET.name }

		viewModel.getGetApiCalls()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)
		Thread.sleep(2500)
		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}

	@Test
	fun `getPostApiCalls updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls =
			fakeDataSource.apiCalls.toUi()
				.filter { it.requestType == RequestType.POST.name }

		viewModel.getPostApiCalls()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)
		Thread.sleep(2500)
		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}

	@Test
	fun `getSuccessApiCalls updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls =
			fakeDataSource.apiCalls.toUi()
				.filterIsInstance<SuccessApiUiModel>()

		viewModel.getSuccessApiCalls()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)
		Thread.sleep(2500)
		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}

	@Test
	fun `getFailedApiCalls updates apiCallsState with Success`() {
		fakeDataSource.populate(mockApiCalls)
		val expectedApiCalls =
			fakeDataSource.apiCalls.toUi()
				.filterIsInstance<ErrorApiUiModel>()

		viewModel.getFailedApiCalls()
		Assert.assertEquals(FlowStatus.Loading, viewModel.apiCallsState.value)
		Thread.sleep(2500)
		Assert.assertEquals(
			FlowStatus.Success(expectedApiCalls), viewModel.apiCallsState.value
		)
	}
}
