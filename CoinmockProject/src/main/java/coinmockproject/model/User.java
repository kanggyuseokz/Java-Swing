package coinmockproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;            // DB의 AUTO_INCREMENT PK
    private String username;
    private String password;
    private double balance;    // 기본값 5000.00으로 시작

    // 1) 회원가입용 생성자: id 미지정, balance=5000.00
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 5000.00;
    }

    // 2) DB에서 꺼낼 때 사용하는 생성자: id와 balance를 DB에서 조회하여 세팅
    public User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance  = balance;
    }

    // 3) 기본 생성자
    public User() {}
}
