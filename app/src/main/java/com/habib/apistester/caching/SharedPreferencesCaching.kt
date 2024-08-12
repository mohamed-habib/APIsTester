package com.habib.apistester.caching

import android.content.SharedPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Base64

class SharedPreferencesCaching(private val sharedPreferences: SharedPreferences) :
	LocalDataSource {
	//todo: handle synchronization

	override fun cacheSuccessGetAPICall(successApiDataModel: SuccessApiDataModel) {
		val list = getList(SUCCESS_GET_KEY).toMutableList()
		list.add(successApiDataModel)
		saveList(SUCCESS_GET_KEY, list)
	}

	override fun cacheFailedGetAPICall(errorApiDataModel: ErrorApiDataModel) {
		val list = getList(FAILED_GET_KEY).toMutableList()
		list.add(errorApiDataModel)
		saveList(FAILED_GET_KEY, list)
	}

	override fun cacheSuccessPostAPICall(successApiDataModel: SuccessApiDataModel) {
		val list = getList(SUCCESS_POST_KEY).toMutableList()
		list.add(successApiDataModel)
		saveList(SUCCESS_POST_KEY, list)
	}

	override fun cacheFailedPostAPICall(errorApiDataModel: ErrorApiDataModel) {
		val list = getList(FAILED_POST_KEY).toMutableList()
		list.add(errorApiDataModel)
		saveList(FAILED_POST_KEY, list)
	}

	override fun getAllApiCalls(onDataRetrieved: (List<ApiDataModel>) -> Unit) {
		val successGet =
			getList(SUCCESS_GET_KEY).filterIsInstance<SuccessApiDataModel>()
		val failedGet =
			getList(FAILED_GET_KEY).filterIsInstance<ErrorApiDataModel>()
		val successPost =
			getList(SUCCESS_POST_KEY).filterIsInstance<SuccessApiDataModel>()
		val failedPost =
			getList(FAILED_POST_KEY).filterIsInstance<ErrorApiDataModel>()
		onDataRetrieved(successGet + successPost + failedGet + failedPost)
	}

	private fun serializeObject(obj: Serializable): ByteArray {
		ByteArrayOutputStream().use { byteArrayOutputStream ->
			ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
				objectOutputStream.writeObject(obj)
				return byteArrayOutputStream.toByteArray()
			}
		}
	}

	private fun deserializeObject(bytes: ByteArray): Serializable {
		ByteArrayInputStream(bytes).use { byteArrayInputStream ->
			ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
				return objectInputStream.readObject() as Serializable
			}
		}
	}

	private fun saveList(key: String, list: List<Serializable>) {
		val serializedList = list.map { serializeObject(it) }
		sharedPreferences.edit().putStringSet(
			key,
			serializedList.map { Base64.getEncoder().encodeToString(it) }.toSet()
		).apply()
	}

	private fun getList(key: String): List<Serializable> {
		val serializedList =
			sharedPreferences.getStringSet(key, emptySet())
				?: emptySet()
		return serializedList.map { Base64.getDecoder().decode(it) }
			.map { deserializeObject(it) }
	}

	companion object {
		private const val SUCCESS_GET_KEY = "success_get"
		private const val FAILED_GET_KEY = "failed_get"
		private const val SUCCESS_POST_KEY = "success_post"
		private const val FAILED_POST_KEY = "failed_post"
	}
}
