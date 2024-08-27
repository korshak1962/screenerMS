package com.korshak.screenerms.fetcher.service;

import com.korshak.screenerms.dto.SharePriceDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ReaderService {
  List<SharePriceDTO> getSharePricesBetweenDates(String ticker, LocalDateTime startDate,
                                                 LocalDateTime endDate);
}
