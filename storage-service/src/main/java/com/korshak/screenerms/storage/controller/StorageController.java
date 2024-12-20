package com.korshak.screenerms.storage.controller;

import com.korshak.screenerms.dto.SharePriceDTO;
import com.korshak.screenerms.storage.dao.SharePrice;
import com.korshak.screenerms.storage.dao.SharePriceRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController {
  @Autowired
  private SharePriceRepository sharePriceRepository;

  @Transactional
  @PostMapping("/saveAll")
  @Transactional()
  public ResponseEntity<String> saveAll(@RequestBody List<SharePriceDTO> sharePriceDTOs) {
    List<SharePrice> sharePrices = sharePriceDTOs.stream()
        .map(this::convertToEntity)
        .collect(Collectors.toList());

    sharePriceRepository.saveAll(sharePrices);

    return ResponseEntity.ok("Successfully saved " + sharePrices.size() + " share prices");
  }

  @GetMapping("/between")
  @Transactional(readOnly=true)
  public ResponseEntity<List<SharePriceDTO>> getSharePricesBetweenDates(
      @RequestParam String ticker,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    List<SharePriceDTO> sharePrices = sharePriceRepository
        .findByTickerAndDateBetween(ticker, startDate, endDate, Pageable.unpaged())
        .stream()
        .map(ent -> new SharePriceDTO(ent.getTicker(), ent.getDate(), ent.getOpen(), ent.getHigh(),
            ent.getLow(), ent.getClose(), ent.getVolume()))
        .toList();
    return ResponseEntity.ok(sharePrices);
  }

  private SharePrice convertToEntity(SharePriceDTO dto) {
    SharePrice entity = new SharePrice();
    entity.setTicker(dto.getTicker());
    entity.setDate(dto.getDate());
    entity.setOpen(dto.getOpen());
    entity.setHigh(dto.getHigh());
    entity.setLow(dto.getLow());
    entity.setClose(dto.getClose());
    entity.setVolume(dto.getVolume());
    return entity;
  }
}
