package org.modeshape.quickstart.federation;

import java.util.HashMap;
import java.util.Map;
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
import org.jboss.logging.Logger;

/**
 * A simple JSF controller, that uses a repository which is injected directly.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Named( "federationController" )
@ApplicationScoped
public class FederationController {

    static final String FS_SOURCE_PROJECTION = "JBossDataDir";
    static final String DB_METADATA_SOURCE_PROJECTION = "ExampleDB";

    private static final Logger LOGGER = Logger.getLogger(FederationController.class);
    private static final Map<String, String> EXTERNAL_SOURCES = new HashMap<String, String>(2);

    @Resource(mappedName = "java:/jcr/federated-repository")
    private transient Repository repository;

    private String externalSource;
    private Set<String> nodes;


    static {
        EXTERNAL_SOURCES.put("JBoss Server Data Dir", FS_SOURCE_PROJECTION);
        EXTERNAL_SOURCES.put("Example DB", DB_METADATA_SOURCE_PROJECTION);
    }

    /**
     * Returns the pre-configured external sources.
     * @return the map (label, value) pairs
     */
    public Map<String, String> getExternalSources() {
        return EXTERNAL_SOURCES;
    }

    /**
     * Returns the value of the selected external source.
     *
     * @return a {@code non-null} string
     */
    public String getExternalSource() {
        return externalSource;
    }

    /**
     * Sets the value of the selected external source
     *
     * @param externalSource a {@code non-null} string
     */
    public void setExternalSource( String externalSource ) {
        this.externalSource = externalSource;
    }

    /**
     * Returns the set of children nodes loaded as a result of {@link #loadChildren()}
     *
     * @return a Set<String> containing the paths of the children.
     */
    public Set<String> getNodes() {
        return nodes;
    }

    /**
     * Loads the children nodes from an external source, based on the value of the {@link #externalSource} selection.
     *
     * @return the view to redirect to.
     */
    public String loadChildren() {
        nodes = new TreeSet<String>();
        if (externalSource == null || externalSource.trim().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please select an external source"));
        } else {
            Session repositorySession = null;
            try {
                repositorySession = newSession();
                addChildrenFromPath(repositorySession, "/" + externalSource);
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

    private void addChildrenFromPath( Session repositorySession, String path ) throws RepositoryException {
        Node parentNode = repositorySession.getNode(path);
        for (NodeIterator nodeIterator = parentNode.getNodes(); nodeIterator.hasNext(); ) {
            Node node = null;
            try {
                node = nodeIterator.nextNode();
            } catch (Exception e) {
                LOGGER.warn("Cannot load node: " + path + " because: "+ e.getMessage());
                continue;
            }
            String primaryType = node.getPrimaryNodeType().getName();
            nodes.add(node.getPath() + " (" + primaryType + ")");
            addChildrenFromPath(repositorySession, node.getPath());
        }
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
        return repository.login();
    }
}
