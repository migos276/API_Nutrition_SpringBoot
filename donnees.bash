#!/bin/bash

# Script pour ajouter des plats camerounais traditionnels
# URL de base de l'API
BASE_URL="http://localhost:8080/api/foods"

echo "Ajout des plats camerounais traditionnels..."

# 1. Ndolé
echo "Ajout du Ndolé..."
curl -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "name": "Ndolé",
    "category": "Plat principal",
    "ingredients": "Feuilles de ndolé, Arachides, Viande de bœuf, Poisson fumé, Crevettes séchées, Huile de palme, Oignons, Ail, Gingembre",
    "isBaseFood": true
}' && echo " ✓ Ndolé ajouté" || echo " ✗ Erreur lors de l'ajout du Ndolé"

# 2. Eru
echo "Ajout de l'Eru..."
curl -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "name": "Eru",
    "category": "Plat principal",
    "ingredients": "Feuilles d'\''eru, Melon (egusi), Viande de bœuf, Poisson fumé, Crevettes séchées, Huile de palme, Stockfish, Sel",
    "isBaseFood": true
}' && echo " ✓ Eru ajouté" || echo " ✗ Erreur lors de l'ajout de l'Eru"

# 3. Bâton de manioc (Bobolo)
echo "Ajout du Bobolo..."
curl -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "name": "Bobolo",
    "category": "Accompagnement",
    "ingredients": "Manioc râpé, Feuilles de bananier, Sel",
    "isBaseFood": true
}' && echo " ✓ Bobolo ajouté" || echo " ✗ Erreur lors de l'ajout du Bobolo"

# 4. Achu
echo "Ajout de l'Achu..."
curl -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "name": "Achu",
    "category": "Plat principal",
    "ingredients": "Macabo jaune, Huile de palme, Feuilles d'\''achu, Viande de bœuf, Poisson fumé, Crevettes séchées, Piment, Sel",
    "isBaseFood": true
}' && echo " ✓ Achu ajouté" || echo " ✗ Erreur lors de l'ajout de l'Achu"

# 5. Kwacoco
echo "Ajout du Kwacoco..."
curl -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "name": "Kwacoco",
    "category": "Accompagnement",
    "ingredients": "Macabo, Feuilles de macabo, Huile de palme, Piment, Sel, Feuilles de bananier, Poisson fumé",
    "isBaseFood": true
}' && echo " ✓ Kwacoco ajouté" || echo " ✗ Erreur lors de l'ajout du Kwacoco"

echo ""
echo "Script terminé !"

# Vérification optionnelle - lister tous les aliments
echo "Vérification des aliments ajoutés..."
curl -s -X GET $BASE_URL | grep -o '"name":"[^"]*"' | sed 's/"name":"//g' | sed 's/"//g' | sort