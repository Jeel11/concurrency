package main.java.com.concurrency.cache.core;

import main.java.com.kv.store.dataStructures.DoublyLinkedList;
import main.java.com.kv.store.dataStructures.LinkedList;
import main.java.com.kv.store.model.KeyValueNode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> implements Cache<K, V>{
    private final Map<K, KeyValueNode<K, V>> keyValueMap;
    private Map<K, ReentrantLock> keyLevelLock;
    private int capacity;
    private LinkedList linkedList;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyValueMap = new ConcurrentHashMap<>(capacity);
        this.keyLevelLock = new ConcurrentHashMap<>();
        this.linkedList = new DoublyLinkedList();
    }

    @Override
    public void put(K key, V value) {
        keyLevelLock.computeIfAbsent(key, k -> new ReentrantLock());
        if (!keyValueMap.containsKey(key) && keyValueMap.size() >= capacity) {
            synchronized (keyValueMap) {
                evictKeysIfNecessary();
            }
        }
        synchronized (keyLevelLock.get(key)) {
            keyValueMap.computeIfPresent(key, (k, v) -> {
                linkedList.removeNode(v);
                v.setValue(value);
                v.setTimestamp(getTimestamp());
                linkedList.addLast(v);
                return v;
            });
            keyValueMap.computeIfAbsent(key, k -> {
                KeyValueNode<K, V> keyValueNode = new KeyValueNode<>(k, value, getTimestamp());
                linkedList.addLast(keyValueNode);
                return keyValueNode;
            });
            System.out.println("PUT: k="+key+", v="+value);
        }
    }

    @Override
    public V get(K key) {
        keyLevelLock.computeIfAbsent(key, k -> new ReentrantLock());
        synchronized (keyLevelLock.get(key)) {
            if (keyValueMap.containsKey(key)) {
                keyValueMap.computeIfPresent(key, (k, v) -> {
                    linkedList.removeNode(v);
                    linkedList.addLast(v);
                    return v;
                });
                System.out.println("GET: k="+key+", v="+keyValueMap.get(key).getValue());
                return keyValueMap.get(key).getValue();
            }
            System.out.println("GET: k="+key+", v=null");
            return null;
        }
    }

    private long getTimestamp() {
        return System.currentTimeMillis();
    }

    private void evictKeysIfNecessary() {
        while (keyValueMap.size() >= capacity && linkedList.getHead() != null) {
            KeyValueNode<K, V> keyValueNode = (KeyValueNode<K, V>) linkedList.removeFirst();
            if (keyValueNode != null) {
                synchronized (keyLevelLock.get(keyValueNode.getKey())) {
                    keyValueMap.remove(keyValueNode.getKey());
                    System.out.println("Evicted: k="+keyValueNode.getKey());
                }
            }
        }
    }

    public int size() {
        return keyValueMap.size();
    }
}
