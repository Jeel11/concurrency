package main.java.com.concurrency.uber;

import java.util.ArrayList;
import java.util.List;
import main.java.com.concurrency.uber.model.UserType;
import main.java.com.concurrency.uber.request.Request;

public class Application {

  public static void main(String[] args) {

    UberService uberService = new UberService();

    int n = 11;
    List<Thread> requests = new ArrayList<>();
    for (int i=0;i<n;i++) {
      requests.add(new Request(i, UserType.REPUBLICAN, uberService));
      requests.add(new Request(i + n, UserType.DEMOCRAT, uberService));
    }

    for (Thread r: requests) {
      r.start();
    }

    for (Thread r: requests) {
      try {
        r.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
