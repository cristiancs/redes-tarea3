# Integrantes

- Cristian Navarrete 201573549-2
- Benjamin Seider 201573541-7

# Compilacion

    MAVEN_OPTS=-Xmx100000000; mvn  -T 1C  package assembly:single

# Ejecucion

    java -cp target/streaming-1.0-SNAPSHOT-jar-with-dependencies.jar streaming.App

# Si ya tienes javacv puedes ejecutar con

    java -classpath mac -cp target/streaming-1.0-SNAPSHOT-jar-with-dependencies.jar streaming.App
