# Les workflow Githubs

Vous trouverez ici les explications des fichier .yml de ce dossier.

## Test et Couverture de code

**Fichier** : *run-test.yml*

Ce fichier est décomposé en deux parties : Les tests et la couverture de code. Il agit sur toutes les branches sauf *
*master** et **github-pages** qui sont géré par un pipeline différent.

### Les tests

Les tests sont réalisé par maven et la librarie JUnit. Il s'agit d'exécuter tout les tests présent dans le fichier
/src/test. Un rapport sur les tests est ensuite déposé en tant qu'artefacts dans l'action de Github. Enfin, on vérifie
si tous les tests sont passé correctement. Dans le cas ou une erreur est produite, on arrête le pipeline ici et on
renvoie une annotation d'erreur.

### La couverture de code

Une fois les tests passé, on vérifie qu'il couvrent une partie sufisante du code. Dans notre cas, la proportion doit
être supérieure à 80% (cf : *pom.xml*). Un rapport de couverture de code est ensuite généré. Dans le cas ou la
couverture
de code est insufisante, le pipeline s'arrête et renvoie une annotation d'erreur.

Ce pipeline s'effectuant sur toutes les branches (même celle ou on ajoute des features), le push n'est pas rejeté si le
pipeline échoue, il est simplement à but informatif.

## Pipeline de la branche principale

**Fichier** : *workflow.yml*

Ce fichier est un pipeline un peu plus complet qui agit sur la branche **master** et la branche **github-pages**. Je ne
détaillerais pas les deux première étapes car elles sont exactement les même que le premier pipeline. Nous sommes donc
dans le cas ou les tests sont tous passé et la couverture de code est au dela de 80%.

### Le déploiement du package

Vu que notre package est correct et fonctionnel, nous pouvons le publier. Github integre par défaut un gestionnaire de
package maven.

Le deploiement se fait sur l'URL présente dans le *pom.xml* et en utilisant un token qui est présent dans les secrets du
repertoire Github. Il est la dernière étape du cycle de vie de maven.

### La documentation

La documentation se mets également à jour automatiquement. Pour cela, j'utilise une action doxygene (de mattnotmitt)
puis je push cette documentation sur la branche **github-pages** qui ne contient que la documentation. Vous pouver le
comprendre en suivant [ce tutoriel](https://kylerobots.github.io/tutorials/Automatic_Documentation/). L'option Pages de
Github est paramétrée pour aller chercher la documentation sur cette branche et la mettre sur le site web

Avec tout cela de fait, notre package est prêt à être utilisé.

### Docker

Un conteneur docker est également créé et poussé sur un repertoire DockerHub. Il contient une simulation de
fonctionnement de notre package. Vous le trouverez à l'adresse 
[https://hub.docker.com/r/skander23000/project_devops_2024](https://hub.docker.com/r/skander23000/project_devops_2024)