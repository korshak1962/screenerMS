package com.korshak.screenerms.downloader.service;

public interface DownLoaderService {
    int downLoadData(String timeSeriesLabel, String ticker, String interval, String month);
}
