package main.java.com.concurrency.uber;

import java.util.concurrent.Semaphore;
import main.java.com.concurrency.uber.model.UserType;
import main.java.com.concurrency.uber.request.Request;

public class UberService {

  private static final int MAX_PEOPLE_WAITING = 4;
  private int republicanCount = 0;
  private int democraticCount = 0;

  private Semaphore republicansWaiting;
  private Semaphore democratsWaiting;

  public UberService() {
    this.republicansWaiting = new Semaphore(MAX_PEOPLE_WAITING);
    this.democratsWaiting = new Semaphore(MAX_PEOPLE_WAITING);
  }

  public void seatRequest(Request request) throws InterruptedException {
    if (request.getUserType().equals(UserType.REPUBLICAN)) {
      republicansWaiting.acquire();
    } else {
      democratsWaiting.acquire();
    }
    synchronized (this) {
      if (request.getUserType().equals(UserType.REPUBLICAN)) {
        this.republicanCount++;
      } else {
        this.democraticCount++;
      }
      boolean isLeader = false;
      if (republicanCount == 4) {
        isLeader = true;
        republicansWaiting.release(4);
        republicanCount -= 4;
      } else if (democraticCount == 4) {
        isLeader = true;
        democratsWaiting.release(4);
        democraticCount -= 4;
      } else if (republicanCount >= 2 && democraticCount >= 2) {
        isLeader = true;
        republicanCount -= 2;
        democraticCount -= 2;
        republicansWaiting.release(2);
        democratsWaiting.release(2);
      }

      request.seated();
      if (isLeader) {
        System.out.println("Uber ride on its way. Ride leader: "+ request.getId());
      }
    }
  }
}
