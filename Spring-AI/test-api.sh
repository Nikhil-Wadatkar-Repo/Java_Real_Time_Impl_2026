# ChatBot API Test Script
# This script contains cURL requests to test the ChatBot API

# Note: Make sure the application is running on http://localhost:8083

# 1. Health Check
echo "=== Testing Health Check ==="
curl -X GET http://localhost:8083/api/chat/health
echo -e "\n"

# 2. Simple Question - Geography
echo "=== Testing Simple Question: What is the capital of France? ==="
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"What is the capital of France?"}'
echo -e "\n\n"

# 3. Simple Question - Math
echo "=== Testing Simple Question: What is 2+2? ==="
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"What is 2+2?"}'
echo -e "\n\n"

# 4. Simple Question - Programming
echo "=== Testing Simple Question: How do I create a loop in Python? ==="
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"How do I create a loop in Python?"}'
echo -e "\n\n"

# 5. Contextual Question - Expert Mode (JavaScript)
echo "=== Testing Contextual Question: JavaScript Expert ==="
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%20are%20a%20JavaScript%20expert%20with%2010%20years%20experience&userQuestion=What%20is%20the%20difference%20between%20let%20and%20var%20in%20JavaScript?" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 6. Contextual Question - Expert Mode (Java)
echo "=== Testing Contextual Question: Java Expert ==="
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%20are%20a%20Java%20expert%20specializing%20in%20Spring%20Framework&userQuestion=What%20is%20dependency%20injection%20in%20Spring?" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 7. Testing Error Handling - Empty Message
echo "=== Testing Error Handling: Empty Message ==="
curl -X POST http://localhost:8083/api/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"message":""}'
echo -e "\n\n"

# 8. Testing Error Handling - Missing Parameter in Context
echo "=== Testing Error Handling: Missing Query Parameter ==="
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%20are%20helpful" \
  -H "Content-Type: application/json"
echo -e "\n\n"

echo "=== All Tests Completed ==="
