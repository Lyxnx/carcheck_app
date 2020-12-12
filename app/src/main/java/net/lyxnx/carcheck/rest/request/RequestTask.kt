package net.lyxnx.carcheck.rest.request

import android.content.Context
import io.reactivex.rxjava3.core.Observable

abstract class RequestTask<T>(private val context: Context) : RxRequest<T> {

    override val observable: Observable<T> by lazy {
        buildObservable(context)
    }

    protected abstract fun buildObservable(context: Context): Observable<T>
}