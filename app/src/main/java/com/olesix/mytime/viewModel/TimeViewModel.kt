package com.olesix.mytime.viewModel

import android.os.SystemClock
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

    private var difference: Long = 0
    private var time: Long = 0
    private var isRunning: Boolean = false

    fun start() : Boolean {
        return if (!isRunning) {
            time = difference + SystemClock.elapsedRealtime()
            val updatedState = TimeStateModel.Start(time)
            uiTimeLiveData.postValue(updatedState)
            difference = 0
            isRunning = true
            false
        } else true

    }

    fun stop() {
        if (difference == 0L && time != 0L) {
            difference = time - SystemClock.elapsedRealtime()
        }
        val updatedState = TimeStateModel.Stop(difference)
        uiTimeLiveData.postValue(updatedState)
        isRunning = false
    }

    fun reset() {
        difference = 0
        time = 0
        isRunning = false
        val updatedState = TimeStateModel.Reset(SystemClock.elapsedRealtime())
        uiTimeLiveData.postValue(updatedState)
    }
}