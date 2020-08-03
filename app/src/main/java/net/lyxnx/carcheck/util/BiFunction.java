package net.lyxnx.carcheck.util;

/**
 * Copy of {@link io.reactivex.rxjava3.functions.BiFunction} but without the throwable
 */
@FunctionalInterface
public interface BiFunction<T1, T2, R> extends io.reactivex.rxjava3.functions.BiFunction<T1, T2, R> {

    R apply(T1 t1, T2 t2);
}