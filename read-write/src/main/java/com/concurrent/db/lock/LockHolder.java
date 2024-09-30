package src.main.java.com.concurrent.db.lock;

public interface LockHolder {
  boolean acquireReadLock(String tableName);
  void releaseReadLock(String tableName);
  boolean acquireWriteLock(String tableName);
  void releaseWriteLock(String tableName);
}
