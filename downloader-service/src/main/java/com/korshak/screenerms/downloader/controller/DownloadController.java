package com.korshak.screenerms.downloader.controller;

import com.korshak.screenerms.downloader.service.DownLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/downloader")
public class DownloadController {
    @Autowired
    private DownLoaderService downLoaderService;
    private CompletableFuture<Integer> downloadFuture;

    @PostMapping("/download")
    public ResponseEntity<String> fetchAndSaveData(
            @RequestParam String timeSeriesLabel,
            @RequestParam String ticker,
            @RequestParam String interval,
            @RequestParam String month) {
        try {
            YearMonth.parse(month); // Validate month format

            // Start the download process asynchronously
            downloadFuture = CompletableFuture.supplyAsync(() ->
                    downLoaderService.downLoadData(timeSeriesLabel, ticker, interval, month)
            );

            return ResponseEntity.ok("Download process started");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid month format. Please use YYYY-MM");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting download: " + e.getMessage());
        }
    }

    @GetMapping("/download-status")
    public SseEmitter downloadStatus() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        CompletableFuture.runAsync(() -> {
            try {
                // Send progress updates
                emitter.send(SseEmitter.event().name("progress").data("25% complete"));
                Thread.sleep(1000);
                emitter.send(SseEmitter.event().name("progress").data("50% complete"));
                Thread.sleep(1000);
                emitter.send(SseEmitter.event().name("progress").data("75% complete"));
                Thread.sleep(1000);

                // Wait for the download to complete
                Integer recordCount = downloadFuture.get();

                // Send completion event
                emitter.send(SseEmitter.event().name("complete").data("Download completed. " + recordCount + " records saved."));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}