/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.modeshape.quickstart.clustering;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import org.jboss.logging.Logger;

/**
 * A simple JSF controller, that uses a repository which is injected directly,
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Named( "clusteringController" )
@ApplicationScoped
public class ClusteringController {

    private static final Logger LOGGER = Logger.getLogger(ClusteringController.class);

    @Resource(mappedName = "java:/jcr/sample")
    private transient Repository clusteredRepository;

    private String parentPath = "/";
    private String newNodeName;
    private String nodeNamePattern;

    private Set<String> nodes = new TreeSet<String>();

    /**
     * Sets a name for a new node to create.
     *
     * @param newNodeName a {@code non-null} string
     */
    public void setNewNodeName( String newNodeName ) {
        this.newNodeName = newNodeName;
    }

    /**
     * Returns the name of a new node which can be created.
     *
     * @return a {@code non-null} string
     */
    public String getNewNodeName() {
        return newNodeName;
    }

    /**
     * Returns the pattern of the name of the node to search after.
     *
     * @return a {@code non-null} string
     */
    public String getNodeNamePattern() {
        return nodeNamePattern;
    }

    /**
     * Sets the name pattern of the node to search after.
     *
     * @param nodeNamePattern a {@code non-null} string
     */
    public void setNodeNamePattern( String nodeNamePattern ) {
        this.nodeNamePattern = nodeNamePattern;
    }

    /**
     * Returns the absolute path of a parent node
     *
     * @return a {@code non-null} string
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * Sets the absolute path of a parent node.
     *
     * @param parentPath a {@code non-null} string
     */
    public void setParentPath( String parentPath ) {
        this.parentPath = parentPath;
    }

    /**
     * Returns the children nodes of the node located at {@link ClusteringController#parentPath}
     *
     * @return a Set<String> containing the paths of the children.
     */
    public Set<String> getNodes() {
        return nodes;
    }

    /**
     * Searches for nodes that have a given pattern in their [jcr:name].
     *
     * @return the view to redirect to.
     */
    public String searchForNodes() {
        nodes = new TreeSet<String>();
        if (nodeNamePattern == null || nodeNamePattern.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    "The node name pattern is required"));
        } else  {
            Session repositorySession = null;
            try {
                repositorySession = newSession();
                QueryManager queryManager = repositorySession.getWorkspace().getQueryManager();
                String queryString = "select node.[jcr:name] from [nt:unstructured] as node where node.[jcr:name] like '%" + nodeNamePattern + "%'";
                LOGGER.info("Executing query:" + queryString);
                QueryResult queryResult = queryManager.createQuery(queryString, Query.JCR_SQL2).execute();
                for (NodeIterator nodeIterator = queryResult.getNodes(); nodeIterator.hasNext(); ) {
                    nodes.add(nodeIterator.nextNode().getPath());
                }
            } catch (RepositoryException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            } finally {
                if (repositorySession != null) {
                    repositorySession.logout();
                }
            }
        }

        return "/main.xhtml";
    }


    /**
     * Loads the children nodes of the node located at {@link ClusteringController#parentPath}
     *
     * @return the view to redirect to.
     */
    public String loadChildren() {
        nodes = new TreeSet<String>();
        if (parentPath == null || parentPath.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    "The absolute path of the parent node is required"));
        } else {
            Session repositorySession = null;
            try {
                repositorySession = newSession();
                Node parentNode = repositorySession.getNode(parentPath);
                for (NodeIterator nodeIterator = parentNode.getNodes(); nodeIterator.hasNext(); ) {
                    nodes.add(nodeIterator.nextNode().getPath());
                }
            } catch (RepositoryException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            } finally {
                if (repositorySession != null) {
                    repositorySession.logout();
                }
            }
        }
        return "/main.xhtml";
    }

    /**
     * Adds a child node at {@link ClusteringController#parentPath} which has the name {@link ClusteringController#newNodeName}
     *
     * @return the view to redirect to.
     */
    public String addChildNode() {
        if (newNodeName == null || newNodeName.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The name of the new node is required"));
        } else {
            Session repositorySession = null;
            try {
                repositorySession = newSession();
                Node parentNode = repositorySession.getNode(parentPath);
                parentNode.addNode(newNodeName);
                repositorySession.save();
            } catch (RepositoryException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            } finally {
                if (repositorySession != null) {
                    repositorySession.logout();
                }
            }
        }
        return loadChildren();
    }

    /**
     * Returns the name of the repository to which the session is bound.
     *
     * @return a non-null string.
     * @throws RepositoryException if anything unexpected fails
     */
    public String getRepositoryName() throws RepositoryException{
        Session repositorySession = newSession();
        try {
            return repositorySession.getRepository().getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
        } finally {
            repositorySession.logout();
        }
    }

    private Session newSession() throws RepositoryException {
        return clusteredRepository.login();
    }
}
