# 🪙 코인 모의투자 프로그램 (Java Swing + CoinGecko API)

<!--[screenshot](./screenshot.png)--> <!-- 스크린샷 첨부시 사용, 없으면 삭제 가능 -->

---

## 📌 소개

**코인 모의투자 프로그램**은 Java Swing과 CoinGecko API를 활용하여
비트코인, 이더리움 등 주요 암호화폐의 실시간 시세 조회, 가상 매수/매도, 자산 현황, 거래 내역 관리 등을 제공하는 데스크탑 데모 애플리케이션입니다.

- 💱 **6종 코인** 실시간 시세 자동 갱신  
- 🧾 **가상 매수/매도** 기능  
- 📊 **포트폴리오 확인** (잔고, 수익률 등)  
- 🧠 **"나의 투자 현황"** 클릭 시 포트폴리오 화면 전환  
- 🗃️ **거래 내역 저장 및 조회**  
- 💻 **Java Swing 기반 GUI**

---

## 🚀 주요 기능

- ✅ CoinGecko API 기반 실시간 코인 시세 조회
- ✅ Java Swing UI: 시세 테이블, 포트폴리오, 거래내역
- ✅ 포트폴리오 수익률, 잔고 계산 기능
- ✅ "나의 투자 현황" 라벨 클릭 시 화면 전환
- ✅ 포트폴리오에서 "투자하기" 버튼 클릭 시 시세 화면으로 복귀
- ⏳ 자동매매, 차트 시각화, 사용자 관리 등 추가 확장 가능

---

## 📂 디렉토리 구조
```yaml
coinmockproject/
└── src/main/java/coinmockproject/
    ├── db/                          # DB 연동 관련 모듈
    │   ├── DBManager.java           # DB 연결 설정 및 커넥션 관리
    │   ├── TradeRepository.java     # 거래 내역 저장/조회 DAO
    │   └── UserRepository.java      # 사용자 정보 관련 DAO

    ├── gui/                         # UI 관련 클래스
    │   ├── panel/                   # 화면 구성 요소 (화면 전환 단위)
    │   │   ├── CoinCardPanel.java         # 코인 상세/매수/매도 UI
    │   │   ├── LoginPanel.java            # 로그인 화면
    │   │   ├── RegisterPanel.java         # 회원가입 화면
    │   │   ├── PortfolioPanel.java        # 포트폴리오 및 수익률 확인 UI
    │   │   └── PurchaseFrame.java         # 매수 창 (프레임 분리)
    │   ├── ColorTheme.java          # UI 색상 및 스타일 테마 설정
    │   ├── Frame.java               # 커스텀 타이틀 바 JFrame (기본 틀)
    │   └── MainWindow.java          # 메인 프레임 (전체 화면 컨트롤)

    ├── model/                       # 데이터 객체 클래스 (DTO/VO)
    │   ├── Coin.java                # 코인 정보
    │   ├── Trade.java               # 매수/매도 거래 정보
    │   └── User.java                # 사용자 계정 정보

    ├── service/                     # 비즈니스 로직, API 연동
    │   ├── CoinAPIService.java      # CoinGecko API 연동
    │   ├── PortfolioService.java    # 포트폴리오 관련 계산 로직
    │   └── TradeManager.java        # 매수/매도 처리 및 거래 실행

    └── util/                        # 공통 유틸리티
        ├── DialogUtil.java          # 공통 알림/경고 다이얼로그
        └── EnvLoader.java           # 환경변수(.env) 로딩 유틸
```
---
## DB 초기화
```SQL
-- 1) user 테이블: id는 AUTO_INCREMENT이지만, 애플리케이션에서는 username만 사용
CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  balance DOUBLE NOT NULL
);

-- 2) trade 테이블: 거래 기록 저장
CREATE TABLE trade (
  id INT AUTO_INCREMENT PRIMARY KEY,
  coin VARCHAR(50) NOT NULL,
  type VARCHAR(10) NOT NULL,    -- BUY or SELL
  price DOUBLE NOT NULL,
  amount DOUBLE NOT NULL,
  time TIMESTAMP NOT NULL
);
ALTER TABLE trade
  ADD COLUMN username VARCHAR(50) NOT NULL AFTER coin,
  ADD INDEX idx_trade_username (username);

```
---
## 🛠️ 기술 스택

- Java 8 이상
- Java Swing (GUI)
- CoinGecko API (무료 시세 데이터)
- org.json (JSON 파싱 라이브러리)
- (확장시) JFreeChart, SQLite, Gson 등

---

## ⚡ 설치 및 실행 방법

1. **프로젝트 클론 또는 다운로드**
2. `org.json` 라이브러리 [다운로드](https://mvnrepository.com/artifact/org.json/json) 후 빌드 패스에 추가  
   (Maven/Gradle 사용시 의존성 추가)
3. Java 8 이상 환경에서 실행  
   (패키지 구조 지키기!)
4. `gui/MainWindow.java` 실행

---
## 🖥️ 실행 화면 예시

### 실시간 코인 시세
| 코인명      | 심볼  | 가격(USD)     |
| -------- | --- | ----------- |
| 비트코인     | BTC | \$68,000.00 |
| 이더리움     | ETH | \$3,500.00  |
| 이더리움 클래식 | ETC | \$28.50     |
| ...      | ... | ...         |

- [새로고침]   업데이트: 12:34:56
---

## 🔧 확장/응용 기능

- 조건부 자동매매 알고리즘 추가
- 시세 변동 차트 시각화 (JFreeChart 등)
- SQLite/MySQL 기반 데이터 저장
- 사용자 인증 및 로그인 기능
- CSV/JSON 기반 포트폴리오 백업/복원

---

## 📝 참고/라이선스

- CoinGecko API는 **무료/비상업적 용도**로 자유롭게 사용 가능  
  [공식 문서](https://www.coingecko.com/en/api/documentation)
- 이 프로젝트는 오픈소스 학습/포트폴리오/개인 사용에 자유롭게 활용할 수 있습니다.

---

