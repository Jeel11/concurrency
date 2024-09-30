package main.java.com.concurrency.async;

import main.java.com.concurrency.async.executor.CustomAsyncExecutor;

public class Application {

  public static void main(String[] args) throws InterruptedException {

    Object callBack = new Object();
    CustomAsyncExecutor asyncExecutor = new CustomAsyncExecutor(callBack);

    Thread t1 = new Thread(() -> asyncExecutor.execute(callBack));

    t1.start();

    synchronized (callBack) {
      callBack.wait();
      System.out.println("Execution Completed");
    }
  }
}
