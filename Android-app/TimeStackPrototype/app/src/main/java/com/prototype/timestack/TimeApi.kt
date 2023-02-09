package com.prototype.timestack

import com.google.gson.annotations.SerializedName

/**
 * Time api
 * This class is used to store the data received from the Time API
 * @property message A string representing the message received from the API
 * @constructor Creates an empty TimeApi object
 */

data class TimeApi(
    @SerializedName("message") val message: String,
)





