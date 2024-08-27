package com.korshak.screenerms.fetcher.controller;

import com.korshak.screenerms.dto.SharePriceDTO;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/fetcher")
public class FetcherController {
  private static final Logger logger = LoggerFactory.getLogger(FetcherController.class);

  @Autowired
  private RestTemplate restTemplate;
  @Value("${storage.api.between}")
  private String storageBetweenUrl;


  @GetMapping("/between")
  public ResponseEntity<List<SharePriceDTO>> getSharePricesBetweenDates(
      @RequestParam String ticker,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    logger.info("=======startDate = " + startDate);
    String fullUrl = String.format("%s?ticker=%s&startDate=%s&endDate=%s",
        storageBetweenUrl, ticker, startDate, endDate);
    logger.info("=======fullUrl = " + fullUrl);
    ResponseEntity<SharePriceDTO[]> response =
        restTemplate.getForEntity(fullUrl, SharePriceDTO[].class);
    List<SharePriceDTO> sharePrices = Arrays.asList(response.getBody());

    return ResponseEntity.ok(sharePrices);
  }
}