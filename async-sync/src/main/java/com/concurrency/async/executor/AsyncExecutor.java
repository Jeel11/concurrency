package main.java.com.concurrency.async.executor;

public interface AsyncExecutor extends Runnable {
  void execute(Object callback);
}
