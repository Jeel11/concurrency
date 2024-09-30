package src.main.java.com.concurrent.db.lock;

public class LockInfo {
  private int readCount;
  private boolean writeLock;

  public int getReadCount() {
    return readCount;
  }

  public void setReadCount(int readCount) {
    this.readCount = readCount;
  }

  public boolean isWriteLock() {
    return writeLock;
  }

  public void setWriteLock(boolean writeLock) {
    this.writeLock = writeLock;
  }
}
