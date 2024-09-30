package src.main.java.com.concurrent.db;

import src.main.java.com.concurrent.db.lock.LockHolder;
import src.main.java.com.concurrent.db.lock.LockHolderImpl;

/**
 * Pitfalls:
 * - Writer thread can run into starvation if there are multiple incoming read requests.
 * As long as a new reader arrives before the last reader departs, there will always be
 * at least one reader in the room. To avoid this situation we can add a mutex for the
 * readers & allow writers to lock it.
 * */
public class Application {

  public static void main(String[] args) throws InterruptedException {

    String table1 = "table1";
    String table2 = "table2";

    LockHolder lockHolder = LockHolderImpl.getInstance();
    Thread t1 = new Thread(() -> {
      // Acquire read locks
      lockHolder.acquireReadLock(table1);
      lockHolder.acquireReadLock(table1);
      lockHolder.acquireReadLock(table1);
      lockHolder.acquireReadLock(table2);

      // Release read locks
      lockHolder.releaseReadLock(table1);
      lockHolder.releaseReadLock(table1);
      lockHolder.releaseReadLock(table1);
      lockHolder.releaseReadLock(table2);
    });

    Thread t2 = new Thread(() -> {
      lockHolder.acquireWriteLock(table1);
      lockHolder.releaseWriteLock(table1);

    });

    Thread t3 = new Thread(() -> {
      lockHolder.acquireWriteLock(table2);
      lockHolder.releaseWriteLock(table2);
    });

    t1.start();
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();
  }
}
