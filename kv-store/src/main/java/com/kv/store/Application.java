package main.java.com.kv.store;

import java.util.ArrayList;
import java.util.List;
import main.java.com.kv.store.core.TimeBasedKVStore;
import main.java.com.kv.store.provider.TimeProvider;

public class Application {

  public static void main(String[] args) throws InterruptedException {
    TimeBasedKVStore kvStore = new TimeBasedKVStore(5L, new TimeProvider());

    List<Thread> threads = new ArrayList<>();

    for (int i=1;i<=50;i++) {
      int finalI = i;
      threads.add(new Thread(() -> kvStore.put("foo", finalI)));
      threads.add(new Thread(() -> kvStore.put("bar", finalI)));
      threads.add(new Thread(() -> kvStore.get("foo")));
      threads.add(new Thread(() -> kvStore.get("bar")));
      threads.add(new Thread(kvStore::getAverage));
    }

    int index = 0;
    for (Thread t: threads) {
      t.start();
      index++;
      if (index%12 == 0) {
        Thread.sleep(1000L);
      }
    }

    for (Thread t: threads) {
      t.join();
    }

    kvStore.close();
  }
}
