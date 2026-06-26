# Utilisation de l'image officielle Java 21 optimisée (Alpine)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copie du code source
COPY ValidatorServer.java .

# Compilation du serveur
RUN javac ValidatorServer.java

# Exposition du port
EXPOSE 8080

# Commande de démarrage
CMD ["java", "ValidatorServer"]