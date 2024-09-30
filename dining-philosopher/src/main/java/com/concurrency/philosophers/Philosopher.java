package main.java.com.concurrency.philosophers;

public class Philosopher implements Dining {

  private final Fork leftFork;
  private final Fork rightFork;
  private boolean finishEating;
  private int id;
  public Philosopher(int id, Fork leftFork, Fork rightFork) {
    this.leftFork = leftFork;
    this.rightFork = rightFork;
    this.finishEating = false;
    this.id = id;
  }

  @Override
  public void eat() {
    finishEating = false;
    while (!finishEating) {
      try {
        Thread.sleep((long) (Math.random() * 100));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (leftFork) {
        synchronized (rightFork) {
          System.out.println(id + " is eating");
          try {
            Thread.sleep((long) (Math.random() * 100));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public Fork getLeftFork() {
    return leftFork;
  }

  public Fork getRightFork() {
    return rightFork;
  }

  public void finishEating() {
    this.finishEating = true;
  }

  @Override
  public void run() {
    eat();
  }
}
