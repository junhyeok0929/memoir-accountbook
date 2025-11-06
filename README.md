# 📖 추억가계부 (Memoir Accountbook)

Spring Boot와 Spring Security(JWT)를 기반으로, **보안 인증/인가 기능이 적용된** 감성 기록형 가계부 백엔드 REST API 서버입니다.

단순 CRUD를 넘어, 로그인한 본인만 자신의 데이터에 접근(생성, 조회, 수정, 삭제)할 수 있도록 **'인가(Authorization)'** 로직을 구현하여 보안성을 확보하는 것을 목표로 했습니다.

<br>

## 🛠️ 사용 기술 스택 (Tech Stack)

* **언어:** Java 17
* **핵심 프레임워크:** Spring Boot 3
* **보안:** **Spring Security**, **JWT (jjwt 라이브러리)**
* **데이터베이스:**
    * Spring Data JPA, Hibernate
    * **MySQL** (영구 저장용)
    * H2 (개발/테스트용)
* **빌드 도구:** Gradle
* **테스트 도구:** Postman
* **기타:** Lombok

<br>

## 📌 주요 기능 (API Endpoints)

모든 API는 **JWT(Bearer Token)**를 통한 **인증(Authentication)**이 필요합니다.
(단, `회원가입`, `로그인`, `H2-Console`은 예외적으로 허용)

### 1. 🧑‍💻 회원 (Member)

* `POST /api/members/signup`: **회원가입** (비밀번호는 **BCrypt**로 암호화되어 저장)
* `POST /api/members/login`: **로그인** (성공 시 `accessToken` 발급)

### 2. 💸 거래 내역 (Transaction) - **보안 CRUD**

* `POST /api/transactions`: **'내'** 거래 내역 생성 (C)
* `GET /api/transactions/me`: **'내'** 모든 거래 내역 조회 (R)
* `PUT /api/transactions/{transactionId}`: **'내'** 거래 내역 수정 (U) (본인 확인)
* `DELETE /api/transactions/{transactionId}`: **'내'** 거래 내역 삭제 (D) (본인 확인)

### 3. 📔 일기 (Diary) - **보안 CRUD**

* `POST /api/diaries`: **'내'** 일기 생성 (C)
* `GET /api/diaries/me`: **'내'** 모든 일기 조회 (R)
* `PUT /api/diaries/{diaryId}`: **'내'** 일기 수정 (U) (본인 확인)
* `DELETE /api/diaries/{diaryId}`: **'내'** 일기 삭제 (D) (본인 확인)

### 4. 📅 일일 통합 조회 (Daily Record)

* `GET /api/daily-records/date/{date}`: **'내'** 특정 날짜의 거래 내역과 일기를 **한 번에** 조회 (R)

<br>

## 🚀 다음 목표

1.  **[현재] DB 교체:** H2(임시) DB에서 **MySQL(영구) DB**로 최종 연결 및 데이터 영구 저장. (진행 중)
2.  **[향후] 예외 처리:** `IllegalArgumentException` 대신, API 응답에 맞는 표준화된 오류 응답(Error Response) 도입.
3.  **[향후] 프론트엔드:** React를 사용하여 이 API와 통신하는 웹 애플리케이션 화면 개발.