package main.java.com.kv.store.model;

import java.util.concurrent.atomic.AtomicInteger;
import main.java.com.kv.store.dataStructures.ListNode;

public class KeyValueNode<K, V> extends ListNode {
  private final K key;
  private V value;
  private AtomicInteger freq;
  private Long timestamp;

  public KeyValueNode(K key, V value, Long timestamp) {
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
    this.freq = new AtomicInteger(1);
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public K getKey() {
    return key;
  }

  public int incrementFreq() {
    return this.freq.incrementAndGet();
  }
}
