package coinmockproject.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class CoinAPIService {

    public static void main(String[] args) {
        try {
            // API 요청 URL
            String urlStr = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";
            URL url = new URL(urlStr);

            // HTTP 연결 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 응답 읽기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();
            conn.disconnect();

            // JSON 파싱
            JSONObject json = new JSONObject(responseBuilder.toString());

            double btcPrice = json.getJSONObject("bitcoin").getDouble("usd");
            double ethPrice = json.getJSONObject("ethereum").getDouble("usd");

            // 결과 출력
            System.out.println("🪙 현재 비트코인 가격 (USD): $" + btcPrice);
            System.out.println("🪙 현재 이더리움 가격 (USD): $" + ethPrice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
