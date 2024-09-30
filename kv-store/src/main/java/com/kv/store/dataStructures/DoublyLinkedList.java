package main.java.com.kv.store.dataStructures;

import java.util.concurrent.locks.ReentrantLock;

public class DoublyLinkedList implements LinkedList {

  private volatile ListNode head;
  private volatile ListNode tail;
  private final ReentrantLock lock;

  public DoublyLinkedList() {
    this.head = null;
    this.tail = null;
    this.lock = new ReentrantLock();
  }

  @Override
  public void addFirst(ListNode value) {
    synchronized (lock) {
      if (head == null) {
        head = value;
        tail = value;
      } else {
        value.setNext(head);
        head.setPrev(value);
        head = head.getPrev();
      }
    }
  }

  @Override
  public void addLast(ListNode value) {
    synchronized (lock) {
      if (tail == null) {
        tail = value;
        head = value;
      } else {
        tail.setNext(value);
        value.setPrev(tail);
        tail = tail.getNext();
      }
    }
  }

  @Override
  public void removeNode(ListNode value) {
    synchronized (lock) {
      ListNode prev = value.getPrev();
      ListNode next = value.getNext();

      if (prev != null) {
        prev.setNext(next);
      }

      if (next != null) {
        next.setPrev(prev);
      }

      if (head == value) {
        head = head.getNext();
        if (head != null) {
          head.setPrev(null);
        }
      }

      if (tail == value) {
        tail = tail.getPrev();
        if (tail != null) {
          tail.setNext(null);
        }
      }

      value.setNext(null);
      value.setPrev(null);
    }
  }

  public ReentrantLock getLock() {
    return lock;
  }

  public ListNode getHead() {
    synchronized (lock) {
      return head;
    }
  }

  public ListNode removeFirst() {
    if (head != null) {
      synchronized (lock) {
        if (head != null) {
          ListNode temp = head;
          removeNode(head);
          return temp;
        }
      }
    }
    return null;
  }

  @Override
  public void insertNodeAfter(ListNode prevNode, ListNode nextNode) {
    if (prevNode != null && nextNode != null) {
      synchronized (lock) {
        ListNode next = prevNode.getNext();
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        nextNode.setNext(next);
      }
    }
  }
}
