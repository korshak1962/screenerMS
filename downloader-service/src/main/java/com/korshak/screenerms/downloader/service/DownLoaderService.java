package com.korshak.screenerms.downloader.service;

public interface DownLoaderService {
  String downLoadData(String timeSeriesLabel, String ticker, String interval, String month);
}
