package com.korshak.screenerms.fetcher.serviceImpl;


import com.korshak.screenerms.dto.SharePriceDTO;
import com.korshak.screenerms.fetcher.service.ReaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReaderServiceImpl implements ReaderService {

   // @Value("${downloader.storage-save-all-url}")
   // private String storagePricesBetweenUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<SharePriceDTO> getSharePricesBetweenDates(String ticker, LocalDateTime startDate, LocalDateTime endDate) {

      return null;
    }
}
