# Document de Synthèse - Générateur de Mots de Passe Robustes (CLI)

**Cours :** DevOps & Application Java  
**Niveau :** Licence 3 - Informatique / Génie Logiciel  
**Projet :** Outil en ligne de commande dédié à la sécurité des accès

---

## 1. Analyse Fonctionnelle

L'application `PassGen CLI` est un outil de cybersécurité fonctionnant en ligne de commande, destiné à générer des mots de passe robustes et à en évaluer l'entropie réelle. Conçue de manière interactive, elle s'articule autour des fonctionnalités suivantes :

*   **Paramétrage sur mesure :** L'utilisateur définit précisément la longueur souhaitée pour son mot de passe et sélectionne les jeux de caractères (majuscules, minuscules, chiffres, symboles spéciaux).
*   **Mode Rafale :** L'outil permet de produire une liste multiple de mots de passe en une seule itération pour répondre aux besoins de déploiements de masse.
*   **Indicateur de Force Indépendant :** Chaque mot de passe généré est audité de manière asynchrone pour lui attribuer un score de robustesse lisible (Très faible, Faible, Moyen, Fort, Très fort).
*   **Interface Terminal :** L'interaction est totalement gérée via une invite de commande interactive, sans interface graphique, favorisant son usage sur des serveurs distants.

---

## 2. Analyse Technique

Ce projet se distingue par une approche **100% Java natif**, démontrant une maîtrise des API standards de Java 21, sans recourir à des dépendances lourdes externes (ni Node.js, ni Spring, ni Maven).

### 2.1. Structure du Programme Java (Application CLI)
Le code est segmenté pour respecter le principe de responsabilité unique (SRP) :
*   `Main.java` : Gère le flux d'entrée/sortie (I/O) via la console avec la classe `Scanner`.
*   `Generator.java` : Moteur de création. Il remplace la fonction `Math.random()` par `java.security.SecureRandom`, une exigence cryptographique stricte pour éviter la prédictibilité des valeurs pseudo-aléatoires.
*   `DockerClient.java` : Intègre le `java.net.http.HttpClient` (introduit depuis Java 11) pour orchestrer des requêtes HTTP POST asynchrones vers le conteneur Docker.

### 2.2. Le Conteneur Docker (Micro-Serveur de Validation)
Pour valider l'interopérabilité exigée par le cahier des charges, le conteneur Docker n'héberge pas un script Python ou Node.js, mais un **serveur HTTP minimaliste en Java** (`com.sun.net.httpserver.HttpServer`). 

**Communication :** L'application CLI envoie une charge utile JSON (`{"password":"..."}`) au port 8080 du conteneur.
**Audit de sécurité (Entropie) :** La force du mot de passe est calculée mathématiquement via l'entropie de Shannon selon la formule :

$$ E = L \times \log_2(R) $$

*Où $L$ est la longueur du mot de passe et $R$ la taille du pool de caractères possibles (26 pour minuscules, 26 pour majuscules, 10 pour chiffres, 32 pour symboles).* 
Le score est ensuite classifié selon des seuils stricts (ex: $E \ge 100$ bits = "Très fort"). L'utilisation de l'image Docker `eclipse-temurin:21-jdk-alpine` permet d'obtenir un conteneur extrêmement léger et performant.

---

## 3. Guide d'Installation et d'Exécution

### Prérequis
*   Docker installé et lancé sur la machine hôte.
*   Java Development Kit (JDK) 21 installé.

### Étape 1 : Construction et lancement du conteneur Docker
Dans votre terminal, placez-vous dans le répertoire `docker-validator/` et exécutez :
```bash
# Construction de l'image Docker
docker build -t java-validator-api .

# Lancement du conteneur en arrière-plan (port 8080)
docker run -d -p 8080:8080 --name validator-container java-validator-api