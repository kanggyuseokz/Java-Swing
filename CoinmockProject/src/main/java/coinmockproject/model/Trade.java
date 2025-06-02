package coinmockproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Trade {
	private String username;
    private String coin;            // 코인명(심볼)
    private String type;            // "BUY" 또는 "SELL"
    private double price;           // 거래 단가
    private double amount;          // 수량
    private LocalDateTime time;     // 거래 시간
    
    public Trade(String coin, String type, double price, double amount, LocalDateTime time) {
        this.coin   = coin;
        this.type   = type;
        this.price  = price;
        this.amount = amount;
        this.time   = time;
    }
}