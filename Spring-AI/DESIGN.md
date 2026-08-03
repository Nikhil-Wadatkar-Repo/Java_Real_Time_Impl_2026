# Spring AI ChatBot Application - Design Document

## Overview

This document outlines the design and architecture of the Spring AI ChatBot application, which integrates with OpenAI's GPT models to provide intelligent conversational capabilities through REST APIs.

## System Architecture

### High-Level Architecture Diagram

```
┌──────────────────────────────────────────────────────────┐
│                  REST Client (Postman/cURL)              │
└────────────┬─────────────────────────────────────────────┘
             │
             │ HTTP Request
             ▼
┌──────────────────────────────────────────────────────────┐
│            Spring Boot Application (Port 8083)           │
│  ┌────────────────────────────────────────────────────┐  │
│  │  ChatBotController                                │  │
│  │  - @PostMapping("/api/chat/ask")                  │  │
│  │  - @PostMapping("/api/chat/ask-with-context")    │  │
│  │  - @GetMapping("/api/chat/health")               │  │
│  └────────────────┬─────────────────────────────────┘  │
│                   │                                      │
│  ┌────────────────▼─────────────────────────────────┐  │
│  │  ChatBotService                                  │  │
│  │  - askQuestion(message)                          │  │
│  │  - askWithContext(system, question)             │  │
│  │  - callOpenAiApi(messages)                      │  │
│  └────────────────┬─────────────────────────────────┘  │
│                   │                                      │
│  ┌────────────────▼─────────────────────────────────┐  │
│  │  ChatClientConfig (RestTemplate Bean)            │  │
│  └────────────────┬─────────────────────────────────┘  │
└────────────────┬────────────────────────────────────────┘
                 │
                 │ HTTPS Request
                 │ (Bearer Token Auth)
                 ▼
         ┌─────────────────────┐
         │  OpenAI API Server  │
         │  (api.openai.com)   │
         └─────────────────────┘
```

## Component Design

### 1. **ChatBotController** (`controller/ChatBotController.java`)

**Purpose:** Expose REST endpoints for chat operations

**Key Methods:**
- `ask(ChatRequest)` - Simple question answering
- `askWithContext(systemPrompt, userQuestion)` - Contextual Q&A
- `health()` - Health check endpoint

**Responsibilities:**
- Validate incoming requests
- Handle HTTP routing and response serialization
- Error handling and status codes
- Request/Response logging

**Request Validation:**
- Empty message check
- Null parameter validation
- Proper HTTP status codes (400, 500, 200)

### 2. **ChatBotService** (`service/ChatBotService.java`)

**Purpose:** Implement business logic for AI interactions

**Key Methods:**
- `askQuestion(String question)` - Process simple queries
- `askWithContext(String systemPrompt, String userQuestion)` - Process contextual queries
- `callOpenAiApi(JsonArray messages)` - Direct OpenAI API calls

**Features:**
- Constructs properly formatted OpenAI API requests
- Manages conversation context with system and user roles
- Parses JSON responses from OpenAI
- Comprehensive error handling and logging

**Configuration:**
- Model: `gpt-3.5-turbo` (cost-effective, fast)
- Max Tokens: 2000
- Temperature: 0.7 (balanced creativity)

### 3. **ChatClientConfig** (`config/ChatClientConfig.java`)

**Purpose:** Spring configuration for HTTP client

**Bean:**
- `RestTemplate` - HTTP client for OpenAI API calls

**Why RestTemplate:**
- Built-in Spring framework
- No external SDK dependencies
- Reliable and well-tested
- Flexible for custom HTTP handling

### 4. **Model Classes** (`model/`)

#### ChatRequest
```java
{
  "message": "Your question here"
}
```

#### ChatResponse
```java
{
  "response": "AI generated response",
  "status": "success|error"
}
```

## API Endpoints

### 1. Simple Chat Endpoint

**Endpoint:** `POST /api/chat/ask`

**Description:** Ask a simple question and get an AI response

**Request Body:**
```json
{
  "message": "What is the capital of France?"
}
```

**Response (200 OK):**
```json
{
  "response": "The capital of France is Paris...",
  "status": "success"
}
```

**Error Responses:**
- `400 Bad Request`: Empty message
- `500 Internal Server Error`: API failure

### 2. Contextual Chat Endpoint

**Endpoint:** `POST /api/chat/ask-with-context`

**Description:** Ask a question with system context/instructions

**Query Parameters:**
- `systemPrompt`: System instruction (e.g., "You are a Python expert")
- `userQuestion`: The actual question

**Example:**
```
POST /api/chat/ask-with-context?systemPrompt=You%20are%20a%20Java%20expert&userQuestion=What%20is%20dependency%20injection?
```

**Response (200 OK):**
```json
{
  "response": "Dependency injection is a design pattern...",
  "status": "success"
}
```

### 3. Health Check Endpoint

**Endpoint:** `GET /api/chat/health`

**Response:**
```
ChatBot Service is running!
```

## Message Flow

### Flow 1: Simple Question

```
1. Client sends: POST /api/chat/ask
   {
     "message": "What is AI?"
   }

2. ChatBotController.ask() receives request

3. Validates: message is not null/empty

4. Calls: ChatBotService.askQuestion("What is AI?")

5. ChatBotService constructs JSON:
   {
     "model": "gpt-3.5-turbo",
     "messages": [
       {
         "role": "user",
         "content": "What is AI?"
       }
     ],
     "max_tokens": 2000,
     "temperature": 0.7
   }

6. RestTemplate.postForObject() sends to OpenAI API

7. OpenAI API returns:
   {
     "choices": [
       {
         "message": {
           "content": "AI is artificial intelligence..."
         }
       }
     ]
   }

8. Service parses response content

9. Returns response to controller

10. Controller wraps in ChatResponse:
    {
      "response": "AI is artificial intelligence...",
      "status": "success"
    }

11. Client receives 200 OK with response
```

### Flow 2: Contextual Question

```
1. Client sends: POST /api/chat/ask-with-context?systemPrompt=...&userQuestion=...

2. ChatBotController.askWithContext() receives request

3. Validates: both parameters are not null/empty

4. Calls: ChatBotService.askWithContext(systemPrompt, userQuestion)

5. ChatBotService constructs JSON with 2 messages:
   {
     "model": "gpt-3.5-turbo",
     "messages": [
       {
         "role": "system",
         "content": "You are a Java expert..."
       },
       {
         "role": "user",
         "content": "What is dependency injection?"
       }
     ],
     "max_tokens": 2000,
     "temperature": 0.7
   }

6-11. Same as Flow 1: REST call to OpenAI and response formatting
```

## Data Models

### OpenAI Request Format

```json
{
  "model": "gpt-3.5-turbo",
  "messages": [
    {
      "role": "system|user|assistant",
      "content": "message content"
    }
  ],
  "max_tokens": 2000,
  "temperature": 0.7
}
```

### OpenAI Response Format

```json
{
  "id": "chatcmpl-...",
  "object": "chat.completion",
  "created": 1234567890,
  "model": "gpt-3.5-turbo",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Response content here"
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 10,
    "completion_tokens": 100,
    "total_tokens": 110
  }
}
```

## Configuration & Setup

### application.properties

```properties
# Server
spring.application.name=Spring-AI-ChatBot
server.port=8083

# OpenAI API
openai.api-key=${OPENAI_API_KEY:your-api-key-here}

# Logging
logging.level.root=INFO
logging.level.com.mco.springai=DEBUG
```

### Environment Variables

Set before running:
```bash
export OPENAI_API_KEY="sk-..."
```

## Technologies Used

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 4.1.0 |
| Java | Java | 21+ |
| HTTP Client | RestTemplate | Spring Web |
| JSON Processing | Gson | 2.10.1 |
| Build Tool | Maven | 3.6+ |
| Logging | Slf4j | Built-in |
| Code Generation | Lombok | Latest |

## Error Handling

### Exception Handling Strategy

1. **Validation Errors** (400)
   - Empty message
   - Missing parameters
   - Null values

2. **API Errors** (500)
   - OpenAI API failure
   - Invalid API key
   - Network timeout
   - Parse errors

3. **Logging**
   - INFO: Request received
   - DEBUG: Request details
   - ERROR: Exceptions with stack trace

### Example Error Response

```json
{
  "response": "Error: Failed to get response from AI: Invalid API key provided",
  "status": "error"
}
```

## Security Considerations

1. **API Key Management**
   - Use environment variables, not hardcoded
   - Never commit secrets to git
   - Use .gitignore for local configs

2. **HTTPS Communication**
   - OpenAI API uses HTTPS (automatic)
   - Encrypt API key in transit

3. **Input Validation**
   - Validate message content
   - Sanitize inputs before sending to API

4. **Rate Limiting** (Future)
   - Implement request throttling
   - Prevent abuse

## Performance Characteristics

| Metric | Value | Notes |
|--------|-------|-------|
| Request Timeout | 30-60 seconds | Typical API response time |
| Max Response Tokens | 2000 | Limits response length |
| Temperature | 0.7 | Balanced creativity |
| Model | gpt-3.5-turbo | Fast & cost-effective |

## Testing

### Manual Testing with cURL

```bash
# Simple question
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"What is Java?"}'

# With context
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%20are%20a%20Java%20expert&userQuestion=What%20is%20OOP?" \
  -H "Content-Type: application/json"

# Health check
curl http://localhost:8083/api/chat/health
```

### Test Coverage

- Unit tests for ChatBotService
- Integration tests for Controller
- Error scenario testing
- API contract testing

## Future Enhancements

1. **Multi-turn Conversations**
   - Store conversation history
   - Context persistence
   - User sessions

2. **Advanced Features**
   - Streaming responses
   - Image input support
   - Custom models
   - Fine-tuned models

3. **Production Ready**
   - Rate limiting
   - Authentication (JWT)
   - Database persistence
   - Caching layer
   - Monitoring & metrics
   - Load balancing

4. **User Experience**
   - Web UI/Dashboard
   - Conversation export
   - Response formatting
   - Syntax highlighting

## Deployment

### Local Development

```bash
mvn spring-boot:run
```

### Production Deployment

```bash
java -jar Spring-AI-0.0.1-SNAPSHOT.jar \
  --OPENAI_API_KEY="sk-..." \
  --server.port=8080
```

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk
COPY target/Spring-AI-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## File Structure

```
Spring-AI/
├── src/main/java/com/mco/springai/
│   ├── SpringAiApplication.java
│   ├── config/
│   │   └── ChatClientConfig.java
│   ├── controller/
│   │   └── ChatBotController.java
│   ├── model/
│   │   ├── ChatRequest.java
│   │   └── ChatResponse.java
│   └── service/
│       └── ChatBotService.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/...
├── pom.xml
└── README.md
```

## Conclusion

This chatbot application provides a clean, scalable architecture for integrating OpenAI's GPT models with Spring Boot. The design follows SOLID principles and Spring best practices, making it easy to extend and maintain.

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenAI API Documentation](https://platform.openai.com/docs/api-reference)
- [RestTemplate Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)
