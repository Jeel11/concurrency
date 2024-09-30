package main.java.com.concurrency.philosophers;

import java.util.concurrent.atomic.AtomicBoolean;

public class Fork {
  private AtomicBoolean isAvailable;

  public boolean isAvailable() {
    return isAvailable.get();
  }

  public void setAvailable(boolean available) {
    isAvailable.set(available);
  }
}
