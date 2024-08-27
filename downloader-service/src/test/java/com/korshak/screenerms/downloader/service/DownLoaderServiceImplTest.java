package com.korshak.screenerms.downloader.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.korshak.screenerms.downloader.serviceimpl.DownLoaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class DownLoaderServiceImplTest {

  private DownLoaderServiceImpl downLoaderService;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    downLoaderService = new DownLoaderServiceImpl();
    ReflectionTestUtils.setField(downLoaderService, "restTemplate", restTemplate);
    ReflectionTestUtils.setField(downLoaderService, "storageSaveAllUrl", "http://example.com/save");
    ReflectionTestUtils.setField(downLoaderService, "apiKey", "testApiKey");
    ReflectionTestUtils.setField(downLoaderService, "baseUrl", "http://example.com/api");
  }

  @Test
  void testDownLoadData_Success() {
    // Mock the first REST call
    ResponseEntity<String> mockResponse = new ResponseEntity<>(
        "{\"Time Series (5min)\": {\"2023-05-01 09:30:00\": {\"1. open\":\"100\", \"2. high\":\"101\", \"3. low\":\"99\", \"4. close\":\"100.5\", \"5. volume\":\"1000000\"}}}",
        HttpStatus.OK
    );
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponse);

    // Mock the second REST call
    ResponseEntity<String> mockSavedResponse = new ResponseEntity<>("Data saved successfully", HttpStatus.OK);
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockSavedResponse);

    String result = downLoaderService.downLoadData("TIME_SERIES_INTRADAY", "AAPL", "5min", "2023-05");

    assertNotNull(result);
    assertEquals("Data saved successfully", result);

    verify(restTemplate).getForEntity(anyString(), eq(String.class));
    verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
  }

  @Test
  void testDownLoadData_FailedToSave() {
    // Mock the first REST call
    ResponseEntity<String> mockResponse = new ResponseEntity<>(
        "{\"Time Series (5min)\": {\"2023-05-01 09:30:00\": {\"1. open\":\"100\", \"2. high\":\"101\", \"3. low\":\"99\", \"4. close\":\"100.5\", \"5. volume\":\"1000000\"}}}",
        HttpStatus.OK
    );
    when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponse);

    // Mock the second REST call to return null
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(null);

    String result = downLoaderService.downLoadData("TIME_SERIES_INTRADAY", "AAPL", "5min", "2023-05");

    assertNotNull(result);
    assertEquals("Failed to save data", result);

    verify(restTemplate).getForEntity(anyString(), eq(String.class));
    verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
  }
}