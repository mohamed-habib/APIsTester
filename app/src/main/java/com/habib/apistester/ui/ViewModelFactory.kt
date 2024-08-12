package com.habib.apistester.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.habib.apistester.AppClass
import com.habib.apistester.repository.HistoryRepository
import com.habib.apistester.repository.HomeRepository
import com.habib.apistester.caching.SharedPreferencesCaching
import com.habib.apistester.networking.DefaultAPICaller
import com.habib.apistester.ui.history.HistoryViewModel
import com.habib.apistester.ui.home.HomeViewModel

class ViewModelFactory() : ViewModelProvider.Factory {
	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		val localDataStore = SharedPreferencesCaching(
			AppClass.app.sharedPreferences
		)
		if (modelClass.isAssignableFrom(HomeViewModel::class.java))
			return HomeViewModel(
				HomeRepository(localDataStore, DefaultAPICaller())
			) as T
		else if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
			return HistoryViewModel(HistoryRepository(localDataStore)) as T
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}