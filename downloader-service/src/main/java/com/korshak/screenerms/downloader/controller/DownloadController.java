package com.korshak.screenerms.downloader.controller;

import com.korshak.screenerms.downloader.service.DownLoaderService;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Controller for handling download requests.
 */
@SuppressWarnings("checkstyle:Indentation")
@RestController
@RequestMapping("/api/downloader")
public class DownloadController {
  @SuppressWarnings("checkstyle:Indentation")
  @Autowired
  private DownLoaderService downLoaderService;
  @SuppressWarnings("checkstyle:Indentation")
  private CompletableFuture<String> downloadFuture;

  @SuppressWarnings({"checkstyle:Indentation", "checkstyle:MissingJavadocMethod"})
  @PostMapping("/download")
  public ResponseEntity<String> fetchAndSaveData(
      @RequestParam String timeSeriesLabel,
      @RequestParam String ticker,
      @RequestParam String interval,
      @RequestParam String month) {
    try {
      YearMonth.parse(month); // Validate month format
      System.out.println("downloader controller get request for:  " + month);
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

  @SuppressWarnings({"checkstyle:Indentation", "checkstyle:LineLength",
      "checkstyle:MissingJavadocMethod"})
  @GetMapping(value = "/download-status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter downloadStatus() {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    // logger.info("SSE connection attempt received");
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
        String result = downloadFuture.get();
        System.out.println("result in status: " + result);
        // Send completion event
        emitter.send(SseEmitter.event().name("complete")
            .data("Download completed. " + result + " records saved."));
        System.out.println("emitter.send: ");
        emitter.complete();
        System.out.println("emitter.complete: ");
      } catch (Exception e) {
        System.out.println("Exception in status: " + e);
        emitter.completeWithError(e);
      }
    });

    return emitter;
  }
}
