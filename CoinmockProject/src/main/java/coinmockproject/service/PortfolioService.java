package coinmockproject.service;

import coinmockproject.db.TradeRepository;
import coinmockproject.db.UserRepository;
import coinmockproject.model.Coin;
import coinmockproject.model.Trade;
import coinmockproject.model.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;
@Getter
/**
 * PortfolioService (JDBC + trade-only 방식)
 *  - UserRepository: user 테이블의 balance 조회/업데이트
 *  - TradeRepository: trade 테이블에 BUY/SELL 이력 저장 + 현재 보유량 계산
 */
public class PortfolioService {
    private final UserRepository  userRepo;
    private final TradeRepository tradeRepo;
    private static PortfolioService instance = new PortfolioService();

    public PortfolioService() {
        this.userRepo  = new UserRepository();
        this.tradeRepo = new TradeRepository();
    }
    
    public static PortfolioService getInstance() {
        return instance;
    }

    /**
     * 코인 매수 로직
     * 1) 잔액 확인 → 2) 잔액 차감 → 3) trade 테이블에 매수 이력 저장
     *
     * @param user   User 객체 (username, balance 정보가 세팅된 상태)
     * @param coin   Coin 객체 (getSymbol(), getPriceUsd() 등이 채워진 상태)
     * @param amount 매수 수량
     * @return true  → 매수 성공
     *         false → 잔액 부족 또는 DB 오류
     */
    public boolean buyCoin(User user, Coin coin, double amount) {
        if (user == null || coin == null || amount <= 0) {
            return false;
        }

        double unitPrice = coin.getPriceUsd();
        double totalCost = Math.round(unitPrice * amount * 100.0) / 100.0;
        String username  = user.getUsername();

        // 1) 잔액 확인
        double currentBalance = user.getBalance();
        if (currentBalance < totalCost) {
            return false; // 잔액 부족
        }

        // 2) 잔액 차감 & DB 업데이트
        double newBalance = Math.round((currentBalance - totalCost) * 100.0) / 100.0;
        boolean updated = userRepo.updateBalance(username, newBalance);
        if (!updated) {
            return false; // DB 오류
        }
        user.setBalance(newBalance); // 메모리 상 User에도 반영

        // 3) trade 테이블에 BUY 이력 저장
        Trade trade = new Trade(
            username,
            coin.getSymbol(),
            "BUY",
            unitPrice,
            amount,
            LocalDateTime.now()
        );
        tradeRepo.saveTrade(trade);

        return true;
    }

    /**
     * 코인 매도 로직
     * 1) 현재 보유량 조회 → 2) 보유량 검증(충분한 개수인지) → 3) trade 테이블에 SELL 이력 저장 + 잔액 증가
     *
     * @param user   User 객체 (username, balance 정보가 세팅된 상태)
     * @param coin   Coin 객체 (getSymbol(), getPriceUsd() 등이 채워진 상태)
     * @param amount 매도 수량
     * @return true  → 매도 성공
     *         false → 보유량 부족(또는 DB 오류)
     */
    public boolean sellCoin(User user, Coin coin, double amount) {
        if (user == null || coin == null || amount <= 0) {
            return false;
        }

        String username = user.getUsername();
        String symbol   = coin.getSymbol();

        // 1) 현재 보유량 조회 (trade-only 방식)
        Map<String, Double> currentHoldings = tradeRepo.findCurrentHoldings(username);
        double heldAmount = currentHoldings.getOrDefault(symbol, 0.0);

        // 2) 보유량 검증
        if (heldAmount < amount) {
            return false; // 보유량 부족 → 매도 불가
        }

        // 3) 매도 이력 저장 + 잔액 증가
        double unitPrice = coin.getPriceUsd();
        double totalGain = Math.round(unitPrice * amount * 100.0) / 100.0;

        // 3-1) trade 테이블에 SELL 이력 저장
        Trade trade = new Trade(
            username,
            symbol,
            "SELL",
            unitPrice,
            amount,
            LocalDateTime.now()
        );
        tradeRepo.saveTrade(trade);

        // 3-2) User 잔액 업데이트 (매도 금액만큼 잔액에 더함)
        double newBalance = Math.round((user.getBalance() + totalGain) * 100.0) / 100.0;
        boolean updated = userRepo.updateBalance(username, newBalance);
        if (!updated) {
            return false; // DB 오류
        }
        user.setBalance(newBalance);

        return true;
    }

    /**
     * 특정 사용자가 현재 보유 중인 코인별 수량을 Map으로 반환
     */
    public Map<String, Double> getCurrentHoldings(User user) {
        return tradeRepo.findCurrentHoldings(user.getUsername());
    }

    /**
     * (디버그용) 콘솔에 사용자 보유량과 거래 이력을 출력
     */
    public void printUserPortfolioAndHistory(User user) {
        System.out.println("=== Current Holdings (" + user.getUsername() + ") ===");
        Map<String, Double> holdings = getCurrentHoldings(user);
        if (holdings.isEmpty()) {
            System.out.println("보유 코인이 없습니다.");
        } else {
            holdings.forEach((coin, amt) -> {
                System.out.printf("%-8s : %.6f%n", coin, amt);
            });
        }

        System.out.println("\n=== Trade History (" + user.getUsername() + ") ===");
        tradeRepo.findTradesByUsername(user.getUsername()).forEach(t -> {
            System.out.printf("[%s] %s %s %.6f @ $%,.2f%n",
                t.getTime(), t.getType(), t.getCoin(), t.getAmount(), t.getPrice());
        });
        System.out.println("==========================================");
    }
}
