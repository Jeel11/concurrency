package src.main.java.com.concurrent.db.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockHolderImpl implements LockHolder {

  private final Map<String, LockInfo> tableNameToLockInfo;
  private static volatile LockHolderImpl instance;

  private LockHolderImpl() {
    this.tableNameToLockInfo = new ConcurrentHashMap<>();
  }

  public static LockHolderImpl getInstance() {
    if (instance == null) {
      synchronized (LockHolderImpl.class) {
        if (instance == null) {
          instance = new LockHolderImpl();
        }
      }
    }
    return instance;
  }

  @Override
  public boolean acquireReadLock(String tableName) {
    LockInfo lockInfo = tableNameToLockInfo.computeIfAbsent(tableName, k -> new LockInfo());
    synchronized (lockInfo) {
      if (lockInfo.isWriteLock()) {
        try {
          lockInfo.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      lockInfo.setReadCount(lockInfo.getReadCount() + 1);
      tableNameToLockInfo.put(tableName, lockInfo);
      System.out.println("Acquired read lock on "+ tableName + ". Read count = " + lockInfo.getReadCount());
      return true;
    }
  }

  @Override
  public void releaseReadLock(String tableName) {
    LockInfo lockInfo = tableNameToLockInfo.get(tableName);
    if (lockInfo != null) {
      synchronized (lockInfo) {
        if (lockInfo.getReadCount() > 0) {
          lockInfo.setReadCount(lockInfo.getReadCount() - 1);
          System.out.println("Released read lock on "+ tableName + ". Read count = "+ lockInfo.getReadCount());
        }
        if (lockInfo.getReadCount() == 0) {
          lockInfo.notifyAll();
        }
      }
    }
  }

  @Override
  public boolean acquireWriteLock(String tableName) {
    LockInfo lockInfo = tableNameToLockInfo.computeIfAbsent(tableName, k -> new LockInfo());
    synchronized (lockInfo) {
      if (lockInfo.getReadCount() > 0 || lockInfo.isWriteLock()) {
        try {
          lockInfo.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      lockInfo.setWriteLock(true);
      System.out.println("Acquired write lock on "+ tableName);
      return true;
    }
  }

  @Override
  public void releaseWriteLock(String tableName) {
    LockInfo lockInfo = tableNameToLockInfo.get(tableName);
    if (lockInfo != null) {
      synchronized (lockInfo) {
        if (lockInfo.isWriteLock()) {
          lockInfo.setWriteLock(false);
          System.out.println("Released lock on "+ tableName);
          lockInfo.notifyAll();
        }
      }
    }
  }
}
