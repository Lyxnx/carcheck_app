package net.lyxnx.carcheck.rest;

import net.lyxnx.carcheck.rest.response.ScrapeResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {
    
    @GET("scrape")
    Observable<ScrapeResponse> scrapeUrl(@Query("access_key") String token, @Query("url") String url);
    
}