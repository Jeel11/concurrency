package main.java.com.kv.store.model;

import main.java.com.kv.store.dataStructures.ListNode;

public class KeyValueNode<K, V> extends ListNode {
  private final K key;
  private V value;
  private Long timestamp;

  public KeyValueNode(K key, V value, Long timestamp) {
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
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
}
