package main.java.com.kv.store.dataStructures;

public interface LinkedList {
  void addLast(ListNode value);
  void removeNode(ListNode value);
  ListNode getHead();
  Object getLock();
  ListNode removeFirst();
}
