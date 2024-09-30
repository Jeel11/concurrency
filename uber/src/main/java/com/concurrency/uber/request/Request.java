package main.java.com.concurrency.uber.request;

import main.java.com.concurrency.uber.UberService;
import main.java.com.concurrency.uber.model.UserType;

public class Request extends Thread {

  private UserType userType;
  private int id;
  private boolean seated;
  private UberService uberService;
  public Request(int id, UserType userType, UberService uberService) {
    this.userType = userType;
    this.id = id;
    this.uberService = uberService;
  }

  public void seated() {
    System.out.println("User "+ id + " is seated. UserType: "+this.userType.toString());
    this.seated = true;
  }

  public UserType getUserType() {
    return userType;
  }

  public long getId() {
    return id;
  }

  @Override
  public void run() {
    try {
      this.uberService.seatRequest(this);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
