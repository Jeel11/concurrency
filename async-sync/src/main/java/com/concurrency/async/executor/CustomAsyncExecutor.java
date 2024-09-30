package main.java.com.concurrency.async.executor;

public class CustomAsyncExecutor implements AsyncExecutor {

  private Object callBack;
  public CustomAsyncExecutor(Object callback) {
    this.callBack = callback;
  }

  @Override
  public void execute(Object callback) {
    synchronized (callback) {
      System.out.println("Executing a task asynchronously...");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      callback.notify();
    }
  }

  @Override
  public void run() {
    execute(this.callBack);
  }
}
