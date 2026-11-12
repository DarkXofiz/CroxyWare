@echo off
set APP_HOME=%~dp0
set JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%JAR%" (
  powershell -Command "Invoke-WebRequest https://raw.githubusercontent.com/gradle/gradle/v9.2.1/gradle/wrapper/gradle-wrapper.jar -OutFile '%JAR%'"
)
java -Xmx64m -Xms64m -classpath "%JAR%" org.gradle.wrapper.GradleWrapperMain %*
