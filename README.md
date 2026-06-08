# 항공 스케줄 API 프로젝트
> MCP & A2A 오케스트레이션 + Spring AI 1.1.4 학습을 위한 Spring Boot 실습 프로젝트


<details>
<summary style="font-weight: bold; font-size: x-large">Step1. Spring AI 환경 설정</summary>
<div>

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
</div>
</details>