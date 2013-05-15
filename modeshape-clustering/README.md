Example Clustering 2 ModeShape Repositories
===========================================

What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to cluster 2 ModeShape repositories. This example
contains 2 configuration files for a simple master/slave setup. However, in production, it's likely that a _Highly Available_
setup is desired. If that is the case, the only configuration difference from this quickstart is that HornetQ (or the chosen JMS solution)
will need to be set up for high availability.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.
The application this project produces is designed to be run on JBoss Enterprise Application Platform 6.

Install ModeShape's EAP kit into an existing JBoss EAP 6 server
---------------------------------------------------------------

Before running this demo make sure that you have installed the ModeShape EAP kit into an existing JBoss EAP server.
The simplest way to do this is to follow the instructions provided [here](https://docs.jboss.org/author/display/MODE/Installing+ModeShape+into+AS7)

Start 2 JBoss EAP instances with the provided configurations
------------------------------------------------------------

1. Copy the `standalone-modeshape-ha-master.xml` and `standalone-modeshape-ha-slave.xml` configuration files from the root of the quickstart
into the `JBOSS_HOME/standalone/configuration` folder
2. Open a command line and navigate to the root of the JBoss server directory.
3. Start the `master` server:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-modeshape-ha-master.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-modeshape-ha-master.xml
4. Start the `slave` server:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-modeshape-ha-slave.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-modeshape-ha-slave.xml


Build and Deploy the Quickstart into each of the running servers
----------------------------------------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

1. Make sure you have started the 2 JBoss Server instances as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive into the `master` server:

        mvn clean package jboss-as:deploy

4. Type this command to build and deploy the archive into the `slave` server:

        mvn clean package jboss-as:deploy -Djboss-as.port=10000

5. This will deploy `target/modeshape-clustering.war` to each of the running instances of the server.

Accessing the application
---------------------

The application will be running at the following URLs:

        On the `master` server: <http://localhost:8080/modeshape-clustering/>
        On the `slave` server: <http://localhost:8081/modeshape-clustering/>

Open the above URLs in two different browsers (or 2 different browser tabs/windows).

The user is presented with a form where he can input one of the following:

1. Parent Absolute Path - an absolute node path
2. New Node Name - a simple string which represents the name of new node that can be added
3. Search for Nodes Names Like - a simple string which represents the name pattern of nodes that will be searched

based on which one of the following actions can be performed

1. Show children - displays the children of node located at "Parent Absolute Path"
2. Add Node - add a new child with the given name under the node located at "Parent Absolute Path"
3. Search - searches for nodes which have in their name the given string pattern

Undeploy the Archive
--------------------

1. Make sure you have started the 2 JBoss Server instances as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from the `master` server:

        mvn jboss-as:undeploy

4. When you are finished testing, type this command to undeploy the archive from the `slave` server:

        mvn jboss-as:undeploy -Djboss-as.port=10000

Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the JBoss Server corresponding to `master` as described above.
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