package main.java.com.kv.store.core;

public interface KVStore<K, V> {
  void put(K key, V value);
  V get(K key);
}
