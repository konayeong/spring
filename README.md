# Spring 결산 과제
## Spring Core
### 상수도 요금 계산 CLI 프로그램 (Spring Boot)
- 파일(CSV/JSON) 기반으로 요금표 및 회원 정보를 로드
- Property 설정에 따라 CSV ↔ JSON 파싱 방식 전환 (@ConditionalOnProperty())
- Profile 설정에 따라 결과 출력 언어 변경 (한글/영어)
- 로그인 기반 사용자 인증 후 요금 조회 가능 (메모리 기반)
- 주요 기능
  - 파일 파싱 인터페이스 및 구현 (CSV, JSON)
  - 출력 포맷 인터페이스 및 다국어 지원
  - 로그인/로그아웃 기능
  - 사용량 기반 요금 계산
- AOP 적용
  - 로그인/로그아웃 요청 → account.log 기록
  - 요금 조회 및 기타 요청 → price.log 기록
  - 비로그인 사용자 요청 시 AOP에서 차단
- 기타
- 파일 경로는 외부 설정으로 주입
- CLI 기반 실행 (Spring Shell)
