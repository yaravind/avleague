avleague
========

Required
--------
* [Java SE 6u45] (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Eclipse IDE for Java EE Developers] (http://www.eclipse.org/downloads/index-developer.php)
* Maven 3 (Comes bundled with Eclipse download)
* EGit (Can be installed from Help>Eclipse Marketplace...)
* m2eclipse (Can be installed from Help>Eclipse Marketplace...)
* JBOSS Tools (Can be installed from Help>Eclipse Marketplace...)
* _OPTIONAL_ [Heroku plugin] (https://devcenter.heroku.com/articles/getting-started-with-heroku-eclipse)
* [Neo4j 1.9] (http://www.neo4j.org/download)
* _OPTIONAL_ [Neoeclipse] (https://github.com/neo4j/neoclipse/downloads)

Local Setup
-----------
* Import the avlleague project to eclipse (Import>Git>Projects from Git>URI>) 
* package tomcat7:run using m2eclipse. This runs embedded Tomcat 7
* The app is accessible at http://localhost:9090/avl/
* If running for first time then bootstrap the seed data by doing a HTTP GET to http://localhost:9090/avl/admin/populate
