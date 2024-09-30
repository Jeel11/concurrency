package main.java.com.concurrency.barrier;

import java.util.ArrayList;
import java.util.List;

public class Application {

  public static void main(String[] args) throws InterruptedException {

    int NUM_THREADS = 5;
    Barrier barrier = new CustomCyclicBarrier(NUM_THREADS);

    SharedCounter sharedCounter = new SharedCounter(20, barrier);

    List<Thread> threads = new ArrayList<>();
    for (int i=1;i<=NUM_THREADS;i++) {
      Thread t = new Thread(() -> {
        try {
          sharedCounter.incrementCount();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      threads.add(t);
      t.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }
  }
}
