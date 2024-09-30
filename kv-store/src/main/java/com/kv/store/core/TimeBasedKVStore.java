package main.java.com.kv.store.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import main.java.com.kv.store.dataStructures.DoublyLinkedList;
import main.java.com.kv.store.dataStructures.ListNode;
import main.java.com.kv.store.model.Average;
import main.java.com.kv.store.model.KeyValueNode;
import main.java.com.kv.store.provider.TimeProvider;

public class TimeBasedKVStore implements KVStore<String, Integer> {

  private Map<String, KeyValueNode<String, Integer>> keyValueMap;
  private final Map<String, ReentrantLock> keyLevelLock;
  private DoublyLinkedList linkedList;
  private final TimeProvider timeProvider;
  private final Average overallAverage;
  private long expiryWindow;

  public TimeBasedKVStore(long expiryWindow, TimeProvider timeProvider) {
    this.expiryWindow = expiryWindow;
    this.keyValueMap = new ConcurrentHashMap<>();
    this.keyLevelLock = new ConcurrentHashMap<>();
    this.linkedList = new DoublyLinkedList();
    this.timeProvider = new TimeProvider();
    this.overallAverage = new Average(0, 0);

    this.timeProvider.start();
  }

  @Override
  public void put(String key, Integer value) {
    keyLevelLock.putIfAbsent(key, new ReentrantLock());
    synchronized (keyLevelLock.get(key)) {
      keyValueMap.computeIfAbsent(key, k -> {
        KeyValueNode<String, Integer> keyValueNode = new KeyValueNode<>(key, value, getTimestamp());
        linkedList.addLast(keyValueNode);

        // Updating average
        overallAverage.incrementTotalCountBy(value);
        overallAverage.incrementRowsCountBy(1);
        return keyValueNode;
      });
      keyValueMap.computeIfPresent(key, (k, v) -> {
        int oldValue = v.getValue();
        linkedList.removeNode(v);
        v.setValue(value);
        v.setTimestamp(getTimestamp());
        linkedList.addLast(v);

        // Updating average
        overallAverage.incrementTotalCountBy(value - oldValue);
        return v;
      });
      System.out.println("PUT: k="+key+", v="+value+", T="+getTimestamp());
    }
  }

  @Override
  public Integer get(String key) {
    keyLevelLock.putIfAbsent(key, new ReentrantLock());
    synchronized (keyLevelLock.get(key)) {
      KeyValueNode<String, Integer> keyValueNode = keyValueMap.get(key);
      if (keyValueNode == null) {
        System.out.println("GET: k="+key+", v=-1, T="+getTimestamp());
        return -1;
      }
      if (keyValueNode.getTimestamp() <= getTimestamp() - expiryWindow) {
        evict(key);
        System.out.println("GET: k="+key+", v=-1, T="+getTimestamp());
        return -1;
      }
      System.out.println("GET: k="+key+", v="+keyValueNode.getValue()+", T="+getTimestamp());
      return keyValueNode.getValue();
    }
  }

  public double getAverage() {
    synchronized (linkedList.getLock()) {
      ListNode listNode = linkedList.getHead();
      while (listNode != null) {
        ListNode next = listNode.getNext();
        if (listNode instanceof KeyValueNode<?,?>) {
          KeyValueNode<String, Integer> keyValueNode = (KeyValueNode<String, Integer>) listNode;
//          System.out.println(keyValueNode.getTimestamp());
          if (keyValueNode.getTimestamp() <= getTimestamp() - expiryWindow) {
            evict(keyValueNode.getKey());
          } else {
            break;
          }
        }
        listNode = next;
      }
      System.out.println("AVG:"+overallAverage.getAverage()+", T="+getTimestamp()+" ,total="+overallAverage.getTotalCount()+" ,rows="+overallAverage.getRowsCount());
      return overallAverage.getAverage();
    }
  }

  private long getTimestamp() {
    return timeProvider.getTime();
  }

  private void evict(String key) {
    synchronized (keyLevelLock.get(key)) {
      KeyValueNode<String, Integer> keyValueNode = keyValueMap.remove(key);
      linkedList.removeNode(keyValueNode);

      // Updating average
      overallAverage.incrementTotalCountBy(-keyValueNode.getValue());
      overallAverage.incrementRowsCountBy(-1);
    }
  }

  public void close() throws InterruptedException {
    timeProvider.stopClock();
    timeProvider.join();
  }
}
