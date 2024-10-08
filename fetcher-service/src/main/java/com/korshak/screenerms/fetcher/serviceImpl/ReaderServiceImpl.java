package com.korshak.screenerms.fetcher.serviceImpl;


import com.korshak.screenerms.dto.SharePriceDTO;
import com.korshak.screenerms.fetcher.service.ReaderService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReaderServiceImpl implements ReaderService {

  // @Value("${downloader.storage-save-all-url}")
  // private String storagePricesBetweenUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public List<SharePriceDTO> getSharePricesBetweenDates(String ticker, LocalDateTime startDate,
                                                        LocalDateTime endDate) {

    return null;
  }
}
