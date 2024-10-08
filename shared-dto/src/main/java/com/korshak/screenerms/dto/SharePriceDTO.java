package com.korshak.screenerms.dto;

import java.time.LocalDateTime;

public class SharePriceDTO {
  private String ticker;
  private LocalDateTime date;
  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;

  public SharePriceDTO(String ticker, LocalDateTime date, double open, double high, double low,
                       double close, long volume) {
    this.ticker = ticker;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  public String getTicker() {
    return ticker;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public double getOpen() {
    return open;
  }

  public double getHigh() {
    return high;
  }

  public double getLow() {
    return low;
  }

  public double getClose() {
    return close;
  }

  public long getVolume() {
    return volume;
  }
}

