# 📌 프로젝트 이름

이 프로젝트는 **예약 및 상점 관리 시스템**으로, 유저(점주,사용자) 와 상점 간 예약을 원활하게 진행하고 관리할 수 있도록 설계되었습니다.

---

## 🚀 주요 기능

### ✅ 회원 관리
- **회원 가입 및 로그인** (Spring Security 기반)
- **UserDetails 구현**
- **회원 예약 관리** (조회 및 삭제)

### 🏪 상점 관리
- **상점 생성, 조회, 수정, 삭제 (점주)**
- **상점 수정 및 삭제 (점주)**
- **상점 목록 정렬 기능** (가나다순, 별점순, 거리순)

### 📅 예약 시스템
- **예약 서비스 구현**
- **키오스크 예약 지원**
- **예약 승인 및 거절 기능(점주)**
- **예약 권한에 따른 삭제 기능**

### 💬 리뷰
- **리뷰 작성, 조회, 수정, 삭제**

---

## 🔍 기술 스택

- **Backend**: Java, Spring Boot
- **Security**: Spring Security 
- **Database**: MySQL
- **ORM**: JPA (Hibernate)
- **Build Tool**: Gradle 
- **Version Control**: Git, GitHub
---
## 🔨 데이터베이스 구조(ERD)
![initial](https://github.com/user-attachments/assets/ac6b2664-bcb1-4e32-aad8-0228006cc5d1)

---

## 🏗️ 폴더 구조

```bash
src/
 ├── main/
 │   ├── java/com/example/project/
 │   │   ├── controller/      # 컨트롤러 레이어
 │   │   ├── service/         # 비즈니스 로직
 │   │   ├── repository/      # 데이터 접근 레이어
 │   │   ├── entity/          # JPA 엔티티
 │   │   ├── dto/             # 데이터 전송 객체
 │   │   ├── config/          # 설정 파일
 │   │   ├── security/        # spring security 파일
 │   ├── resources/
 │   │   ├── application.properties  # 환경설정 파일
