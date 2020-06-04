package sk.p1ro.updater.util;

@FunctionalInterface
public interface Listener<T> {
    void onResponse(T result);
}