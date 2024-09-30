package main.java.com.kv.store.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Average {

  public int getTotalCount() {
    return totalCount.get();
  }

  private AtomicInteger totalCount;

  public int getRowsCount() {
    return rowsCount.get();
  }

  private AtomicInteger rowsCount;

  public Average(Integer totalCount, Integer rowsCount) {
    this.totalCount = new AtomicInteger(totalCount);
    this.rowsCount = new AtomicInteger(rowsCount);
  }

  public Double getAverage() {
    if (rowsCount.get() == 0) {
      return 0.0;
    }
    return (totalCount.get() * 100d) / (rowsCount.get() * 100d);
  }

  public void incrementRowsCountBy(int num) {
    rowsCount.addAndGet(num);
  }

  public void incrementTotalCountBy(int num) {
    totalCount.addAndGet(num);
  }
}
