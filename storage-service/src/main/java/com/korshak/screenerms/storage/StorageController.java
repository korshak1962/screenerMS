package com.korshak.screenerms.storage;

import com.korshak.screenerms.dto.SharePriceDTO;
import com.korshak.screenerms.storage.dao.SharePrice;
import com.korshak.screenerms.storage.dao.SharePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private SharePriceRepository sharePriceRepository;

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody List<SharePriceDTO> sharePriceDTOs) {
        List<SharePrice> sharePrices = sharePriceDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        sharePriceRepository.saveAll(sharePrices);

        return ResponseEntity.ok("Successfully saved " + sharePrices.size() + " share prices");
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
