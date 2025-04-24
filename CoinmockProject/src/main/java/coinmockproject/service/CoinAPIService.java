package coinmockproject.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

public class CoinAPIService {
    public static void main(String[] args) {
        Timer timer = new Timer();
        int delay = 0;         // 처음 시작 딜레이 (ms)
        int period = 60000;     // 반복 주기 1분(60000ms)

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    // API 요청 URL
                    String urlStr = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";
                    URL url = new URL(urlStr);

                    // HTTP 연결
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
                    System.out.println("------[시세 갱신: " + java.time.LocalTime.now() + "]------");
                    System.out.println("비트코인: $" + btcPrice + " / 이더리움: $" + ethPrice);
                } catch (Exception e) {
                    System.out.println("API 요청 오류: " + e.getMessage());
                }
            }
        }, delay, period);

        // 프로그램 종료 방지 (Enter 누르면 종료)
        try {
            System.in.read();
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
