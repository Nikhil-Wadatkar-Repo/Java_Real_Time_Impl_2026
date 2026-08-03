@echo off
REM ChatBot API Test Script for Windows
REM This script contains cURL requests to test the ChatBot API

REM Note: Make sure the application is running on http://localhost:8083

echo.
echo === Testing Health Check ===
curl -X GET http://localhost:8083/api/chat/health
echo.
echo.

echo === Testing Simple Question: What is the capital of France? ===
curl -X POST http://localhost:8083/api/chat/ask ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"What is the capital of France?\"}"
echo.
echo.

echo === Testing Simple Question: What is 2+2? ===
curl -X POST http://localhost:8083/api/chat/ask ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"What is 2+2?\"}"
echo.
echo.

echo === Testing Simple Question: How do I create a loop in Python? ===
curl -X POST http://localhost:8083/api/chat/ask ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"How do I create a loop in Python?\"}"
echo.
echo.

echo === Testing Contextual Question: JavaScript Expert ===
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%%20are%%20a%%20JavaScript%%20expert%%20with%%2010%%20years%%20experience&userQuestion=What%%20is%%20the%%20difference%%20between%%20let%%20and%%20var%%20in%%20JavaScript?" ^
  -H "Content-Type: application/json"
echo.
echo.

echo === Testing Contextual Question: Java Expert ===
curl -X POST "http://localhost:8083/api/chat/ask-with-context?systemPrompt=You%%20are%%20a%%20Java%%20expert%%20specializing%%20in%%20Spring%%20Framework&userQuestion=What%%20is%%20dependency%%20injection%%20in%%20Spring?" ^
  -H "Content-Type: application/json"
echo.
echo.

echo === Testing Error Handling: Empty Message ===
curl -X POST http://localhost:8083/api/chat/ask ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"\"}"
echo.
echo.

echo === All Tests Completed ===
pause
