Example Using ModeShape from different types of EJBs
=========================================================


What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to get access and use a repository bound in JNDI, from different
types of EJBs via a JSF application.

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better.
The application this project produces is designed to be run on JBoss Wildfly 9 or 10.

Running the Quickstart
-------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

1. Open a command line and navigate to the root directory of this quickstart.
2. Type this command to build and deploy the archive:

        mvn clean package

3. This will start a local Wildfly instance located at `target\wildfly', unpack and copy the ModeShape WF kit and deploy `target/modeshape-ejb.war` to the running instance of the server.

Accessing the application
---------------------

The application will be running at the following URL: http://localhost:8080/modeshape-ejb

Installing the ModeShape kit will add two pre-configured demo repositories: `sample` and `artifacts` (see the `JBOSS_HOME/conf/standalone-modeshape.xml` file for more details).
Both repositories are bound by default in JNDI under names: `java:/jcr/sample` and `java:/jcr/artifacts`.

The user is presented with a form where he can input the name of a repository and select on of the following EJBs: a Stateful Singleton,
a Stateless EJB which uses Container Managed Transactions and a Stateful EJB which uses Bean Managed Transactions.

Based on the user selection, one of the following two actions is delegated to the EJB:

1. Describe - displays some meta-information about the repository or shows an error in case the repository cannot be located in JNDI.
2. Count - displays the total number of non-system nodes in a repository or shows an error in case the repository cannot be located in JNDI.


Run the Arquillian Tests
-------------------------

This quickstart provides some optional Arquillian tests. These are part of separate profile and can be run like so: 

1. Open a command line and navigate to the root directory of this quickstart.
2. Type the following command to run the tests:

       mvn clean verify -Pdist

The above command will start a server with the ModeShape WF kit deployed and run the tests using the predefined repository named `sample` (see above).

The ModeShape project
---------------------
ModeShape is an open source implementation of the JCR 2.0 
([JSR-283](http://www.jcp.org/en/jsr/detail?id=283])) specification and 
standard API. To your applications, ModeShape looks and behaves like a 
regular JCR repository. Applications can search, query, navigate, change, 
version, listen for changes, etc. But ModeShape can store that content 
in a variety of back-end stores (including relational databases, Infinispan 
data grids, JBoss Cache, etc.), or it can access and update existing content 
from *other* kinds of systems (including file systems, SVN repositories, 
JDBC database metadata, and other JCR repositories). ModeShape's connector 
architecture means that you can write custom connectors to access any 
kind of system. And ModeShape can even federate multiple back-end systems 
into a single, unified virtual repository.

For more information on ModeShape, including getting started guides, 
reference guides, and downloadable binaries, visit the project's website 
at [http://www.modeshape.org]() or follow us on our [blog](http://modeshape.wordpress.org) 
or on [Twitter](http://twitter.com/modeshape). Or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our community 
of contributors and users.

The official Git repository for the project is also on GitHub at 
[http://github.com/ModeShape/modeshape]().

Need help ?
-----------

ModeShape is open source software with a dedicated community. If you have 
any questions or problems, post a question in our 
[user forum](http://community.jboss.org/en/modeshape) or hop into our 
[IRC chat room](http://www.jboss.org/modeshape/chat) and talk our 
community of contributors and users.
