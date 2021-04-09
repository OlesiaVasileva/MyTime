package com.olesix.mytime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.olesix.mytime.model.TimeStateModel

/**
 * TimeViewModel is viewModel that updates the TimeFragment (the visible UI),
 * gets the data from model.
 */
class TimeViewModel: ViewModel() {

    /**
     * Create MutableLiveData which TimeFragment can subscribe to.
     * When this data changes, it triggers the UI to do an update.
     */
    val uiTimeLiveData = MutableLiveData<TimeStateModel>()

    /**
     * Get the updated states from our model and post the values to TimeFragment
     */
    fun start() {
        val updatedState = TimeStateModel.START
        uiTimeLiveData.postValue(updatedState)
    }

    fun stop() {
        val updatedState = TimeStateModel.STOP
        uiTimeLiveData.postValue(updatedState)
    }

    fun reset() {
        val updatedState = TimeStateModel.RESET
        uiTimeLiveData.postValue(updatedState)
    }
}