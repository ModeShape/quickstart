/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.quickstart.servlet;

import java.io.IOException;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;

/**
 * Demo for using JCR from web application. This example application accepts a "repository" parameter and a "path" parameter,
 * while most practical applications will know where to find the repository URL (e.g. via configuration, perhaps via the web.xml),
 * and will determine the path from the requests' URL.
 * 
 * @author kulikov
 * @author Horia Chiorean
 */
public class RepositoryServlet extends HttpServlet {
    public static final String CHILDREN_ATTRIBUTE = "children";
    public static final String REPOSITORY_PARAM = "repository";
    public static final String NODE_PATH_PARAM = "path";
    public static final String ERROR_MESSAGE = "errorMessage";

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(RepositoryServlet.class);

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws IOException {
        cleanSession(request);
        String repositoryLocation = storeRequestParameterIntoSession(request, REPOSITORY_PARAM, null);

        if (repositoryLocation != null && repositoryLocation.trim().length() > 0) {
            Session jcrSession = null;
            try {
                // Lookup repository ...
                Repository repository = getRepository(repositoryLocation);

                // establish jcr session ...
                jcrSession = repository.login();
                LOGGER.info("Session established successfully to repository at: " + repositoryLocation);

                // get the path of the node
                String path = storeRequestParameterIntoSession(request, NODE_PATH_PARAM, "/");
                // display children
                try {
                    Node node = jcrSession.getNode(path);
                    request.getSession().setAttribute(CHILDREN_ATTRIBUTE, node.getNodes());
                } catch (PathNotFoundException e) {
                    displayError(request, "No node can be located at: " + path);
                }
            } catch (Exception e) {
                LOGGER.error("Could not complete request", e);
                displayError(request, e.getMessage());
            } finally {
                // Always log out of the jcrSession ...
                if (jcrSession != null) {
                    jcrSession.logout();
                    LOGGER.info("Logged out from jcrSession");
                }
            }
        } else {
            displayError(request, "The repository location is required");
        }

        response.sendRedirect("main.jsp");
    }

    private Repository getRepository( String location ) throws Exception {
        Repository repository = searchJNDI(location);
        if (repository == null) {
            repository = searchURL(location);
        }

        if (repository == null) {
            throw new RuntimeException("Unable to lookup repository at '" + location + "'. Please check the format.");
        }

        return repository;
    }

    private Repository searchURL( String location ) throws RepositoryException {
        if (location.startsWith("jndi:") ||
                location.startsWith("file:") ||
                location.startsWith("classpath:") ||
                location.startsWith("http:")) {
            // Look up the repository using JCR's RepositoryFactory. This seems like a lot more work, but it actually
            // is far more capable and can handle a much wider range of "location" formats. This code even works in
            // a Java SE application, so this approach is great for libraries that can be used in any kind of application.
            // The "parameters" map could actually be obtained by reading in the entries from a properties file or via
            // properties in the web.xml; this way, the code contains no specific settings for ModeShape.
            Map<String, String> parameters = java.util.Collections.singletonMap("org.modeshape.jcr.URL", location);
            for (RepositoryFactory factory : java.util.ServiceLoader.load(RepositoryFactory.class)) {
                Repository repository = factory.getRepository(parameters);
                if (repository != null) {
                    return repository;
                }
            }
        }
        return null;
    }

    private Repository searchJNDI( String location ) {
        Repository repository = null;
        try {
            LOGGER.debug("Searching in JNDI directly at: " + location);
            InitialContext ic = new InitialContext();
            repository = (Repository)ic.lookup(location);
        } catch (NamingException e) {
            LOGGER.debug("Naming exception", e);
        }

        if (repository == null) {
            location = "java:/jcr/" + location;
            try {
                LOGGER.debug("Searching in JNDI directly at: " + location);
                InitialContext ic = new InitialContext();
                repository = (Repository)ic.lookup(location);
            } catch (NamingException e) {
                LOGGER.debug("Naming exception", e);
            }
        }

        return repository;
    }

    private void cleanSession( HttpServletRequest request ) {
        request.getSession(true).removeAttribute(ERROR_MESSAGE);
        request.getSession().removeAttribute(CHILDREN_ATTRIBUTE);
    }

    private void displayError( HttpServletRequest request,
                               String message ) {
        request.getSession().setAttribute(ERROR_MESSAGE, message);
    }

    private String storeRequestParameterIntoSession( HttpServletRequest request,
                                                     String name,
                                                     String defaultValue ) {
        String paramValue = request.getParameter(name);
        if (paramValue == null || paramValue.trim().length() == 0) {
            paramValue = defaultValue;
        }
        request.getSession(true).setAttribute(name, paramValue);
        return paramValue;
    }
}
