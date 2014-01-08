package org.modeshape.quickstart.cdi;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.jboss.logging.Logger;

/**
 * A simple managed bean, that provides clients with {@link javax.jcr.Session} instances.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SessionProducer {

    private static final Logger LOGGER = Logger.getLogger(SessionProducer.class);

    @Resource(mappedName = "java:/jcr/sample")
    private Repository sampleRepository;

    /**
     * Producer method which returns a default session to the preconfigured repository.
     *
     * @return a {@link Session} instance
     * @throws RepositoryException if anything goes wrong
     */
    @RequestScoped
    @Produces
    public Session getSession() throws RepositoryException {
        LOGGER.info("Creating new session...");
        return sampleRepository.login();
    }

    /**
     * Disposer method for {@link org.modeshape.quickstart.cdi.SessionProducer#getSession()}
     *
     * @param session the {@link Session} created by the producer method
     */
    public void logoutSession( @Disposes final Session session ) {
        LOGGER.info("Closing session...");
        session.logout();
    }
}
