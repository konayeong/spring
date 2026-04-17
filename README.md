# Spring Boot MVC 결산과제
## [NHN Mart 고객센터]

### JSESSIONID
> 서버(톰캣)가 클라이언트(브라우저)를 식별하기 위해 발급하는 세션 ID
- 흐름
  - 클라이언트 최초 요청
  - 서버가 세션 생성
  - 서버가 응답에 쿠키로 내려줌
  - 이후 요청마다 브라우저가 자동으로 보냄
  - 서버는 이 값으로 같은 사용자인지 구분
- 핵심 역할
  - 로그인 상태 유지
  - 사용자별 데이터 저장(session.setAttribute)
  - 서버에서 상태 관리
- 세션 유지 방식 2가지
  - 쿠키 방식 (기본)
    - Cookie: JSESSIONID=ABC123
  - URL Rewriting 방식 (fallback)
    - 쿠키 못 쓸때만 사용
    - URL에 세션 노출 
- 문제 상황
  - 쿠키 삭제 후 로그인 시 URL Rewriting 방식으로 작동하면서 error page 반환
  - 해결
    - `server.servlet.session.tracking-modes=cookie`
    - URL 방식 쓰지 말고, 무조건 쿠키만 쓰도록 설정