package com.korshak.screenerms.downloader.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korshak.screenerms.downloader.service.DownLoaderService;
import com.korshak.screenerms.dto.SharePriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DownLoaderServiceImpl implements DownLoaderService {

    @Value("${storage.api.save-all}")
    private String storageSaveAllUrl;
    @Value("${alpha.apiKey}")
    private String apiKey;
    @Value("${alpha.baseUrl}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String downLoadData(String timeSeriesLabel, String ticker, String interval, String month) {
        String fullAlphaUrl = baseUrl + timeSeriesLabel + "&symbol=" + ticker + "&interval=" + interval + "&month=" + month
                + "&outputsize=full&apikey=" + apiKey;
        ResponseEntity<String> response = restTemplate.getForEntity(
                fullAlphaUrl, String.class);
        System.out.println("downloader service get request for: ticker "+ticker +" month "+month);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode timeSeries = root.path("Time Series (5min)");

        List<SharePriceDTO> intradayDataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Iterator<Map.Entry<String, JsonNode>> entrys = timeSeries.fields();
        while (entrys.hasNext()) {
            Map.Entry<String, JsonNode> dataNode = entrys.next();
            LocalDateTime localDateTime = LocalDateTime.parse(dataNode.getKey(), formatter);
            double open = Double.parseDouble(dataNode.getValue().path("1. open").asText());
            double high = Double.parseDouble(dataNode.getValue().path("2. high").asText());
            double low = Double.parseDouble(dataNode.getValue().path("3. low").asText());
            double close = Double.parseDouble(dataNode.getValue().path("4. close").asText());
            long volume = Long.parseLong(dataNode.getValue().path("5. volume").asText());
            SharePriceDTO sharePrice = new SharePriceDTO(ticker, localDateTime, open, high, low, close, volume);
            intradayDataList.add(sharePrice);
        }
        ResponseEntity<String> saved = restTemplate.postForEntity(storageSaveAllUrl, intradayDataList, String.class);
        return saved.getBody();
    }
}
