package com.example.multi_datasource_demo;

import org.springframework.util.Assert;

public class DataSourceContextHolder {
  private static final ThreadLocal<String> context = new ThreadLocal<>();

  public static void setDataSource(String datasourceKey) {
    Assert.notNull(datasourceKey, "datasourceKeyがnullになっています。");
    context.set(datasourceKey);
  }

  public static String getDataSource() {
    return context.get();
  }

  public static void clearDataSource() {
    context.remove();
  }
}
