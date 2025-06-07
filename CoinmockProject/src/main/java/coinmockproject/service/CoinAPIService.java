package coinmockproject.service;

import coinmockproject.model.Coin;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * CoinAPIService: simple/price (fixed) 와 coins/markets (동적) 를
 * 내부적으로 하나의 캐시 로직으로 통일합니다.
 */
public class CoinAPIService {
    // --- 캐시 필드 ---
    private static Coin[] cacheCoins   = new Coin[0];
    private static String cacheIds     = "";
    private static long   cacheTime    = 0L;
    private static final long TTL_MS   = 60_000L;  // 1분

    /** 고정 6종 코인 호출 */
    public static Coin[] fetchCoins() throws Exception {
        List<String> fixed = Arrays.asList(
            "bitcoin",
            "ethereum",
            "ripple",
            "dogecoin",
            "solana",
            "ethereum-classic"
        );
        // 항상 동적 호출 쪽으로 위임
        return fetchCoins(fixed);
    }

    /**
     * 동적 호출 + 캐시 로직
     * @param coinIds ex) ["bitcoin","ethereum",...]
     */
    public static Coin[] fetchCoins(List<String> coinIds) throws Exception {
        String joined = String.join(",", coinIds);

        // 1) 캐시 유효성 검사: 동일한 coinIds 로 1분 이내 재사용
        if (joined.equals(cacheIds)
         && System.currentTimeMillis() - cacheTime < TTL_MS) {
            return cacheCoins;
        }

        // 2) 실제 API 호출 (coins/markets)
        String urlStr = "https://api.coingecko.com/api/v3/coins/markets"
                      + "?vs_currency=usd"
                      + "&ids=" + joined
                      + "&order=market_cap_desc"
                      + "&per_page=" + coinIds.size()
                      + "&page=1"
                      + "&sparkline=false";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5_000);
        conn.setReadTimeout(5_000);

        int code = conn.getResponseCode();
        if (code == 429) {
            // Rate limit: 캐시 있으면 그대로 반환, 없으면 예외
            if (cacheCoins.length > 0) {
                System.err.println("429 Too Many Requests → 캐시 반환");
                return cacheCoins;
            }
            throw new RuntimeException("API rate limit exceeded (429)");
        }
        if (code != 200) {
            throw new RuntimeException("API 호출 실패: HTTP " + code);
        }

        // 3) JSON 파싱
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
        List<Coin> list = new ArrayList<>(array.size());
        for (JsonElement el : array) {
            JsonObject o = el.getAsJsonObject();
            String name   = o.get("name").getAsString();
            String symbol = o.get("symbol").getAsString().toUpperCase();
            double price  = o.get("current_price").getAsDouble();
            list.add(new Coin(name, symbol, price));
        }
        reader.close();
        conn.disconnect();

        // 4) 캐시 갱신
        cacheCoins = list.toArray(new Coin[0]);
        cacheIds   = joined;
        cacheTime  = System.currentTimeMillis();

        return cacheCoins;
    }
}
