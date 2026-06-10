# 항공 스케줄 API 프로젝트
> MCP & A2A 오케스트레이션 + Spring AI 1.1.4 학습을 위한 Spring Boot 실습 프로젝트


<details>
<summary style="font-weight: bold; font-size: x-large">Step1. Spring AI 환경 설정</summary>

### LLM (Large Language Model, 대규모 언어 모델)

> 방대한 텍스트 데이터로 학습한 인공지능 모델
- **동작 과정** : 입력 → 토큰화 → 모델 처리 → 출력
- **주요 용어 설명**
    - Token : LLM이 처리하는 최소 단위
    - Prompt : LLM에게 전달하는 입력
    - Context : 대화의 맥락 정보
    - Completion : LLM이 생성하는 답변
- **파라미터**
    - Temperature : 창의성을 조절하는 파라미터 (0.0 ~ 2.0)
        - **추천 설정**:
            - Function Calling: `0.0` (일관된 결과 필요)
            - 질문-답변: `0.7` (적절한 균형)
            - 창작: `1.0+` (창의적 응답)
    - Max Tokens : LLM이 생성할 수 있는 최대 토큰 수
    - Top-P, Top-K : 다음 토큰을 선택할 때 고려할 후보 수
        - Top-K : 상위 n개 후보
        - Top-P : 누적 확률 N%까지 후보 고려

### Spring AI

> Spring 생태계에서 AI 모델을 쉽게 사용할 수 있게 해주는 프레임워크

- ChatClient : LLM과 대화하는 빌더 패턴 API
- ChatModel : 실제 LLM과 연결되는 구현체 (OllamaChatModel, GoogleGenAiChatModel)
  - Ollama : 속도 느림, 무료, 개발/테스트
  - Gemini : 속도 빠름, 유료, 실시간 검색/운영 환경
- `@Tool` : LLM이 호출할 수 있는 함수 정의
- `@ToolParam` : Tool 파라미터 설명
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step2. ChatClient 기초와 LLM 연동</summary>

### ChatClient
> Spring AI에서 LLM과 대화하기 위한 유창한 API를 제공하는 인터페이스
- SDK마다 사용법이 달라 모델 교체 시 코드 대폭 수정
- ChatClient를 사용함으로써 모델 교체도 설정만 변경, 코드는 그대로

#### 설계 원칙
- Fluent API : 메서드 체이닝으로 직관적인 코드 작성
- Builder Pattern
- 불변성 : 각 호출은 독립적이고 부작용이 없음

#### ChatClient API 상세 분석
- 메서드 체이닝
    - `chatClient.prompt().user("질문").call().content();`
        - prompt() : 프롬프트 빌더 시작
        - user() : 사용자 질문 설정
        - call() : LLM 호출 (동기)
        - content() : 응답 내용 추출

#### 고급 기능
1. 시스템 프롬프트 설정
    - `.system()`
2. 대화 기록 유지
    - `messages(history) : List<Message> history`
3. 스트리밍 응답
    - `.stream()`
    - `.forEach(chunk → {})` : 토큰 단위 출력 가능
4. 구조화된 응답 (Entity)
    - `.entity(class)` : JSON → 자바 객체

### curl
> Spring API를 브라우저 없이 터미널에서 직접 호출하는 도구
- 하는 일
    - HTTP 요청 생성 : GET / POST 등
    - 서버 호출 : URL로 요청 전송
    - 응답 출력 : JSON / 텍스트 그대로 출력
- curl 자동 인코딩

    ```bash
    curl -G "http://localhost:8080/api/chat/ollama" --data-urlencode "question=안녕하세요 반갑습니다"
    ```
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step3. Function Calling 기초</summary>

### Function Calling
> LLM이 자연어 질문을 분석하여 적절한 자바 메서드를 자동으로 호출하는 기술

#### 작동 원리
1. LLM의 추론 (Reasoning)
    - 의도 파악, 함수 선택, 파라미터 추출
2. 함수 호출 (Function Invocation)
    - LLM의 결정에 따라 실제 자바 메서드 호출
3. 결과 통합 (Response Synthesis)
    - 함수 실행 결과를 사용자에게 자연어로 설명

#### 장점
- 자연어 인터페이스 : 사용자가 복잡한 API 몰라도 됨
- 자동 파라미터 추출
- 유연한 Tool 선택
- 확장성 : 새로운 Tool 추가가 쉬움

#### 패턴
1. 단일 Tool
2. 다중 Tool (Chaining)
    ```
    사용자 → LLM → Tool1 → 결과1 → LLM → Tool2 → 결과2 → 응답
    ```

3. 병렬 Tool 호출
    ```
    사용자 → LLM → Tool1 ─┐
                    ├→ 결과 통합 → 응답
                    → Tool2 ─┘
    ```

### Tool
#### `@Tool`
- LLM이 이 메서드를 호출할 수 있게 함
- description : LLM이 이 함수가 무엇을 하는지 설명
    - 좋은 Description의 조건
        - 명확성 : 무엇을 하는지 명확히 설명
        - 파라미터 설명 : 각 파라미터의 역할 설명
        - 반환값 설명 : 무엇을 반환하는지 설명
        - 사용 예시 : 언제 사용하는지 예시

#### `@ToolParam`
- 파라미터에 대한 설명
- description : 파라미터에 대한 설명
- required : 필수 파라미터 여부 (기본값: ture)

#### 등록 방법

- ChatClient.Builder에 등록 [권장]
- 여러 Tool 등록
- 패키지 스캔 (자동 등록)
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step4. Tool Callback</summary>

보류
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step5.다중 Function Calling과 MCP Tool 패턴</summary>

### MCP Tool 패턴

> LLM이 사용할 수 있는 도구를 체계적으로 설계하고 구현하는 패턴
>

#### 핵심 원칙

- 단일 책임, 명확한 인터페이스, 독립성, 재사용성

#### 등록

- 명시적 등록 (권장)
- 자동 스캔

    ```java
    @Configuration
    public class ChatClientConfig {
    
        @Bean
        @Primary
        public ChatClient.Builder ollamaChatClientBuilder(
                @Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
                ApplicationContext context) {
    
            // 모든 @Tool 빈 자동 수집
            Map<String, Object> tools = context.getBeansWithAnnotation(Component.class);
            List<Object> toolList = tools.values().stream()
                    .filter(bean -> hasToolMethods(bean.getClass()))
                    .toList();
    
            return ChatClient.builder(ollamaChatModel)
                    .defaultTools(toolList.toArray(new Object[0]));
        }
    
        private boolean hasToolMethods(Class<?> clazz) {
            return Arrays.stream(clazz.getMethods())
                    .anyMatch(method -> method.isAnnotationPresent(Tool.class));
        }
    }
    ```


#### Tool Description 최적화 (5가지 원칙)

- 무엇을 하는지
- 언제 사용하는지
- 파라미터 설명
- 반환값 설명
- 제한 사항

#### Tool 설계 패턴

- 조회 Tool (Read-Only) : 여러 번 호출해도 안전
- 명령 Tool (Command) : 한 번만 호출해야 함, 권한 체크 필요
- 필터링 Tool

#### Tool 성능 최적화

- 결과 제한
- 비동기 실행
- 캐싱
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step6. 항공편 검색 Tool 구현</summary>

보류
</details>