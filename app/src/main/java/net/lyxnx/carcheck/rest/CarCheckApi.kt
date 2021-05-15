package net.lyxnx.carcheck.rest

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import net.lyxnx.carcheck.rest.response.ScrapeResponse
import net.lyxnx.simplerest.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class CarCheckApi : ApiInterface<RestApi> {

    override fun build(): RestApi {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.scrapestack.com/")
                .addConverterFactory(ScrapeResponseConverter.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .client(OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .callTimeout(60, TimeUnit.SECONDS).build())
                .build()
        return retrofit.create(RestApi::class.java)
    }

    private class ScrapeResponseConverter : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
            return ScrapeResponseBodyConverter.INSTANCE
        }

        companion object {
            fun create(): ScrapeResponseConverter {
                return ScrapeResponseConverter()
            }
        }
    }

    private class ScrapeResponseBodyConverter : Converter<ResponseBody, ScrapeResponse> {

        override fun convert(value: ResponseBody): ScrapeResponse {
            val string = value.string()

            return try {
                Gson().fromJson(string, ScrapeResponse::class.java).apply {
                    isSuccess = false
                }
            } catch (ex: JsonSyntaxException) {
                ScrapeResponse().apply {
                    html = string
                    isSuccess = true
                }
            }
        }

        companion object {
            val INSTANCE = ScrapeResponseBodyConverter()
        }
    }
}