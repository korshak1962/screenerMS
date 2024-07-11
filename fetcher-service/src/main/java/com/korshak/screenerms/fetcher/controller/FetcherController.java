package com.korshak.screenerms.fetcher.controller;

import com.korshak.screenerms.dto.SharePriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/fetcher")
public class FetcherController {

    @Autowired
    private  RestTemplate restTemplate;
    @Value("${storage.api.between}")
    private  String storageBetweenUrl;


    @GetMapping("/between")
    public ResponseEntity<List<SharePriceDTO>> getSharePricesBetweenDates(
            @RequestParam String ticker,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

        System.out.println("=======startDate = " + startDate);
        String fullUrl = String.format("%s?ticker=%s&startDate=%s&endDate=%s",
                storageBetweenUrl, ticker, startDate, endDate);
        System.out.println("=======fullUrl = " + fullUrl);
        ResponseEntity<SharePriceDTO[]> response = restTemplate.getForEntity(fullUrl, SharePriceDTO[].class);
        List<SharePriceDTO> sharePrices = Arrays.asList(response.getBody());

        return ResponseEntity.ok(sharePrices);
    }
}