# spring-security-final

# 과제 제출 방법
1. 해당 레포지토리를 clone 합니다.
2. git checkout -b feature/ATGG_03_본인 학번으로 checkout 합니다.
3. 과제 완료 후 커밋 -> 푸시하면 됩니다.
4. 필요한 의존성은 pom.xml에 개별적으로 추가합니다. 추가하셨다면 pom.xml또한 같이 커밋 후 푸시 해주셔야 합니다.

# Member (기본)

1. 멤버를 생성하는 API
    1. ~~text/csv 지원~~
    2. ~~중복 처리~~
2. 멤버 조회
    1. ~~_단건 조회 API_~~
    2. ~~전체 조회 API~~
    3. ~~role은 소문자로 응답합니다 (enum을 이용)~~
3. 멤버 단건을 조회하는 API
   1\. ~~text/csv 지원~~
4. ~~멤버 데이터는 Redis를 이용하여 저장합니다.~~
5. ~~Password는 암호화 해서 저장합니다. 꼭!!! 저장할때부터!!~~
6. ~~Role은 ADMIN, MEMBER, GOOGLE 로 구분합니다~~ (조회시에는 소문자로 @JsonValue를 사용하지않고)

MEMBER

```java
private String id;
private String name;
private String password;
private Integer age;
private Role role;
```
## 요청 공통 설정

* [x] 멤버 조회(GET)는 xml 형식으로 받을 수 있도록 합니다.
* [x] HandlerMethodArgumentResolver를 이용해 Pageable에 기본값을 수정합니다.
    * [x] 기본 페이징 size 5
    * [x] MAX 페이징 10

## 인가

* [x] ADMIN만 접속가능한 페이지
* [x] MEMBER만 접속가능한 페이지
* [x] GOOGLE 만 접속가능한 페이지
* [x] 각 페이지에 인가 TEST 케이스를 작성합니다 (200/403)

## 로그인

* [x] 사용자는 회원가입한 id,password로 로그인 가능합니다.
* [x] 레디스에 세션, 사용자 id 를 저장합니다.
* [x] 사용자가 로그인을 5번 실패(패스워드가 틀리면)하면 로그인을 차단합니다.
    * [x] 레디스를 이용하여 등록합니다.
        * [x] 실패 카운트 데이터에 TTL을 넣어둡니다.
    * [x] 차단되었다고 메신저로 알림을 보냅니다.
* [x] 로그아웃 핸들러에서 쿠키 세션과, 레디스 세션을 제거합니다.

## OAUTH

* [x] 구글 로그인을 추가로 지원합니다.
* [ ] 구글 로그인 성공시 기존 세션을 무효화합니다.
    * [?] 이미 아이디/패스워드로 로그인 사람은 로그인이 안되는 현상 제거. 
* [x] 구글 로그인 한 사용자는 Role이 GOOGLE 로 지정합니다.
* [x] 선택: 구글 로그인한 사용자도 서버 재시작시 로그인이 유지되도록 해주세요.(여러 방법을 고민해보세요) 