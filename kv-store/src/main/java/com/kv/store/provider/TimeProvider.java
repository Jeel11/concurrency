package main.java.com.kv.store.provider;

import java.util.concurrent.atomic.AtomicLong;

public class TimeProvider extends Thread {

  private AtomicLong time;
  private boolean stopCalled;
  public TimeProvider() {
    this.time = new AtomicLong();
    this.stopCalled = false;
  }

  @Override
  public void run() {
    while (!stopCalled) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      time.incrementAndGet();
    }
  }

  public void stopClock() {
    this.stopCalled = true;
  }

  public Long getTime() {
    return time.get();
  }
}
