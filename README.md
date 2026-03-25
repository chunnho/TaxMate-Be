# 💰 TaxMate - 프리랜서 세금 관리 서비스

> 3.3% 원천징수 프리랜서를 위한 수입·지출·세금 관리 플랫폼

프리랜서 600만 시대, 매달 수입은 들어오는데 세금이 얼마인지 모르겠다면?
TaxMate가 수입 관리부터 종소세 예측까지 한 번에 해결해 드립니다.

## ✨ 주요 기능

### 🔓 로그인 없이 바로 사용
- **원천징수 계산기** — 금액만 입력하면 3.3% 원천징수액과 실수령액 즉시 계산
- **종소세 간편 계산기** — 연간 수입으로 예상 종합소득세 확인

### 🔐 로그인 후 사용
- **수입 관리** — 클라이언트별 수입 등록, 원천징수 자동 계산
- **지출 관리** — 카테고리별 지출 기록, 경비 처리 여부 체크
- **대시보드** — 월별·연간 수입/지출 요약, 종소세 예상액, 환급·추가납부 예측

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Kotlin |
| Framework | Spring Boot 3.4 |
| Auth | Spring Security + JWT |
| ORM | Spring Data JPA |
| DB | MySQL 8.0 |
| Cache | Redis 7.0 |
| Infra | Docker Compose |
| Build | Gradle (Kotlin DSL) |
| Docs | Swagger (SpringDoc) |

## 🚀 시작하기

### 사전 준비
- Java 17+
- Docker Desktop

### 실행

```bash
# 1. 저장소 클론
git clone https://github.com/chunnho/TaxMate-Be.git
cd TaxMate-Be

# 2. MySQL, Redis 실행
docker-compose up -d

# 3. 서버 실행
./gradlew bootRun

# 4. API 확인
curl http://localhost:8080/api/health
# → {"status":200,"message":"success","data":{"status":"ok"}}
```

### Docker 컨테이너 관리

```bash
docker-compose up -d     # 시작
docker-compose down      # 중지
docker-compose down -v   # 중지 + 데이터 초기화
docker ps                # 상태 확인
```

## 📡 API 엔드포인트

### 세금 계산기 (비로그인)
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/calculator/withholding` | 원천징수 계산 (3.3%) |
| POST | `/api/calculator/income-tax` | 종소세 간편 계산 |

### 수입 관리 (로그인 필요)
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/incomes` | 수입 등록 |
| GET | `/api/incomes` | 수입 목록 조회 |
| GET | `/api/incomes/{id}` | 수입 상세 조회 |
| PUT | `/api/incomes/{id}` | 수입 수정 |
| DELETE | `/api/incomes/{id}` | 수입 삭제 |

### 지출 관리 (로그인 필요)
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/expenses` | 지출 등록 |
| GET | `/api/expenses` | 지출 목록 조회 |
| GET | `/api/expenses/{id}` | 지출 상세 조회 |
| PUT | `/api/expenses/{id}` | 지출 수정 |
| DELETE | `/api/expenses/{id}` | 지출 삭제 |

### 대시보드 (로그인 필요)
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/dashboard/monthly` | 월별 수입·지출 요약 |
| GET | `/api/dashboard/yearly` | 연간 요약 + 종소세 예상 |

### 인증
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| POST | `/api/auth/refresh` | 토큰 갱신 |

## 📁 프로젝트 구조

```
src/main/kotlin/com/taxmate/
├── TaxmateApplication.kt
├── config/                  # 설정 (Security, JPA)
├── common/                  # 공통 모듈
│   ├── entity/              #   BaseEntity
│   ├── api/                 #   ApiResponse (통일된 응답)
│   └── exception/           #   ErrorCode, 전역 예외 처리
├── controller/              # API 엔드포인트
├── service/                 # 비즈니스 로직
├── entity/                  # JPA Entity (DB 매핑)
├── repository/              # DB 접근
└── dto/                     # 요청/응답 DTO
```

## 🗺 로드맵

- [x] **Phase 1 — MVP**
  - [x] 프로젝트 셋업 (Spring Boot + Docker)
  - [x] 공통 모듈 (BaseEntity, ApiResponse, ErrorCode)
  - [ ] 세금 계산기 API
  - [ ] 회원가입/로그인 (JWT)
  - [ ] 수입 CRUD
  - [ ] 지출 CRUD
  - [ ] 대시보드 (월별/연간)
  - [ ] Swagger 연동
- [ ] **Phase 2 — 확장**
  - [ ] 소셜 로그인 (카카오, 구글, 애플)
  - [ ] 종소세 신고 기간 알림
  - [ ] 업종별 경비율 가이드
  - [ ] 클라이언트 관리 + 미수금
  - [ ] PDF 리포트 다운로드
- [ ] **Phase 3 — 고도화**
  - [ ] 복합 소득 관리 (근로+사업+기타)
  - [ ] 세무사 연결
  - [ ] 세금 캘린더

## 📄 라이선스

MIT License
