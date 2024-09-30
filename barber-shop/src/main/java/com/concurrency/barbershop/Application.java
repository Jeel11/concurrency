package main.java.com.concurrency.barbershop;

public class Application {

  public static void main(String[] args) throws InterruptedException {

    BarberShop barberShop = new BarberShop(new Object());

    barberShop.start();

    Thread.sleep(1000);
    int i=0;
    for (;i<12;i++) {
      barberShop.addCustomer(i+1);
    }

    Thread.sleep((long) (Math.random() * 10000));

    barberShop.addCustomer(i+1);

    barberShop.close();

    barberShop.join();
  }
}
