package main.java.com.concurrency.philosophers;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic example of circular wait
 * */
public class Application {

  public static void main(String[] args) {

    int n = 5;
    List<Philosopher> philosophers = new ArrayList();
    List<Fork> forks = new ArrayList<>();
    List<Thread> threads = new ArrayList<>();

    for(int i=0;i<n;i++) {
      forks.add(new Fork());
    }

    for (int i=0;i<n;i++) {
      Philosopher philosopher;
      if (i==0) {
        // Did this to avoid circular wait problem
        philosopher = new Philosopher(i+1, forks.get((i-1+n)%n), forks.get((i%n)));
      } else {
        philosopher = new Philosopher(i+1, forks.get((i%n)), forks.get((i-1+n)%n));
      }
      philosophers.add(philosopher);
      threads.add(new Thread(philosopher));
    }

    for (int i = 0;i<n;i++) {
      threads.get(i).start();
    }

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < n; i++) {
      philosophers.get(i).finishEating();
    }
  }
}
