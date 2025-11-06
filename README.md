# 📖 추억가계부 (Memoir Accountbook)

단순한 지출 내역 기록을 넘어, 그날의 감정과 추억을 함께 담아내는 **감성 기록형 가계부 백엔드 API 서버**입니다.

이 프로젝트는 Spring Boot와 JPA를 기반으로 RESTful API를 설계하고, CRUD(생성, 조회, 수정, 삭제) 기능을 구현하며 백엔드 개발의 핵심 역량을 강화하기 위해 진행되었습니다.

<br>

## 🛠️ 사용 기술 스택 (Tech Stack)

* **언어:** Java 17
* **핵심 프레임워크:** Spring Boot 3
* **데이터베이스:** Spring Data JPA, Hibernate
* **테스트 DB:** H2 (인메모리 DB)
* **배포/테스트:**
    * 내장 Tomcat (서버)
    * Postman (API 테스트 도구)
* **빌드 도구:** Gradle
* **기타:** Lombok

<br>

## 📌 주요 기능 (API Endpoints)

현재까지 구현된 핵심 API 기능 목록입니다.

### 1. 🧑‍💻 회원 (Member)

* `POST /api/members/signup`: 회원가입

### 2. 💸 거래 내역 (Transaction) - **CRUD**

* `POST /api/transactions`: 거래 내역 생성 (Create)
* `GET /api/transactions/member/{memberId}`: 특정 회원의 모든 거래 내역 조회 (Read)
* `PUT /api/transactions/{transactionId}`: 특정 거래 내역 수정 (Update)
* `DELETE /api/transactions/{transactionId}`: 특정 거래 내역 삭제 (Delete)

### 3. 📔 일기 (Diary) - **CRUD**

* `POST /api/diaries`: 일기 생성 (Create)
* `GET /api/diaries/member/{memberId}`: 특정 회원의 모든 일기 조회 (Read)
* `PUT /api/diaries/{diaryId}`: 특정 일기 수정 (Update)
* `DELETE /api/diaries/{diaryId}`: 특정 일기 삭제 (Delete)

### 4. 📅 일일 통합 조회 (Daily Record)

* `GET /api/daily-records/member/{memberId}/date/{date}`: 특정 날짜의 거래 내역과 일기를 **한 번에** 조회

<br>

## 🚀 다음 목표

1.  **[1순위] 보안 적용:** Spring Security와 JWT(JSON Web Token)를 도입하여, 로그인한 본인만 자신의 데이터에 접근할 수 있도록 API 보안을 강화할 예정입니다.
2.  **[2순위] DB 교체:** H2(임시) DB에서 MySQL(영구) DB로 교체하여 데이터를 영구적으로 저장할 예정입니다.
3.  **[3순위] 프론트엔드:** React를 사용하여 이 API와 통신하는 웹 애플리케이션 화면을 개발할 예정입니다.