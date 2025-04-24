package coinmockproject.service;

import coinmockproject.model.Coin;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinAPIService {
    public static Coin[] fetchCoins() {
        // 예시: 비트코인, 이더리움
        String[] names = {"비트코인", "이더리움"};
        String[] symbols = {"bitcoin", "ethereum"};
        Coin[] coins = new Coin[names.length];
        try {
            String ids = "bitcoin,ethereum";
            String urlStr = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=usd";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            for (int i = 0; i < names.length; i++) {
                double price = json.getJSONObject(symbols[i]).getDouble("usd");
                coins[i] = new Coin(names[i], symbols[i].toUpperCase(), price);
            }
        } catch (Exception e) {
            for (int i = 0; i < names.length; i++) {
                coins[i] = new Coin(names[i], symbols[i].toUpperCase(), -1);
            }
        }
        return coins;
    }
}