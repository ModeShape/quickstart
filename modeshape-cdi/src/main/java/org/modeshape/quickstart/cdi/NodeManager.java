package org.modeshape.quickstart.cdi;

import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Session;

/**
 * @author Richard Lucas
 */
@Stateless
public class NodeManager {

    private static final Logger LOGGER = Logger.getLogger(NodeManager.class);

    @Inject
    private Session repositorySession;

    public void addNode(String parentPath, String nodeNode, int index) {
        try {
            Node parentNode = repositorySession.getNode(parentPath);
            String name = nodeNode + index;
            parentNode.addNode(name);
            repositorySession.save();
            LOGGER.info("Added new node " + name);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
