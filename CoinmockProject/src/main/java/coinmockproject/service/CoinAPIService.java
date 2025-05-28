package coinmockproject.service;

import coinmockproject.model.Coin;

import org.json.JSONObject;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class CoinAPIService {

	// 고정 6종 코인 호출용
	public static Coin[] fetchCoins() {
		String[] names = { "비트코인", "이더리움", "리플", "도지코인", "솔라나", "이더리움클래식" };
		String[] symbols = { "bitcoin", "ethereum", "ripple", "dogecoin", "solana", "ethereum-classic" };
	    Coin[] coins = new Coin[names.length];

	    try {
	        String ids = String.join(",", symbols);
	        String urlStr = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=usd";
	        URL url = new URL(urlStr);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

	        // 응답 읽기
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) sb.append(line);
	        reader.close();
	        conn.disconnect();

	        JSONObject json = new JSONObject(sb.toString());

	        for (int i = 0; i < names.length; i++) {
	            try {
	                double price = json.getJSONObject(symbols[i]).getDouble("usd");
	                coins[i] = new Coin(names[i], symbols[i].toUpperCase(), price);
	            } catch (Exception inner) {
	                System.err.println("[파싱 실패] " + symbols[i]);
	                coins[i] = new Coin(names[i], symbols[i].toUpperCase(), -1);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        for (int i = 0; i < names.length; i++) {
	            coins[i] = new Coin(names[i], symbols[i].toUpperCase(), -1);
	        }
	    }

	    return coins;
	}

	// 선택한 코인 리스트 기반으로 동적 호출
	public static Coin[] fetchCoins(List<String> coinIds) {
		String joinedIds = String.join(",", coinIds);
		String urlStr = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + joinedIds;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			List<Coin> coins = new ArrayList<>();
			for (JsonElement el : jsonArray) {
				JsonObject obj = el.getAsJsonObject();
				String name = obj.get("name").getAsString();
				String symbol = obj.get("symbol").getAsString().toUpperCase();
				double price = obj.get("current_price").getAsDouble();
				coins.add(new Coin(name, symbol, price));
			}

			return coins.toArray(new Coin[0]);

		} catch (Exception e) {
			e.printStackTrace();
			return new Coin[0];
		}
	}
}
