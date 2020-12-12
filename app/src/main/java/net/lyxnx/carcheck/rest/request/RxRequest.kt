package net.lyxnx.carcheck.rest.request

import io.reactivex.rxjava3.core.Observable

interface RxRequest<T> {
    val observable: Observable<T>
}