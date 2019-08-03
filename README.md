# Integrantes

- Cristian Navarrete 201573549-2
- Benjamin Seider 201573541-7

# Compilacion

    MAVEN_OPTS=-Xmx100000000; mvn  -T 1C  package assembly:single

O si se quiere compilar para una plataforma especifica (reduce bastante el tiempo de compilación)

    MAVEN_OPTS=-Xmx100000000; mvn  -T 1C -Djavacpp.platform=macosx-x86_64  package assembly:single

Donde las plataformas pueden ser: android-arm, linux-x86_64, macosx-x86_64, windows-x86_64

# Ejecucion

    java -cp target/streaming-1.0-SNAPSHOT-jar-with-dependencies.jar streaming.App

# Notas

Esta tarea fue realizada con java 8.
