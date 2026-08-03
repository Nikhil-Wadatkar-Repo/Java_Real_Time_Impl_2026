# Spring AI ChatBot Application

A Spring Boot application that integrates with OpenAI to create a conversational chatbot. The application exposes REST APIs to ask questions and get intelligent responses powered by GPT-4.

## Architecture

```
┌─────────────────────────────────────────────────────┐
│             REST Controller                         │
│  /api/chat/ask (POST)                               │
│  /api/chat/ask-with-context (POST)                  │
│  /api/chat/health (GET)                             │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│         ChatBotService (Business Logic)            │
│  - askQuestion()                                   │
│  - askWithContext()                                │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│    Spring AI ChatClient (OpenAI Integration)       │
│  - Communicates with OpenAI API                    │
│  - Manages prompt building                        │
└─────────────────────────────────────────────────────┘
```

## Project Structure

```
src/main/java/com/mco/springai/
├── SpringAiApplication.java          # Main Spring Boot Application
├── config/
│   └── ChatClientConfig.java          # ChatClient Configuration
├── controller/
│   └── ChatBotController.java         # REST API Endpoints
├── model/
│   ├── ChatRequest.java               # Request DTO
│   └── ChatResponse.java              # Response DTO
└── service/
    └── ChatBotService.java            # Business Logic Service
```

## Prerequisites

1. **Java 21+** - Required by Spring Boot 4.1.0
2. **Maven 3.6+** - Build tool
3. **OpenAI API Key** - Get from https://platform.openai.com/account/api-keys

## Setup Instructions

### 1. Configure OpenAI API Key

You can set the API key in one of two ways:

**Option A: Environment Variable (Recommended)**
```bash
# Windows (PowerShell)
$env:OPENAI_API_KEY="your-openai-api-key-here"

# Windows (Command Prompt)
set OPENAI_API_KEY=your-openai-api-key-here

# Linux/Mac
export OPENAI_API_KEY="your-openai-api-key-here"
```

**Option B: application.properties**
Edit `src/main/resources/application.properties`:
```properties
spring.ai.openai.api-key=your-openai-api-key-here
```

### 2. Build the Application

```bash
cd Spring-AI
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/Spring-AI-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8083`

## API Endpoints

### 1. Simple Chat (Ask a Question)

**Endpoint:** `POST /api/chat/ask`

**Request:**
```bash
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"What is the capital of France?"}'
```

**Request Body:**
```json
{
  "message": "What is the capital of France?"
}
```

**Response:**
```json
{
  "response": "The capital of France is Paris. It is located in the north-central part of France and is the country's largest city. Paris is known for its iconic landmarks such as the Eiffel Tower, Notre-Dame Cathedral, and the Louvre Museum.",
  "status": "success"
}
```

### 2. Chat with Context (System Prompt)

**Endpoint:** `POST /api/chat/ask-with-context`

This endpoint allows you to provide a system context/instruction to the AI.

**Request:**
```bash
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%20are%20a%20helpful%20JavaScript%20expert&userQuestion=How%20do%20I%20create%20a%20promise%20in%20JavaScript?" \
  -H "Content-Type: application/json"
```

**Query Parameters:**
- `systemPrompt`: System instruction/context for the AI
- `userQuestion`: The user's question

**Response:**
```json
{
  "response": "To create a promise in JavaScript, you can use the Promise constructor...",
  "status": "success"
}
```

### 3. Health Check

**Endpoint:** `GET /api/chat/health`

**Request:**
```bash
curl http://localhost:8083/api/chat/health
```

**Response:**
```
ChatBot Service is running!
```

## Model Classes

### ChatRequest
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message;
}
```

### ChatResponse
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String response;
    private String status;
}
```

## Configuration Details

### application.properties

```properties
# OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY:your-api-key-here}
spring.ai.openai.chat.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.7
```

**Configuration Options:**
- `spring.ai.openai.api-key`: Your OpenAI API key (uses env variable or default)
- `spring.ai.openai.chat.model`: AI model to use (gpt-4o-mini, gpt-4, gpt-3.5-turbo)
- `spring.ai.openai.chat.options.temperature`: Creativity level (0.0-2.0, default 0.7)

## Use Cases

1. **General Q&A Chatbot**
   - Ask any general knowledge questions
   - Get instant answers from GPT-4

2. **Code Assistance**
   - Ask programming questions
   - Get code examples and explanations

3. **Content Generation**
   - Generate ideas, summaries, or content
   - Create documentation

4. **Contextual Assistance**
   - Provide role-based context (e.g., "You are a Python expert")
   - Get specialized responses

## Error Handling

The API returns proper HTTP status codes and error messages:

- **400 Bad Request**: Empty message or invalid parameters
- **500 Internal Server Error**: AI service error or network issue

Example error response:
```json
{
  "response": "Error: API key is invalid",
  "status": "error"
}
```

## Logging

Logging is configured in `application.properties`. Current levels:
- Root: INFO
- Application (com.mco.springai): DEBUG
- Spring AI (org.springframework.ai): DEBUG

View logs in console output or configure file logging in `application.properties`.

## Troubleshooting

### 1. "API key is invalid" Error
- Verify your OpenAI API key is correct
- Check that the environment variable is set: `echo $OPENAI_API_KEY` (Linux/Mac) or `echo %OPENAI_API_KEY%` (Windows)

### 2. "Connection refused" Error
- Ensure the application is running
- Check the correct port (default: 8083)

### 3. Application Startup Fails
- Check that Java 21+ is installed
- Verify Maven dependencies: `mvn clean dependency:resolve`

## Dependencies

Main dependencies:
- **Spring Boot 4.1.0**: Web framework
- **Spring AI 0.8.1**: AI integration framework
- **OpenAI Spring Boot Starter**: OpenAI API integration
- **Lombok**: Code generation for POJOs

## Performance Considerations

- Temperature = 0.7: Balanced between creativity and consistency
- Model: gpt-4o-mini (cost-effective, fast responses)
- Each request incurs OpenAI API costs

## Future Enhancements

1. **Multi-turn Conversations**: Store conversation history
2. **Rate Limiting**: Prevent abuse
3. **User Authentication**: Secure endpoints with JWT
4. **Database Integration**: Persist chat history
5. **Streaming Responses**: Real-time response streaming
6. **Custom Models**: Support for fine-tuned models

## License

This project is part of Java Real Time Implementation 2026.

## Support

For issues or questions, refer to:
- Spring AI Documentation: https://docs.spring.io/spring-ai/
- OpenAI Documentation: https://platform.openai.com/docs/
