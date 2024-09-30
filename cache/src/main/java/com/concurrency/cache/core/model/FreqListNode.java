package main.java.com.concurrency.cache.core.model;

import main.java.com.kv.store.dataStructures.LinkedList;
import main.java.com.kv.store.dataStructures.ListNode;

public class FreqListNode<L extends LinkedList> extends ListNode {
  private int freq;
  private final L list;

  public FreqListNode(int freq, L list) {
    this.list = list;
    this.freq = freq;
  }

  public int getFreq() {
    return freq;
  }

  public void setFreq(int freq) {
    this.freq = freq;
  }

  public L getList() {
    return list;
  }
}
