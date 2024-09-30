package main.java.com.concurrency;

public class TestClass {

  private int count=0;
  public static void main(String[] args) {
    TestClass testClass = new TestClass();
    Thread t1 = new Thread(() -> {
      for(int i=0;i<5;i++) {
        testClass.m1();
      }
    });

    Thread t2 = new Thread(() -> {
      for(int i=0;i<5;i++) {
        testClass.m2();
      }
    });

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void m1() {
    count++;
    System.out.println("Inside m1. Count = "+ count);
  }

  public synchronized void m2() {
    count++;
    System.out.println("Inside m2. Count = "+ count);
  }
}
