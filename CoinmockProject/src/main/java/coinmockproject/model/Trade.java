package coinmockproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Trade {
    private String coin;            // 코인명(심볼)
    private String type;            // "BUY" 또는 "SELL"
    private double price;           // 거래 단가
    private double amount;          // 수량
    private LocalDateTime time;     // 거래 시간
}