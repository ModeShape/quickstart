Example Using a Federated ModeShape Repository
==============================================

What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to configure a Federated ModeShape repository.
This example contains a `standalone-modeshape-federation.xml` configuration file which shows the configuration necessary for enabling a
Federated repository.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.
The application this project produces is designed to be run on JBoss Enterprise Application Platform 6.

Install ModeShape's EAP kit into an existing JBoss EAP 6 server
---------------------------------------------------------------

Before running this demo make sure that you have installed the ModeShape EAP kit into an existing JBoss EAP server.
The simplest way to do this is to follow the instructions provided [here](https://docs.jboss.org/author/display/MODE/Installing+ModeShape+into+AS7)

Start a JBoss EAP instance with the provided configuration file (see above)
---------------------------------------------------------------------------

1. Copy the `standalone-modeshape-federation.xml` file from the root of the quickstart into the `JBOSS_HOME/standalone/configuration`
folder
2. Open a command line and navigate to the root of the JBoss server directory.
3. Start the `master` server:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-modeshape-federation.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-modeshape-federation.xml

Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

1. Make sure you have started the JBoss Server instance as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive into the `master` server:

        mvn clean package jboss-as:deploy

4. This will deploy `target/modeshape-federation.war` to the running server.

Accessing the application
------------------------

The application will be running at the following URL: <http://localhost:8080/modeshape-federation/>

Open the above URLs into your browser

The user is presented with a form where he can choose an external source for which to list its content:

1. JBoss Server Data Dir - represents the JBOSS_HOME/standalone/data folder
2. Example DB - represents a simple, in memory H2 database which has been configured as part of  `standalone-modeshape-federation.xml`

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server instance as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from the server:

        mvn jboss-as:undeploy

Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean package -Parq-jbossas-remote

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