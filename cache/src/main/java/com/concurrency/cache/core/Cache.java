package main.java.com.concurrency.cache.core;

public interface Cache<K, V> {
    void put(K key, V value);
    V get(K key);
    int size();
}
