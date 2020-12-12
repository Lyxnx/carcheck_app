package net.lyxnx.carcheck.rest.request

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import net.lyxnx.carcheck.rest.RestApi
import net.lyxnx.carcheck.rest.Singletons

abstract class ApiRequestTask<T>(context: Context) : RequestTask<T>(context) {
    private val api: RestApi = Singletons.api

    override fun buildObservable(context: Context): Observable<T> = buildObservable(context, api)

    protected abstract fun buildObservable(context: Context, api: RestApi): Observable<T>
}