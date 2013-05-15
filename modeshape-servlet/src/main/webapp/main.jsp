<%@ page import="javax.jcr.Node" %>
<%@ page import="javax.jcr.NodeIterator" %>
<html>
<head>
    <title>ModeShape Servlet QuickStart</title>
</head>
<body style="background-image:url(dblue106.gif)">
<div style="background-image:url(dblue106.gif)">
    <div style="margin:5%;background-color:white; border:1px solid black">
        <div style="margin:5%">
            <table border="0" cellpadding="5px">
                <tr>
                    <td><img border="0" align="left" height="64" width="64" src="modeshape_icon_64px_med.png"></td>
                    <td><p><h1>ModeShape Demo Servlet Web Application</h1></p></td>
                </tr>
            </table>
            <hr>
            <p style="font: 10pt sans-sherif;">
                This is a demo which demonstrates two approaches of how to access a ModeShape repository from
                web application's servlet.
                The first approach uses standard way to get a reference to the repository via JNDI, the second
                demonstrates how to utilize the native JCR API to access the same repository.
            </p>

            <p style="font: 10pt sans-sherif;">
                Before running this quickstart make sure that you have installed Modeshape repository and the repository is bound to some
                JNDI name. The simplest way to do it is use JBoss AS7 and install
                Modeshape following to intructions provided by the <a
                    href="https://docs.jboss.org/author/display/MODE/Installing+ModeShape+into+AS7"> wiki page</a>
                which explains how to start ModeShape engine as AS7 subsystem with two preconfigured sample repositories: <b>sample
                and artifacts</b> (see conf/standalone-modeshape.xml).
                Both repositories are bound by default in JNDI under name <i>jcr/sample</i> and <i>jcr/artifacts</i> in the <i>java</i>
                namespace.
            </p>

            <p style="font: 10pt sans-sherif">
                Via the form bellow it is possible to specify a repository name/full JNDI name and an absolute path to a node.
                The response will contain the children of the node at the given path in the repository.
                The format of the <i>Repository location</i> parameter defines which approach will be used
                to get the access to the repository.
                If <i>Repository location</i> contains a simple name (like sample or artifacts) or a JNDI name like java:/jcr/sample
                the repository will be located directly in JNDI.
                If Repository location is a URI which starts with one of the following prefixes: 'jndi:' (i.e. <i>jndi:/jcr/artifacts</i>) or
                'http:', 'file:', 'classpath:' + the path to the a JSON file representing a valid repository configuration.
                the native JCR API will be used
            </p>

            <div style="padding:2%; border:1px solid black; font: 10pt sans-sherif;background-image:url(lgrey029.jpg)">
                <p style="font: 14pt sans-sherif"><b>Repository parameters</b>
                    <hr>
                </p>
                <form action="session.do">
                    <table>
                        <tr>
                            <td>
                                <p style="font: 10pt sans-sherif"><b>Repository location:</b><br/>
                                    (simple repository name/jndi-path/URL)
                                </p>
                            </td>
                            <td style="vertical-align:bottom">
                                <input type="text" name="<%=org.modeshape.quickstart.servlet.RepositoryServlet.REPOSITORY_PARAM%>"
                                       value="<%= session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.REPOSITORY_PARAM) == null ? "" :
                                       session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.REPOSITORY_PARAM) %>"
                                       size="50"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p style="font: 10pt sans-sherif">
                                    <b>Node path:</b>
                                    <br/>Absolute path to node. If you don't enter anything, the root node path "/" will be used</p>
                            </td>
                            <td style="vertical-align:bottom">
                                <input type="text" name="path"
                                       value="<%= session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.NODE_PATH_PARAM) == null ?  "" :
                                       session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.NODE_PATH_PARAM) %>"
                                       size="50"/></td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                    <button type="submit">Submit</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <% if (session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.ERROR_MESSAGE) != null) { %>
            <div style="border:1px solid red; color:red; margin-top:30px; padding:5%">
                <%= session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.ERROR_MESSAGE) %>
            </div>
            <%} else if (session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.CHILDREN_ATTRIBUTE) != null) {%>
            <p style="font: 14pt sans-serif">
                <b>Selected node: <%= session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.NODE_PATH_PARAM) %></b>
            </p>
            <p style="font: 14pt sans-serif"><b>Children:</b></p>
            <ul style="font: 10pt sans-serif">
                <%
                    NodeIterator it = (NodeIterator)session.getAttribute(org.modeshape.quickstart.servlet.RepositoryServlet.CHILDREN_ATTRIBUTE);
                    while (it.hasNext()) {
                        Node n = it.nextNode(); %>
                <li>
                    <%=
                    n.getName()
                    %>
                </li>
                <% } %>
            </ul>
            <% } %>
        </div>
    </div>
</div>
</body>
</html> 
