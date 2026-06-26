```markdown
# 🔐 PassGen CLI - Java & Docker

Outil en ligne de commande (CLI) développé en **Java 21 pur** permettant de générer des mots de passe robustes sur mesure. L'évaluation de la solidité des mots de passe est effectuée par une API distante isolée dans un conteneur **Docker**, calculant l'entropie cryptographique.

Ce projet s'inscrit dans le cadre du cours DevOps & Application Java.

## 🚀 Fonctionnalités
- Génération paramétrable (longueur, majuscules, minuscules, chiffres, symboles).
- Mode rafale (génération multiple).
- Évaluation stricte de la robustesse via Entropie de Shannon.
- Architecture 100% native (Aucune dépendance externe, `HttpClient` et `HttpServer` natifs).
- Environnement d'évaluation conteneurisé sous Docker (image Alpine légère).

## 📂 Architecture du dépôt
* `src/` : Code source de l'application Java en ligne de commande.
* `docker-validator/` : Fichiers liés au micro-serveur de validation et son `Dockerfile`.
* `DOCUMENTATION.md` : Analyse technique et fonctionnelle détaillée du projet.

## ⚙️ Démarrage rapide

### 1. Démarrer l'API d'évaluation (Docker)
```bash
cd docker-validator
docker build -t java-validator .
docker run -d -p 8080:8080 --name pass-validator java-validator