package net.lyxnx.carcheck.rest.request

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import net.lyxnx.carcheck.rest.RestApi
import net.lyxnx.carcheck.rest.Singletons
import net.lyxnx.carcheck.rest.parser.VehicleHtmlParser
import net.lyxnx.carcheck.rest.response.GetVehicleResponse
import net.lyxnx.carcheck.rest.response.ScrapeResponse
import org.jsoup.Jsoup

class GetVehicleTask(context: Context, private val vrm: String) : ApiRequestTask<GetVehicleResponse>(context) {

    override fun buildObservable(context: Context, api: RestApi): Observable<GetVehicleResponse> {
        return api.scrapeUrl(Singletons.apiKey, "https://totalcarcheck.co.uk/FreeCheck?regno=$vrm")
                .flatMap { scrapeResponse: ScrapeResponse ->
                    val response = GetVehicleResponse()

                    if (!scrapeResponse.isSuccess) {
                        return@flatMap Observable.just(response.apply {
                            success = false
                            error = scrapeResponse.error!!.message
                        })
                    }

                    val body = Jsoup.parse(scrapeResponse.html)
                    val parsedVehicle = VehicleHtmlParser.parseVehicle(body)

                    return@flatMap Observable.just(GetVehicleResponse().apply {
                        when (parsedVehicle) {
                            is VehicleHtmlParser.ParseStatus.Success -> {
                                success = true
                                vehicle = parsedVehicle.vehicle
                            }
                            is VehicleHtmlParser.ParseStatus.Error -> {
                                success = false
                                error = parsedVehicle.error
                            }
                        }
                    })
                }
    }
}