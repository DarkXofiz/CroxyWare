#!/bin/sh
# Basit Gradle wrapper başlatıcı. gradle-wrapper.jar yoksa otomatik indirir.
APP_HOME=$(cd "$(dirname "$0")" && pwd -P)
JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$JAR" ]; then
  echo "gradle-wrapper.jar indiriliyor..."
  URL="https://raw.githubusercontent.com/gradle/gradle/v9.8.2/gradle/wrapper/gradle-wrapper.jar"
  if command -v curl >/dev/null 2>&1; then curl -fsSL "$URL" -o "$JAR"
  else wget -q "$URL" -O "$JAR"; fi
fi
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then JAVACMD="$JAVA_HOME/bin/java"; else JAVACMD=java; fi
exec "$JAVACMD" -Xmx64m -Xms64m -Dorg.gradle.appname=gradlew \
  -classpath "$JAR" org.gradle.wrapper.GradleWrapperMain "$@"
