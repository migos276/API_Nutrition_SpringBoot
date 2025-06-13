# Application de Détection d'Allergies Alimentaires

Une application Spring Boot pour la détection et l'analyse des allergies alimentaires basée sur le suivi des repas et des symptômes des utilisateurs.

## 🚀 Fonctionnalités

- **Gestion des utilisateurs** : Création, modification et suppression d'utilisateurs
- **Suivi des repas** : Enregistrement détaillé des repas consommés
- **Suivi des symptômes** : Enregistrement des symptômes avec corrélation temporelle
- **Détection d'allergies** : Analyse automatique des corrélations repas-symptômes
- **Gestion des aliments** : Base de données d'aliments avec images
- **Planification hebdomadaire** : Planification des repas par semaine
- **Gestion d'événements buffet** : Organisation d'événements avec calcul des quantités
- **Tableau de bord** : Statistiques et analyses personnalisées
- **Gestion des médias** : Upload et gestion des images d'aliments

## 🛠️ Technologies utilisées

- **Backend** : Spring Boot 3.x
- **Base de données** : PostgreSQL (recommandé) / H2 (développement)
- **Documentation API** : Swagger/OpenAPI 3
- **Validation** : Jakarta Validation
- **Gestion des images** : Service de téléchargement et stockage d'images

## 📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- PostgreSQL 12+ (ou H2 pour les tests)
- Postman (pour les tests d'API)

## 🚀 Installation et démarrage

### 1. Cloner le projet
```bash
   git clone https://github.com/migos276/API_Nutrition_SpringBoot.git
```
```  
pour peuple la base de donnee taper la commande bash donnee.sh

```
### 2. Configuration de la base de données( optionel car la l'application possede deja la base donnee H2)
Modifier le fichier `application.properties` :
```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/allergy_detection_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
app.upload.dir=./uploads/
```
L'application sera accessible sur : `http://localhost:8080`


## 📡 Endpoints principaux

### Gestion des utilisateurs
- `POST /api/users` - Créer un utilisateur
- `GET /api/users` - Lister tous les utilisateurs (avec pagination)
- `GET /api/users/{id}` - Obtenir un utilisateur
- `PUT /api/users/{id}` - Modifier un utilisateur
- `DELETE /api/users/{id}` - Supprimer un utilisateur

### Gestion des aliments
- `POST /api/foods` - Créer un aliment
- `GET /api/foods` - Lister tous les aliments
- `GET /api/foods/{id}` - Obtenir un aliment
- `GET /api/foods/search?q={terme}` - Rechercher des aliments
- `PUT /api/foods/{id}` - Modifier un aliment
- `DELETE /api/foods/{id}` - Supprimer un aliment

### Suivi des repas
- `POST /api/meals` - Enregistrer un repas
- `GET /api/users/{userId}/meals` - Obtenir les repas d'un utilisateur
- `PUT /api/meals/{id}` - Modifier un repas
- `DELETE /api/meals/{id}` - Supprimer un repas

### Suivi des symptômes
- `POST /api/symptoms` - Enregistrer un symptôme
- `GET /api/users/{userId}/symptoms` - Obtenir les symptômes d'un utilisateur
- `PUT /api/symptoms/{id}` - Modifier un symptôme
- `DELETE /api/symptoms/{id}` - Supprimer un symptôme

### Analyse des allergies
- `GET /api/users/{userId}/allergy-analysis` - Analyser les allergies potentielles
- `GET /api/users/{userId}/food-risk/{foodId}` - Calculer le risque d'un aliment

### Planification hebdomadaire
- `POST /api/weekly-plans` - Créer un élément de planification
- `GET /api/users/{userId}/weekly-plans` - Obtenir les plans hebdomadaires
- `GET /api/users/{userId}/weekly-plans/current` - Plan de la semaine courante
- `GET /api/users/{userId}/weekly-plans/suggestions` - Suggestions de planification

### Gestion des événements buffet
- `POST /api/buffet-events` - Créer un événement buffet
- `GET /api/buffet-events` - Lister tous les événements
- `GET /api/users/{userId}/buffet-events` - Événements d'un utilisateur
- `POST /api/buffet-events/{buffetId}/foods` - Ajouter un aliment au buffet
- `GET /api/buffet-events/{buffetId}/calculate-quantities` - Calculer les quantités
- `GET /api/buffet-events/{buffetId}/shopping-list` - Générer une liste de courses

### Gestion des médias
- `POST /api/foods/{foodId}/images` - Ajouter une image à un aliment
- `GET /api/foods/{foodId}/images` - Obtenir les images d'un aliment
- `GET /api/media/{fileName}` - Servir un fichier média

### Tableau de bord
- `GET /api/users/{userId}/dashboard` - Tableau de bord utilisateur
- `GET /api/admin/global-statistics` - Statistiques globales

### Santé de l'application
- `GET /api/health` - Vérification de l'état de l'application

## 🧪 Tests avec Postman

Voir la collection Postman complète dans le fichier séparé `postman_collection.json`.

### Structure des données principales

#### Utilisateur (UserDto)
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "age": 30,
  "weight": 70.5,
  "height": 175.0,
  "activityLevel": "MODERATE",
  "medicalConditions": "Aucune",
  "knownAllergies": "Noix"
}
```

#### Aliment (FoodDto)
```json
{
  "name": "Pomme rouge",
  "category": "Fruits",
  "ingredients": "Pomme",
  "isBaseFood": true
}
```

#### Repas (MealDto)
```json
{
  "userId": 1,
  "foodId": 1,
  "foodName": "Pomme rouge",
  "quantity": 150.0,
  "unit": "grammes",
  "mealType": "BREAKFAST",
  "consumedAt": "2024-01-15T08:30:00"
}
```

#### Symptôme (SymptomDto)
```json
{
  "userId": 1,
  "symptomType": "DIGESTIVE",
  "description": "Maux d'estomac",
  "severity": "MILD",
  "startTime": "2024-01-15T10:00:00",
  "endTime": "2024-01-15T12:00:00"
}
```

## 🔧 Configuration avancée

### Variables d'environnement
```bash
# Base de données
DB_HOST=localhost
DB_PORT=5432
DB_NAME=allergy_detection_db
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Upload de fichiers
UPLOAD_DIR=./uploads/
MAX_FILE_SIZE=10MB

# Seuils d'analyse
DEFAULT_ALLERGY_THRESHOLD=30.0
DEFAULT_ANALYSIS_DAYS=30
```

### Profils Spring
- `dev` : Développement avec H2
- `prod` : Production avec PostgreSQL
- `test` : Tests avec base de données en mémoire

```bash
# Lancer avec un profil spécifique
mvn spring-boot:run -Dspring.profiles.active=dev
```

## 📊 Analyse des allergies

L'application utilise un algorithme de corrélation pour détecter les allergies potentielles :

1. **Collecte des données** : Repas et symptômes avec horodatage
2. **Analyse temporelle** : Corrélation entre consommation et apparition de symptômes
3. **Calcul de score de risque** : Basé sur la fréquence et la sévérité des symptômes
4. **Seuils configurables** : Personnalisation des niveaux d'alerte

### Paramètres d'analyse
- **Fenêtre temporelle** : 2-8 heures après le repas
- **Seuil de risque** : 30% par défaut (configurable)
- **Période d'analyse** : 30 derniers jours (configurable)

## 🚨 Gestion d'erreurs

L'API retourne des codes de statut HTTP standard :
- `200` : Succès
- `201` : Créé avec succès
- `400` : Erreur de validation
- `404` : Ressource non trouvée
- `500` : Erreur serveur

Format standard des réponses d'erreur :
```json
{
  "success": false,
  "error": "Message d'erreur",
  "timestamp": "2024-01-15T10:00:00",
  "path": "/api/endpoint"
}
```

## 🔒 Sécurité

**Note** : L'application ne dispose pas encore d'un système d'authentification complet. Pour la production, il est recommandé d'ajouter :
- Authentification JWT
- Autorisation basée sur les rôles
- Chiffrement des données sensibles
- Rate limiting

## 📝 Logs

Les logs sont configurés pour capturer :
- Requêtes HTTP importantes
- Erreurs d'application
- Analyses d'allergies
- Opérations sur les fichiers

## 🤝 Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 📞 Support

Pour toute question ou problème :
- Créer une issue sur GitHub
- Contacter l'équipe de développement
- Consulter la documentation Swagger

---
# Collection Postman - API Allergy Detection

## Configuration de base
- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`

```pour peuple la base de donnee taper la commande bash donnee.sh
```
---

## 1. HEALTH CHECK

### GET - Health Check
```
GET {{base_url}}/health
```

---

## 2. USER MANAGEMENT

### POST - Create User
```
POST {{base_url}}/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "gender": "MALE",
  "height": 175.5,
  "weight": 70.0
}
```

### GET - Get User by ID
```
GET {{base_url}}/users/{{user_id}}
```

### GET - Get All Users with Pagination
```
GET {{base_url}}/users?page=1&per_page=10&search=john
```

### PUT - Update User
```
PUT {{base_url}}/users/{{user_id}}
Content-Type: application/json

{
  "username": "john_doe_updated",
  "email": "john.updated@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15",
  "gender": "MALE",
  "height": 176.0,
  "weight": 72.0
}
```

### DELETE - Delete User
```
DELETE {{base_url}}/users/{{user_id}}?confirm=true
```

---

## 3. FOOD MANAGEMENT

### POST - Create Food
```
POST {{base_url}}/foods
Content-Type: application/json

{
  "name": "Riz au poisson",
  "category": "Plat principal",
  "ingredients": "riz, poisson, tomates, oignons, huile",
  "isBaseFood": false,
  "imageUrl": "https://example.com/image.jpg"
}
```

### GET - Get All Foods
```
GET {{base_url}}/foods
```

### GET - Get Food by ID
```
GET {{base_url}}/foods/{{food_id}}
```

### GET - Search Foods
```
GET {{base_url}}/foods/search?q=riz
```

### PUT - Update Food
```
PUT {{base_url}}/foods/{{food_id}}
Content-Type: application/json

{
  "name": "Riz au poisson épicé",
  "category": "Plat principal",
  "ingredients": "riz, poisson, tomates, oignons, huile, piment",
  "isBaseFood": false
}
```

### DELETE - Delete Food
```
DELETE {{base_url}}/foods/{{food_id}}
```

---

## 4. MEDIA MANAGEMENT

### POST - Add Image to Food
```
POST {{base_url}}/foods/{{food_id}}/images
Content-Type: application/json

{
  "imageUrl": "https://example.com/food-image.jpg",
  "isPrimary": true
}
```

### GET - Get Food Images
```
GET {{base_url}}/foods/{{food_id}}/images
```

### PUT - Set Primary Image
```
PUT {{base_url}}/images/1/primary
```

### DELETE - Delete Image
```
DELETE {{base_url}}/images/1
```

### GET - Serve Media File
```
GET {{base_url}}/media/food_1_image.jpg
```

---

## 5. MEAL MANAGEMENT

### POST - Create Meal
```
POST {{base_url}}/meals
Content-Type: application/json

{
  "userId": {{user_id}},
  "foodId": {{food_id}},
  "foodName": "Riz au poisson",
  "quantity": 250.0,
  "unit": "g",
  "mealTime": "2024-12-15T12:30:00",
  "mealType": "LUNCH",
  "notes": "Délicieux repas"
}
```

### GET - Get User Meals
```
GET {{base_url}}/users/{{user_id}}/meals
```

### GET - Get User Meals with Date Filter
```
GET {{base_url}}/users/{{user_id}}/meals?start_date=2024-12-01T00:00:00&end_date=2024-12-31T23:59:59
```

### PUT - Update Meal
```
PUT {{base_url}}/meals/{{meal_id}}
Content-Type: application/json

{
  "userId": {{user_id}},
  "foodId": {{food_id}},
  "foodName": "Riz au poisson épicé",
  "quantity": 300.0,
  "unit": "g",
  "mealTime": "2024-12-15T12:30:00",
  "mealType": "LUNCH",
  "notes": "Très épicé aujourd'hui"
}
```

### DELETE - Delete Meal
```
DELETE {{base_url}}/meals/{{meal_id}}
```

---

## 6. SYMPTOM MANAGEMENT

### POST - Create Symptom
```
POST {{base_url}}/symptoms
Content-Type: application/json

{
  "userId": {{user_id}},
  "symptomType": "NAUSEA",
  "severity": "MODERATE",
  "startTime": "2024-12-15T14:00:00",
  "endTime": "2024-12-15T16:00:00",
  "description": "Nausées après le repas",
  "possibleTriggerFoodId": {{food_id}}
}
```

### GET - Get User Symptoms
```
GET {{base_url}}/users/{{user_id}}/symptoms
```

### GET - Get User Symptoms with Date Filter
```
GET {{base_url}}/users/{{user_id}}/symptoms?start_date=2024-12-01T00:00:00&end_date=2024-12-31T23:59:59
```

### PUT - Update Symptom
```
PUT {{base_url}}/symptoms/{{symptom_id}}
Content-Type: application/json

{
  "userId": {{user_id}},
  "symptomType": "STOMACH_PAIN",
  "severity": "SEVERE",
  "startTime": "2024-12-15T14:00:00",
  "endTime": "2024-12-15T17:00:00",
  "description": "Douleurs abdominales sévères",
  "possibleTriggerFoodId": {{food_id}}
}
```

### DELETE - Delete Symptom
```
DELETE {{base_url}}/symptoms/{{symptom_id}}
```

---

## 7. ALLERGY ANALYSIS

### GET - Analyze User Allergies
```
GET {{base_url}}/users/{{user_id}}/allergy-analysis?threshold=30
```

### GET - Get Food Risk Score
```
GET {{base_url}}/users/{{user_id}}/food-risk/{{food_id}}?days=30
```

---

## 8. WEEKLY PLANNING

### POST - Create Weekly Plan Item
```
POST {{base_url}}/weekly-plans
Content-Type: application/json

{
  "userId": {{user_id}},
  "weekStartDate": "2024-12-16",
  "dayOfWeek": 1,
  "dayName": "MONDAY",
  "mealType": "BREAKFAST",
  "foodId": {{food_id}},
  "plannedQuantity": 200.0,
  "notes": "Petit-déjeuner léger"
}
```

### GET - Get User Weekly Plans
```
GET {{base_url}}/users/{{user_id}}/weekly-plans?week_start_date=2024-12-16
```

### GET - Get Current Week Plan
```
GET {{base_url}}/users/{{user_id}}/weekly-plans/current
```

### PUT - Update Weekly Plan Item
```
PUT {{base_url}}/weekly-plans/1
Content-Type: application/json

{
  "userId": {{user_id}},
  "weekStartDate": "2024-12-16",
  "dayOfWeek": 1,
  "dayName": "MONDAY",
  "mealType": "BREAKFAST",
  "foodId": {{food_id}},
  "plannedQuantity": 250.0,
  "notes": "Petit-déjeuner copieux"
}
```

### DELETE - Delete Weekly Plan Item
```
DELETE {{base_url}}/weekly-plans/1
```

### POST - Duplicate Week Plan
```
POST {{base_url}}/users/{{user_id}}/weekly-plans/duplicate?source_week=2024-12-16&target_week=2024-12-23
```

### GET - Get Weekly Plan Suggestions
```
GET {{base_url}}/users/{{user_id}}/weekly-plans/suggestions
```

### POST - Create Batch Weekly Plan
```
POST {{base_url}}/users/{{user_id}}/weekly-plans/batch
Content-Type: application/json

{
  "week_start_date": "2024-12-16",
  "plans": [
    {
      "day_of_week": 1,
      "meal_type": "BREAKFAST",
      "food_id": 1,
      "planned_quantity": 200.0
    },
    {
      "day_of_week": 1,
      "meal_type": "LUNCH",
      "food_id": 2,
      "planned_quantity": 300.0
    }
  ]
}
```

---

## 9. BUFFET MANAGEMENT

### POST - Create Buffet Event
```
POST {{base_url}}/buffet-events
Content-Type: application/json

{
  "name": "Buffet d'anniversaire",
  "description": "Célébration du 30ème anniversaire",
  "eventDate": "2024-12-25T18:00:00",
  "location": "Salle de fête",
  "organizerId": {{user_id}},
  "estimatedGuests": 50,
  "budget": 500000.0,
  "notes": "Thème tropical"
}
```

### GET - Get Buffet Event
```
GET {{base_url}}/buffet-events/{{buffet_id}}
```

### GET - Get All Buffet Events
```
GET {{base_url}}/buffet-events
```

### GET - Get User Buffet Events
```
GET {{base_url}}/users/{{user_id}}/buffet-events
```

### PUT - Update Buffet Event
```
PUT {{base_url}}/buffet-events/{{buffet_id}}
Content-Type: application/json

{
  "name": "Buffet d'anniversaire - Édition spéciale",
  "description": "Célébration du 30ème anniversaire avec surprise",
  "eventDate": "2024-12-25T19:00:00",
  "location": "Grande salle de fête",
  "organizerId": {{user_id}},
  "estimatedGuests": 75,
  "budget": 750000.0,
  "notes": "Thème tropical avec DJ"
}
```

### DELETE - Delete Buffet Event
```
DELETE {{base_url}}/buffet-events/{{buffet_id}}
```

### POST - Add Food to Buffet
```
POST {{base_url}}/buffet-events/{{buffet_id}}/foods
Content-Type: application/json

{
  "foodId": {{food_id}},
  "plannedQuantity": 5000.0,
  "unit": "g",
  "estimatedCost": 25000.0,
  "priority": "HIGH",
  "notes": "Plat principal populaire"
}
```

### DELETE - Remove Food from Buffet
```
DELETE {{base_url}}/buffet-foods/1
```

### GET - Calculate Buffet Quantities
```
GET {{base_url}}/buffet-events/{{buffet_id}}/calculate-quantities
```

### GET - Get Buffet Shopping List
```
GET {{base_url}}/buffet-events/{{buffet_id}}/shopping-list
```

### POST - Optimize Buffet Menu
```
POST {{base_url}}/buffet-events/{{buffet_id}}/optimize
Content-Type: application/json

{
  "guest_user_ids": [1, 2, 3, 4, 5]
}
```

### GET - Estimate Buffet Cost
```
GET {{base_url}}/buffet-events/{{buffet_id}}/cost-estimation?budget_per_person=1500
```

---

## 10. DASHBOARD & ANALYTICS

### GET - User Dashboard
```
GET {{base_url}}/users/{{user_id}}/dashboard?days=30
```

### GET - Global Statistics (Admin)
```
GET {{base_url}}/admin/global-statistics
```

---

# SIMULATION D'UTILISATEUR COMPLET

## Scénario : Marie, utilisatrice de l'application

### Étape 1 : Création de l'utilisateur
```
POST {{base_url}}/users
Content-Type: application/json

{
  "username": "marie_dupont",
  "email": "marie.dupont@gmail.com",
  "firstName": "Marie",
  "lastName": "Dupont",
  "dateOfBirth": "1985-03-20",
  "gender": "FEMALE",
  "height": 165.0,
  "weight": 60.0
}
```

### Étape 2 : Création de quelques aliments
```
POST {{base_url}}/foods
Content-Type: application/json

{
  "name": "Arachides grillées",
  "category": "Snack",
  "ingredients": "arachides, sel",
  "isBaseFood": true
}
```

```
POST {{base_url}}/foods
Content-Type: application/json

{
  "name": "Thieboudienne",
  "category": "Plat principal",
  "ingredients": "riz, poisson, tomates, oignons, aubergines, gombo",
  "isBaseFood": false
}
```

```
POST {{base_url}}/foods
Content-Type: application/json

{
  "name": "Lait frais",
  "category": "Boisson",
  "ingredients": "lait de vache",
  "isBaseFood": true
}
```

### Étape 3 : Enregistrement de repas
```
POST {{base_url}}/meals
Content-Type: application/json

{
  "userId": 1,
  "foodId": 1,
  "foodName": "Arachides grillées",
  "quantity": 50.0,
  "unit": "g",
  "mealTime": "2024-12-15T10:30:00",
  "mealType": "SNACK",
  "notes": "En-cas matinal"
}
```

```
POST {{base_url}}/meals
Content-Type: application/json

{
  "userId": 1,
  "foodId": 2,
  "foodName": "Thieboudienne",
  "quantity": 400.0,
  "unit": "g",
  "mealTime": "2024-12-15T13:00:00",
  "mealType": "LUNCH",
  "notes": "Déjeuner copieux"
}
```

```
POST {{base_url}}/meals
Content-Type: application/json

{
  "userId": 1,
  "foodId": 3,
  "foodName": "Lait frais",
  "quantity": 250.0,
  "unit": "ml",
  "mealTime": "2024-12-15T19:30:00",
  "mealType": "DINNER",
  "notes": "Avec du pain"
}
```

### Étape 4 : Signalement de symptômes
```
POST {{base_url}}/symptoms
Content-Type: application/json

{
  "userId": 1,
  "symptomType": "SKIN_RASH",
  "severity": "MODERATE",
  "startTime": "2024-12-15T11:00:00",
  "endTime": "2024-12-15T15:00:00",
  "description": "Démangeaisons et rougeurs sur les bras",
  "possibleTriggerFoodId": 1
}
```

```
POST {{base_url}}/symptoms
Content-Type: application/json

{
  "userId": 1,
  "symptomType": "STOMACH_PAIN",
  "severity": "MILD",
  "startTime": "2024-12-15T20:00:00",
  "endTime": "2024-12-15T21:30:00",
  "description": "Légères douleurs abdominales",
  "possibleTriggerFoodId": 3
}
```

### Étape 5 : Création d'un plan hebdomadaire
```
POST {{base_url}}/weekly-plans
Content-Type: application/json

{
  "userId": 1,
  "weekStartDate": "2024-12-16",
  "dayOfWeek": 1,
  "dayName": "MONDAY",
  "mealType": "BREAKFAST",
  "foodId": 3,
  "plannedQuantity": 200.0,
  "notes": "Petit-déjeuner simple"
}
```

### Étape 6 : Création d'un événement buffet
```
POST {{base_url}}/buffet-events
Content-Type: application/json

{
  "name": "Buffet familial",
  "description": "Réunion de famille mensuelle",
  "eventDate": "2024-12-22T12:00:00",
  "location": "Maison familiale",
  "organizerId": 1,
  "estimatedGuests": 20,
  "budget": 200000.0,
  "notes": "Tous âges confondus"
}
```

### Étape 7 : Ajout d'aliments au buffet
```
POST {{base_url}}/buffet-events/1/foods
Content-Type: application/json

{
  "foodId": 2,
  "plannedQuantity": 3000.0,
  "unit": "g",
  "estimatedCost": 15000.0,
  "priority": "HIGH",
  "notes": "Plat principal"
}
```

### Étape 8 : Analyse des allergies
```
GET {{base_url}}/users/1/allergy-analysis?threshold=25
```

### Étape 9 : Vérification du tableau de bord
```
GET {{base_url}}/users/1/dashboard?days=7
```

### Étape 10 : Vérification du risque pour un aliment spécifique
```
GET {{base_url}}/users/1/food-risk/1?days=30
```

---

## Notes importantes pour les tests

1. **Ordre des requêtes** : Respectez l'ordre de création (User → Food → Meal/Symptom)
2. **IDs dynamiques** : Utilisez les IDs retournés par les requêtes de création
3. **Dates** : Ajustez les dates selon vos besoins de test
4. **Variables Postman** : Configurez les variables d'environnement pour faciliter les tests
5. **Status codes attendus** :
   - POST : 201 Created
   - GET : 200 OK
   - PUT : 200 OK
   - DELETE : 200 OK

## Collection Postman JSON
Pour importer directement dans Postman, sauvegardez ce contenu dans un fichier .json et importez-le dans votre collection Postman.


