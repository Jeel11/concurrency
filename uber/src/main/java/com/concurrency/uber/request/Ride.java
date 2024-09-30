package main.java.com.concurrency.uber.request;

import main.java.com.concurrency.uber.model.RideStatus;
import main.java.com.concurrency.uber.model.UserType;

public class Ride {

  private int id;
  private RideStatus rideStatus;
  private Integer numOfRepublicans = 0;
  private Integer numOfDemocrats = 0;
  public Ride(int id) {
    this.id = id;
    this.rideStatus = RideStatus.NOT_STARTED;
  }

  private boolean isFull() {
    return (numOfRepublicans + numOfDemocrats) == 4;
  }

  public int getId() {
    return id;
  }

}
