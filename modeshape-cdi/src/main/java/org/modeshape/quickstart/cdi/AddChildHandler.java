package org.modeshape.quickstart.cdi;

import org.jboss.logging.Logger;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * @author Richard Lucas
 */
public class AddChildHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(AddChildHandler.class);

    private final String parentPath;
    private final String nodeName;
    private final int index;

    public AddChildHandler(String parentPath, String nodeName, int index) {
        this.parentPath = parentPath;
        this.nodeName = nodeName;
        this.index = index;
    }


    @Override
    public void run() {

//        try {
//            Thread.sleep(500 * index);
//        } catch (InterruptedException e) {
//            LOGGER.warn(e.getMessage(), e);
//        }

        UserTransaction userTransaction = null;
        Session repositorySession = null;
        try {
            InitialContext ctx = new InitialContext();

            Repository sampleRepository = (Repository) ctx.lookup("java:/jcr/sample");
            repositorySession = sampleRepository.login();

            userTransaction = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            LOGGER.info("Adding node");
            userTransaction.begin();
            Node parentNode = repositorySession.getNode(parentPath);
            String name = nodeName + index;
            parentNode.addNode(name);
            repositorySession.save();
            LOGGER.info("Added new node " + name);
            userTransaction.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (userTransaction != null && userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                    userTransaction.rollback();
                }

                if (repositorySession != null) {
                    repositorySession.logout();
                }

            } catch (Throwable e) {
                LOGGER.error("Failed to rollback", e);
            }
        }
    }
}
