package com.cozybinarybase.accountstopthestore.model.images.service.util;

import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class OcrUtil {
  @Value("${azure.ocr.endpoint}")
  private String AZURE_OCR_ENDPOINT;
  @Value("${azure.ocr.key}")
  private String AZURE_OCR_KEY;
  private final String MODEL_ID = "prebuilt-receipt";

  public String startDocumentAnalysis(MultipartFile file) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA); // 멀티파트 데이터 타입으로 변경
    headers.add("Ocp-Apim-Subscription-Key", AZURE_OCR_KEY);

    // 멀티파트 파일을 ByteArrayResource로 변환
    ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
      @Override
      public String getFilename() {
        return file.getOriginalFilename(); // 파일 이름을 가져옴
      }
    };

    // 멀티파트 바디 생성
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", fileResource);

    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(
        AZURE_OCR_ENDPOINT + "/formrecognizer/documentModels/" + MODEL_ID
            + ":analyze?api-version=2023-07-31",
        entity,
        String.class);

    return response.getHeaders().get("Operation-Location").get(0);
  }

  public String getDocumentAnalysisResult(String operationLocation) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Ocp-Apim-Subscription-Key", AZURE_OCR_KEY);
    headers.add("Host", "koreacentral.api.cognitive.microsoft.com");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    while (true) {
      ResponseEntity<String> response = restTemplate.exchange(
          operationLocation,
          HttpMethod.GET,
          entity,
          String.class);

      JSONObject jsonResponse = new JSONObject(response.getBody());
      String status = jsonResponse.getString("status");

      if ("succeeded".equals(status)) {
        return jsonResponse.toString();
      } else if ("failed".equals(status)) {
        throw new RuntimeException("Analysis failed");
      }

      System.out.println("Waiting for analysis result...");

      Thread.sleep(2000);
    }
  }
}
