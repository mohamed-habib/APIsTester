package com.habib.apistester

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceDataStore

class AppClass : Application() {

	init {
		app = this
	}

	val sharedPreferences: SharedPreferences by lazy {
		getSharedPreferences("api_cache", Context.MODE_PRIVATE)
	}

	companion object {
		/** The singular running [AppClass]. */
		lateinit var app: AppClass
			private set
	}
}