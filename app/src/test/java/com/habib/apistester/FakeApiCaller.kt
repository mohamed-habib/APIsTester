
import com.habib.apistester.networking.APICaller
import com.habib.apistester.networking.APINetworkingRequest
import com.habib.apistester.networking.ErrorResponseModel
import com.habib.apistester.networking.ExceptionResponseModel
import com.habib.apistester.networking.FailedResponseModel
import com.habib.apistester.networking.HTTPMethod
import com.habib.apistester.networking.ResponseModel
import com.habib.apistester.networking.SuccessResponseModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


val fakeSuccessResponseModel = SuccessResponseModel(
	200, emptyMap(), "", 0
)
val fakeFailureResponseModel = FailedResponseModel(
	404,
	"Not found", -1
)
val fakeExceptionResponseModel = ExceptionResponseModel(
	IllegalArgumentException()
)

class FakeAPINetworkingRequestRequest(
	override val requestMethod: HTTPMethod,
	private val expectedSuccessResponseModel: ResponseModel
) : APINetworkingRequest {

	override fun execute(
		onSuccess: (SuccessResponseModel) -> Unit,
		onError: (ErrorResponseModel) -> Unit
	) {
		Executors.newSingleThreadExecutor().execute {
			TimeUnit.SECONDS.sleep(1)
			when (expectedSuccessResponseModel) {
				is SuccessResponseModel -> onSuccess(expectedSuccessResponseModel)
				is ExceptionResponseModel -> onError(expectedSuccessResponseModel)
				is FailedResponseModel -> onError(expectedSuccessResponseModel)
			}

		}
	}

	override fun makeAPICall(): ResponseModel {
		return expectedSuccessResponseModel
	}
}

class FakeApiCaller : APICaller {
	lateinit var expectedResponseModel: ResponseModel

	override fun createGetRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	) = FakeAPINetworkingRequestRequest(
		HTTPMethod.GET,
		expectedResponseModel
	)

	override fun createPostRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>
	) = FakeAPINetworkingRequestRequest(
		HTTPMethod.POST,
		expectedResponseModel
	)

	override fun createMultipartRequest(
		urlString: String,
		headers: Map<String, String>,
		requestBody: String,
		queryParams: Map<String, String>,
		fileUri: String
	) = FakeAPINetworkingRequestRequest(
		HTTPMethod.POST,
		expectedResponseModel
	)

}