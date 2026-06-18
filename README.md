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
            - Function Calling:`0.0`(일관된 결과 필요)
            - 질문-답변:`0.7`(적절한 균형)
            - 창작:`1.0+`창의적 응답)
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
> LLM이 할 수 없는 외부 기능들
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

### Tool Calling Callback
> Function Calling의 모든 단계를 가로채서 로깅, 모니터링, 수정할 수 있는 인터페이스
- 등록 : ChatClient에 주입 받아서 등록
  - `.defaultAdvisor(new SimpleLoggerAdvisor());` 
### Advisor
>LLM에 요청이 전달되기 전/후에 로깅이나 모니터링 로직을 실행하는 것
#### ChatClient Advisor
>ChatClient의 요청/응답 과정에 개입하여 로깅, 수정, 모니터링 할 수 있는 메커니즘
- Spring AI 내장 Advisor
    - SimpleLoggerAdvisor : LLM에게 보낸 요청과 받은 응답을 자동으로 로깅
    - QuestionAnswerAdvisor : 문서 검색 결과를 컨텍스트에 추가 - RAG
    - PromptChatMemoryAdvisor : 대화 기록 유지
    - VectorStoreChatMemoryAdvisor : 벡터 DB에서 관련 문서 검색
- 사용자 정의 로깅
  - 실행 시간, 요청 추적, 상세 로깅 등
  - `implements CallAdvisor`
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step5. 다중 Function Calling과 MCP Tool 패턴</summary>

### MCP(Model Context Protocol) Tool 패턴
> LLM이 사용할 수 있는 도구를 체계적으로 설계하고 구현하는 패턴 (외부 도구를 연결하기 위한 프로토콜)

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

### ToolResultCapture 패턴
> MCP Tool의 호출 결과를 ThreadLocal에 저장하는 유틸리티 클래스
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step6. 항공편 검색 Tool 구현</summary>

### RestClient
> 동기식 HTTP 클라이언트를 위한 현대적인 API

#### RestClient vs RestTemplate vs WebClient
| **항목** | **RestTemplate** | **WebClient** | **RestClient** |
| --- | --- | --- | --- |
| **출시** | Spring 3.x | Spring 5.x | Spring 6.1 |
| **스타일** | 명령형 | 반응형 | 유창한 API |
| **차단/비차단** | 차단(Blocking) | 비차단(Non-blocking) | 차단(Blocking) |
| **복잡도** | 복잡함 | 중간 | 간결함 |
| **권장 사용** | 레거시 | 비동기 필요 시 | 일반적인 HTTP 호출 |

#### ApiProperties.java
- application.yml에 있는 설정값을 타입 안전하게 관리하기 위함
- 장점
    - 설정값을 한 곳에서 관리
    - 설정이 늘어나도 코드가 깔끔
    - 타입 변환을 자동으로 해줌

### Agent
> 하나의 작은 실행 주체 <br>
> 각 에이전트가 **하나의 변환/처리만** 담당

#### 코디네이터 에이전트
- 하위 에이전트를 조율하여 항공편 검색 작업을 수행하는 에이전트
- 어떤 순서로 어떤 에이전트를 호출할지 조율
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step7. 공항/항공사 정보 Tool 구현</summary>

### 에이전트 위임(Delegation) 패턴
> MCP Tool이 직접 비즈니스 로직을 수행하지 않고, 전문화된 에이전트 클래스에게 작업을 위임하는 설계 패턴

#### 장점
- 단일 책임 : Tool은 진입점만 담당, Agent가 실제 작업 수행
- 재사용성 : Agent를 여러 Tool에서 공유 가능
- 테스트 용이
- 확장성

### 전체 아키텍처 - 계층별 책임
- MCP Tool : LLM 진입점, `@Tool` 어노테이션 결과 제한, ToolResultCapture
- Agent : 작업 조율, 하위 에이전트 호출
- 전문 Agent : 단일 기능 수행
- Service : 외부 API 호출, 응답 파싱
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step8. A2A 개념과 단일 에이전트 구현 </summary>

### A2A (Agent-to-Agent)
> 여러 전문화된 에이전트가 협력하여 복잡한 작업을 해결하는 패턴

#### 통신 패턴
1. 순차적 통신 : 한 에이전트의 결과를 다른 에이전트의 입력으로 전달
2. 병렬 통신
3. 파이프라인 : 중간 결과를 계속 흘려보냄

#### Agent
> 자율적으로 행동하고 목표를 달성하기 위해 환경과 상호작용하는 소프트웨어 객체
- 특징
    - 자율성 : 스스로 결정하고 행동
    - 반응성 : 환경 변화에 즉시 대응
    - 능동성 : 목표 달성을 위해 주도적 행동
    - 사회성 : 다른 Agent와 협력

#### Agent vs Service
- Service : 여러 기능 포함 가능, Controller → Service
- Agent : 단일 책임 (SRP), Coordinator → Agent

### A2A vs MCP Tool
| 항목 | MCP Tool | A2A Agent |
| --- | --- | --- |
| 호출자 | LLM이 직접 호출 | Coordinator가 호출 |
| 제어권 | LLM이 가짐 | Coordinator가 가짐 |
| 복잡도 | 단일 기능 | 복합 기능 |
| 통신 방식 | LLM → Tool | Agent → Agent |
| 용도 | 간단한 함수 호출 | 복잡한 작업 흐름 |
</details>

<details style="margin-top: 20px">
<summary style="font-weight: bold; font-size: x-large">Step9. Coordinator / Orchestration Pattern</summary>

### Coordinator (개발자 중심 제어)
> Controller -> Coordinator -> Tool 호출 순서 고정
- 호출 흐름을 개발자가 100% 결정
- 어떤 Tool을 언제 호출할지 코드에 다 있음
- 결과도 직접 모음

### Orchestrator (LLM 중심 제어)
> User Prompt -> LLM -> Tool 선택/호출 -> 결과 조합
- Tool을 누가 호출할지 LLM이 결정
- 실행 흐름이 고정되어 있지 않음
- "필요하면 Tool 호출" 구조

</details>
