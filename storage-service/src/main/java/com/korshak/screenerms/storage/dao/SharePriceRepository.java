package com.korshak.screenerms.storage.dao;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharePriceRepository extends JpaRepository<SharePrice, SharePriceId> {
  Page<SharePrice> findByTicker(String ticker, Pageable pageable);

  Page<SharePrice> findByTickerAndDateBetween(String ticker, LocalDateTime startDate,
                                              LocalDateTime endDate, Pageable pageable);
}
