package main.java.com.kv.store.dataStructures;

public class ListNode {
  protected ListNode next;
  protected ListNode prev;

  public ListNode() {
    this.next = null;
    this.prev = null;
  }

  public ListNode getNext() {
    return next;
  }

  public void setNext(ListNode next) {
    this.next = next;
  }

  public ListNode getPrev() {
    return prev;
  }

  public void setPrev(ListNode prev) {
    this.prev = prev;
  }
}
