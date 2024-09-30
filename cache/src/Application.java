import main.java.com.concurrency.cache.core.Cache;
import main.java.com.concurrency.cache.core.LFUCache;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        Cache<Integer, Integer> lfuCache = new LFUCache<>(5);

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
            threads.add(new Thread(() -> lfuCache.put(finalI/10, finalI)));
            threads.add(new Thread(() -> lfuCache.get(finalI/10)));
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }

        System.out.println("Cache size="+lfuCache.size());
    }
}