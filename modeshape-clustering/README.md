Example Clustering 2 ModeShape Repositories
===========================================

What is it?
-----------

This is a self-contained and deployable Maven 3 project that shows how to cluster 2 ModeShape repositories using JBoss Wildfly

**The example uses an H2 database for purely demonstrative purposes. Never use H2 in a production system when clustering**

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better.
The application this project produces is designed to be run on JBoss Wildfly 9 or 10.

Running the quickstart
------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

1. Open a command line and navigate to the root directory of this quickstart.
2. Type the following command to unpack in the `target` folder a full JBoss Wildfly installation with the ModeShape WF Kit installed: 

        mvn clean package        
3. Navigate to the `target/wildfly/bin` folder and run each of following commands:

        standalone.bat -c standalone-modeshape-ha.xml

        standalone.bat -c standalone-modeshape-ha.xml -Djboss.socket.binding.port-offset=1
        
4. You should now have two different server instances running locally

Accessing the application
-------------------------

The application will be running at http://localhost:8080/modeshape-clustering and http://localhost:8081/modeshape-clustering respectively.

Open the above URLs in two different browsers (or 2 different browser tabs/windows).

The user is presented with a form where he can input one of the following:

1. Parent Absolute Path - an absolute node path
2. New Node Name - a simple string which represents the name of new node that can be added
3. Search for Nodes Names Like - a simple string which represents the name pattern of nodes that will be searched

based on which one of the following actions can be performed

1. Show children - displays the children of node located at "Parent Absolute Path"
2. Add Node - add a new child with the given name under the node located at "Parent Absolute Path"
3. Search - searches for nodes which have in their name the given string pattern


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the first Wildfly server as described above
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the tests:

        mvn clean verify -Pdist

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