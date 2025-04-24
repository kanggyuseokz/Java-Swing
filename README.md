# 🪙 코인 모의투자 프로그램 (Java Swing + CoinGecko API)

<!--[screenshot](./screenshot.png)--> <!-- 스크린샷 첨부시 사용, 없으면 삭제 가능 -->

---

## 📌 소개

**코인 모의투자 프로그램**은 Java Swing과 CoinGecko API를 활용하여
비트코인, 이더리움 등 주요 암호화폐의 실시간 시세 조회, 가상 매수/매도, 자산 현황, 거래 내역 관리 등을 제공하는 데스크탑 데모 애플리케이션입니다.

- **실시간 시세** 자동 갱신
- **매수/매도** 기능 (확장 가능)
- **포트폴리오** 및 **수익률** 확인 (확장 가능)
- **거래 내역 저장** (확장 가능)
- Swing 기반 **GUI**

---

## 🚀 주요 기능

- ✅ 코인 시세 실시간 자동 조회 (CoinGecko API)
- ✅ Swing UI 기반 코인 시세 테이블
- ✅ 수동 새로고침 버튼
- ✅ 5초마다 자동 시세 갱신 표시
- ⏳ 포트폴리오, 거래 내역, 자동매매 등 추가 확장 가능

---

## 📂 디렉토리 구조
```yaml
coinmockproject/
  gui/
    MainWindow.java         # 메인 프레임 및 전체 UI 제어
    CoinTablePanel.java     # 코인 시세 테이블 패널
  model/
    Coin.java               # 코인 데이터 객체 클래스
  service/
    CoinAPIService.java     # 코인 시세 API 연동 클래스
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

| 코인명    | 심볼 | 가격(USD)    |
|-----------|------|-------------|
| 비트코인   | BTC  | $69,543.21  |
| 이더리움   | ETH  | $3,664.78   |

- [새로고침]   업데이트: 12:34:56
---

## 🔧 확장/응용 예시

- 포트폴리오(보유자산, 잔고, 수익률) 관리
- 매수/매도 및 거래내역 기록
- 여러 코인 추가 지원
- 자동매매(조건부 트레이딩)
- 시세 변화 차트(JFreeChart 등 활용)
- 데이터 파일 저장 및 불러오기(CSV, JSON, SQLite)

---

## 📝 참고/라이선스

- CoinGecko API는 **무료/비상업적 용도**로 자유롭게 사용 가능  
  [공식 문서](https://www.coingecko.com/en/api/documentation)
- 이 프로젝트는 오픈소스 학습/포트폴리오/개인 사용에 자유롭게 활용할 수 있습니다.

---

