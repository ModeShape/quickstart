package org.modeshape.quickstart.cdi;

import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * @author Richard Lucas
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class NodeManager {

    private static final Logger LOGGER = Logger.getLogger(NodeManager.class);

    @Inject
    private Session repositorySession;

    @Inject
    private UserTransaction userTransaction;

    public void addNode(String parentPath, String nodeNode, int index) {
        try {
            LOGGER.info("Adding node");
            userTransaction.begin();
            Node parentNode = repositorySession.getNode(parentPath);
            String name = nodeNode + index;
            parentNode.addNode(name);
            repositorySession.save();
            LOGGER.info("Added new node " + name);
            userTransaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
                    userTransaction.rollback();
            } catch (Throwable e) {
                LOGGER.error("Failed to rollback", e);
            }
        }
    }
}
