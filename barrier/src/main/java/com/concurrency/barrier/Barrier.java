package main.java.com.concurrency.barrier;

public interface Barrier {
  void await() throws InterruptedException;
  void setBarrierAction(Runnable barrierAction);
}
