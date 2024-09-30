package main.java.com.concurrency.cache.core;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import main.java.com.concurrency.cache.core.model.FreqListNode;
import main.java.com.kv.store.dataStructures.DoublyLinkedList;
import main.java.com.kv.store.model.KeyValueNode;

/**
 * Implement a key-value cache using LFU eviction strategy
 * Requirement:
 * - Evict the key-value pair having the least frequency value.
 * - If there are multiple key-value pairs having the least frequency value, then fallback to LRU policy.
 * */
public class LFUCache<K, V> implements Cache<K, V> {
  private final Map<K, KeyValueNode<K, V>> keyValueMap;
  private final Map<K, ReentrantLock> keyLevelLocks;

  private final Map<Integer, FreqListNode<DoublyLinkedList>> freqListNodeMap;
  private final DoublyLinkedList freqLinkedList;
  private final int capacity;

  public LFUCache(int capacity) {
    this.capacity = capacity;
    this.keyValueMap = new ConcurrentHashMap<>(capacity);
    this.keyLevelLocks = new ConcurrentHashMap<>();
    this.freqListNodeMap = new ConcurrentHashMap<>();

    this.freqLinkedList = new DoublyLinkedList();
  }

  @Override
  public synchronized void put(K key, V value) {
    if (!keyValueMap.containsKey(key) && size() >= capacity) {
      evictKeysIfNecessary();
    }
    keyValueMap.computeIfPresent(key, (k, v) -> {
      v.setValue(value);
      int newFreq = v.incrementFreq();
      v.setTimestamp(getTimestamp());
      // remove node from old freq list
      freqListNodeMap.get(newFreq - 1).getList().removeNode(v);
      // add node to new freq list
      freqListNodeMap.computeIfAbsent(newFreq, f -> {
        FreqListNode<DoublyLinkedList> freqListNode = new FreqListNode<>(newFreq, new DoublyLinkedList());
        freqLinkedList.insertNodeAfter(freqListNodeMap.get(newFreq - 1), freqListNode);
        return freqListNode;
      });
      freqListNodeMap.get(newFreq).getList().addLast(v);
      // remove old freq node from the linked list if there are no value nodes attached to it
      if (Optional.ofNullable(freqListNodeMap.get(newFreq - 1).getList()).map(DoublyLinkedList::getHead).isEmpty()) {
        freqLinkedList.removeNode(freqListNodeMap.get(newFreq - 1));
        freqListNodeMap.remove(newFreq - 1);
      }
      return v;
    });
    keyValueMap.computeIfAbsent(key, k -> {
      KeyValueNode<K, V> keyValueNode = new KeyValueNode<>(k, value, getTimestamp());
      // Initial freq will be 1
      freqListNodeMap.computeIfAbsent(1, f -> {
        FreqListNode<DoublyLinkedList> freqListNode = new FreqListNode<>(f, new DoublyLinkedList());
        freqLinkedList.addFirst(freqListNode);
        return freqListNode;
      });
      freqListNodeMap.get(1).getList().addLast(keyValueNode);

      return keyValueNode;
    });
    System.out.println("PUT: k="+key+", v="+value+", T="+getTimestamp());
  }

  @Override
  public synchronized V get(K key) {
    if (keyValueMap.containsKey(key)) {
      int newFreq = keyValueMap.get(key).incrementFreq();
      keyValueMap.get(key).setTimestamp(getTimestamp());

      // remove node from old freq list
      freqListNodeMap.get(newFreq - 1).getList().removeNode(keyValueMap.get(key));
      // add node to new freq list
      freqListNodeMap.computeIfAbsent(newFreq, f -> {
        FreqListNode<DoublyLinkedList> freqListNode = new FreqListNode<>(newFreq, new DoublyLinkedList());
        freqLinkedList.insertNodeAfter(freqListNodeMap.get(newFreq - 1), freqListNode);
        return freqListNode;
      });
      freqListNodeMap.get(newFreq).getList().addLast(keyValueMap.get(key));
      // remove old freq node from the linked list if there are no value nodes attached to it
      if (Optional.ofNullable(freqListNodeMap.get(newFreq - 1).getList()).map(DoublyLinkedList::getHead).isEmpty()) {
        freqLinkedList.removeNode(freqListNodeMap.get(newFreq - 1));
        freqListNodeMap.remove(newFreq - 1);
      }
      System.out.println("GET: k="+key+", v="+keyValueMap.get(key).getValue()+", T="+getTimestamp());
      return keyValueMap.get(key).getValue();
    }
    System.out.println("GET: k="+key+", v=null, T="+getTimestamp());
    return null;
  }

  @Override
  public int size() {
    return keyValueMap.size();
  }

  private long getTimestamp() {
    return System.currentTimeMillis();
  }

  private void evictKeysIfNecessary() {
    while (size() >= capacity && freqLinkedList.getHead() != null) {
      FreqListNode<DoublyLinkedList> minFreqNode = (FreqListNode<DoublyLinkedList>) freqLinkedList.getHead();
      if (Optional.ofNullable(minFreqNode.getList()).map(DoublyLinkedList::getHead).isPresent()) {
        KeyValueNode<K, V> keyNode = (KeyValueNode<K, V>) minFreqNode.getList().removeFirst();
        keyValueMap.remove(keyNode.getKey());
        System.out.println("Evicted: k="+keyNode.getKey()+", f="+minFreqNode.getFreq());
      }
      if (Optional.ofNullable(minFreqNode.getList()).map(DoublyLinkedList::getHead).isEmpty()) {
        freqLinkedList.removeNode(minFreqNode);
        freqListNodeMap.remove(minFreqNode.getFreq());
      }
    }
  }
}
