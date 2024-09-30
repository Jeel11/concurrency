package main.java.com.concurrency.barrier;

import java.util.concurrent.locks.ReentrantLock;

public class CustomCyclicBarrier implements Barrier {

  private int parties;
  private Runnable barrierAction;
  private final ReentrantLock lock;
  private int partiesWaiting = 0;
  public CustomCyclicBarrier(int parties) {
    this.parties = parties;
    this.lock = new ReentrantLock();
  }

  @Override
  public void await() throws InterruptedException {
    synchronized (lock) {
      partiesWaiting++;
      if (partiesWaiting >= parties) {
        if (barrierAction != null) {
          barrierAction.run();
          System.out.println("Running barrier action");
        }
        this.partiesWaiting = 0;
        lock.notifyAll();
      } else {
        lock.wait();
      }
    }
  }

  @Override
  public void setBarrierAction(Runnable barrierAction) {
    this.barrierAction = barrierAction;
  }
}
