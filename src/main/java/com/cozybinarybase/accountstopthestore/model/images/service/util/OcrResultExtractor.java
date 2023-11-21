package com.cozybinarybase.accountstopthestore.model.images.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.JsonNode;

public class OcrResultExtractor {

  public OcrResult extractOcrResult(JsonNode content) {
    OcrResult ocrResult = new OcrResult();

    Pattern datePattern = Pattern.compile("(\\d{4})[ .]+(\\d{2})[ .]+(\\d{2})[ .]+(\\d{2}):(\\d{2}):(\\d{2})");
    Pattern amountPattern = Pattern.compile("판매 합계\\n(\\d+,?\\d*)");
    Pattern vendorPattern = Pattern.compile("\\(주\\) ([^\\n]+)");
    Pattern addressPattern = Pattern.compile("매장: ([^\\(]+)");

    Matcher dateMatcher = datePattern.matcher(content.asText());
    Matcher amountMatcher = amountPattern.matcher(content.asText());
    Matcher vendorMatcher = vendorPattern.matcher(content.asText());
    Matcher addressMatcher = addressPattern.matcher(content.asText());

    if (dateMatcher.find()) {
      ocrResult.setOcrDate(String.join(".",
          dateMatcher.group(1), // 년
          dateMatcher.group(2), // 월
          dateMatcher.group(3)  // 일
      ) + "T" + dateMatcher.group(4) + ":" + // 시
          dateMatcher.group(5) + ":" + // 분
          dateMatcher.group(6));        // 초
    }

    if (amountMatcher.find()) {
      String amountString = amountMatcher.group(1).replace(",", "");
      ocrResult.setOcrAmount(Long.parseLong(amountString));
    }

    if (vendorMatcher.find()) {
      ocrResult.setOcrVendor(vendorMatcher.group(1).trim());
    }

    if (addressMatcher.find()) {
      ocrResult.setOcrAddress(addressMatcher.group(1).trim());
    }

    if (ocrResult.getOcrDate() != null && ocrResult.getOcrAmount() != null &&
        ocrResult.getOcrVendor() != null && ocrResult.getOcrAddress() != null) {
      return ocrResult;
    }

    datePattern = Pattern.compile("거래일시 (\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})");
    amountPattern = Pattern.compile("합 계:\\n(\\d+,?\\d*)원");
    vendorPattern = Pattern.compile("\\n(.+\\(주\\))\\n");
    addressPattern = Pattern.compile("\\n(서울시[^\\n]+)\\n");

    dateMatcher = datePattern.matcher(content.asText());
    amountMatcher = amountPattern.matcher(content.asText());
    vendorMatcher = vendorPattern.matcher(content.asText());
    addressMatcher = addressPattern.matcher(content.asText());

    if (dateMatcher.find()) {
      String originalDate = dateMatcher.group(1);
      // "20/10/14"에서 연, 월, 일, 시, 분, 초를 추출합니다.
      String[] dateParts = originalDate.split("[ /:]");
      // 연도를 4자리로 변환합니다.
      String year = "20" + dateParts[0];
      // 날짜 형식을 "YYYY.MM.DDTHH:MM:SS"로 변환합니다.
      String ocrDate = String.format("%s.%s.%sT%s:%s:%s", year, dateParts[1], dateParts[2],
          dateParts[3], dateParts[4], dateParts[5]);
      ocrResult.setOcrDate(ocrDate);
    }

    if (amountMatcher.find()) {
      String amountString = amountMatcher.group(1).replace(",", "");
      ocrResult.setOcrAmount(Long.parseLong(amountString));
    }

    if (vendorMatcher.find()) {
      ocrResult.setOcrVendor(vendorMatcher.group(1).trim());
    }

    if (addressMatcher.find()) {
      ocrResult.setOcrAddress(addressMatcher.group(1).trim());
    }

    return ocrResult;
  }
}
