import main.java.com.concurrency.cache.core.Cache;
import main.java.com.concurrency.cache.core.LRUCache;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        Cache<Integer, Integer> lruCache = new LRUCache<>(5);

//        lruCache.put("k1", "A");
//        lruCache.put("k2", "B");
//        lruCache.put("k3", "C");
//        lruCache.get("k1");
//        lruCache.put("k4", "D");
//        lruCache.put("k5", "E");
//        lruCache.put("k6", "F");
//        lruCache.get("k3");
//        lruCache.put("k7", "G");

        List<Thread> threads = new ArrayList<>();

        for(int i=1;i<=50;i++) {
            int finalI = i;
            threads.add(new Thread(() -> lruCache.put(finalI, finalI)));
            threads.add(new Thread(() -> System.out.println("GET: k="+finalI+", v="+lruCache.get(finalI))));
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }

        System.out.println("Cache size="+lruCache.size());
    }
}