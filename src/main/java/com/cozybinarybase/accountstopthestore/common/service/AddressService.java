package com.cozybinarybase.accountstopthestore.common.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class AddressService {

  @Value("${kakao.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate;

  public JSONObject getAddressInfo(String address) {
    final String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + apiKey);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    return new JSONObject(response.getBody());
  }

  public Map<String, String> getCoordinates(String address) {
    Map<String, String> coordinates = new HashMap<>();
    if (address == null || address.isEmpty()) return coordinates;

    JSONObject addressInfoJson = getAddressInfo(address);

    if (!addressInfoJson.getJSONArray("documents").isEmpty()) {
      JSONObject documentObject = addressInfoJson.getJSONArray("documents").getJSONObject(0);

      // optJSONObject를 사용하여 "road_address"가 없는 경우 null을 반환
      JSONObject roadAddressObject = documentObject.optJSONObject("road_address");

      // roadAddressObject가 null이 아닐 때만 x, y 좌표를 설정
      if (roadAddressObject != null) {
        coordinates.put("x", roadAddressObject.getString("x"));
        coordinates.put("y", roadAddressObject.getString("y"));
      }
    }

    return coordinates;
  }
}
