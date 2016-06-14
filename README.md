[![License](http://img.shields.io/:license-apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.modeshape/modeshape-parent/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.modeshape%22)
[![Build Status](https://travis-ci.org/ModeShape/quickstart.svg?branch=master)](https://travis-ci.org/ModeShape/quickstart)

ModeShape Quickstarts
=========================================================

What is it?
-----------

ModeShape quickstarts are a collection of self-contained projects, that demonstrate how to use ModeShape when installed as part of JBoss AS.
Each quickstart is a small, specific, focused, working example that can be used as a reference for your own project.

System requirements
-------------------

All you need to build the quickstarts is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better. Each quickstart
produces a web application (WAR) that is designed to be run on JBoss Wildfly 9 or 10.

Contents
--------

The following quickstarts are available:

* [modeshape-cdi](modeshape-cdi) - shows how you use CDI to inject various ModeShape JCR object instances
* [modeshape-cli](modeshape-cli) - shows how you use the JBoss AS CLI to configure a ModeShape subsystem
* [modeshape-clustering](modeshape-clustering) - shows how you can configure multiple ModeShape nodes to run in a cluster
* [modeshape-ejb](modeshape-ejb) - shows how you can use ModeShape from different types of EJBs
* [modeshape-servlet](modeshape-servlet) - shows how you can use ModeShape from a standard web application Servlet

Packaging and distributing
--------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must use the `settings.xml`
file from the root of this project. See [this ModeShape community article](http://community.jboss.org/wiki/ModeShapeandMaven)
for help on how to install and configure Maven 3._

To create a zip distribution with all the quickstarts, run the following Maven command:

        mvn clean verify -Pdist

The output will be placed in the [target](target) folder.

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
