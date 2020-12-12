package net.lyxnx.carcheck.rest

object Singletons {

    val api: RestApi by lazy {
        ApiInterface.buildApiInterface()
    }

    var apiKey: String? = null
}