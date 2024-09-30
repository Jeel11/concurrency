package main.java.com.concurrency.barbershop;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarberShop extends Thread {

  private static final int NUM_OF_WAITING_CHAIRS = 10;
  private Semaphore waitingChairs;
  private final Object barber;
  private AtomicBoolean isShopClosed;

  public BarberShop(Object barber) {
    this.waitingChairs = new Semaphore(NUM_OF_WAITING_CHAIRS, true);
    this.barber = barber;
    this.isShopClosed = new AtomicBoolean(false);
  }

  public void addCustomer(int customerId) throws InterruptedException {
    if (!isShopClosed.get() && waitingChairs.tryAcquire(0, TimeUnit.SECONDS)) {
      System.out.println("Customer " + customerId +" is in waiting queue.");
      synchronized (barber) {
        barber.notify();
      }
    } else {
      System.out.println("Customer " + customerId + " left.");
    }
  }

  @Override
  public void run() {
    while (!isShopClosed.get() || waitingChairs.availablePermits() != NUM_OF_WAITING_CHAIRS) {
      while (waitingChairs.availablePermits() == NUM_OF_WAITING_CHAIRS) {
        synchronized (barber) {
          try {
            System.out.println("Barber goes to sleep");
            barber.wait();
            System.out.println("Barber wakes up");
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

      waitingChairs.release(1);
      System.out.println("Barber is busy");
      try {
        Thread.sleep((long) (Math.random() * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void close() {
    this.isShopClosed.set(true);
  }
}
