package net.lyxnx.carcheck.rest.response

import com.google.gson.annotations.SerializedName

class ScrapeResponse {
    var html: String? = null

    @SerializedName("success")
    var isSuccess = false

    @SerializedName("error")
    var error: ScrapeError? = null

    class ScrapeError {
        @SerializedName("code")
        var code = 0

        @SerializedName("type")
        var message: String? = null
    }
}