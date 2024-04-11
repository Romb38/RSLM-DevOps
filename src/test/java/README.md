# Les tests

# Qe teste-t-on ?

Ces fichiers testent principalement l'implémentation de la classe DataFrame qui reprends les principales
fonctionnalitées de la librairie Panda de python.

# Comment fonctionne les tests

Les tests ont été fait en utilisant la librairie JUnit du language JAVA. Cette librairie permet de faire des tests
efficaces. Nous utilisons également Jacoco, un outil de couverture de code afin d'avoir un indice sur la pertinence de
nos tests.

# Les fichiers de tests

- **UnitTest** : Ce fichier contient les test rédigée à la main, il s'agit souvent de test précis afin de cibler des cas
  très spécifiques
- **autoTesting** : Il contient des tests généré par une intelligence artificielle. Les tests sont plus généraux et font
  une couverture classique du code

# Les annexes

- **empty.csv** : Fichier CSV vide
- **noheader.csv** : Fichier CSV ne contenant que des données
- **exemple.csv** : Fichier CSV contenant des données lambdas