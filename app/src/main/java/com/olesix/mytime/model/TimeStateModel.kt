package com.olesix.mytime.model

/**
 * TimeStateModel contains the states of UI (in TimeFragment)
 */
sealed class TimeStateModel {
/**
* START is starts the Chronometer
*/
data class Start(val time: Long) : TimeStateModel()
/**
* STOP is stops the Chronometer
*/
data class Stop(val time: Long) : TimeStateModel()

/**
* RESET is resets the Chronometer
*/
data class Reset(val time: Long) : TimeStateModel()

}