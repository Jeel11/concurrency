package main.java.com.concurrency.barrier;

import java.util.concurrent.atomic.AtomicInteger;

public class SharedCounter {

  private AtomicInteger count;
  private final Barrier barrier;
  private boolean counterStopped;
  public SharedCounter(int target, Barrier barrier) {
    count = new AtomicInteger();
    this.barrier = barrier;
    this.barrier.setBarrierAction(() -> {
      if (count.get() >= target) {
        this.counterStopped = true;
      }
    });
  }

  public void incrementCount() throws InterruptedException {
    while (!counterStopped) {
      synchronized (barrier) {
        int c = count.incrementAndGet();
        System.out.println("count = "+c);
      }
      barrier.await();
    }
  }

  private void stopCounter() {
    this.counterStopped = true;
  }
}
