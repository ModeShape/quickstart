package org.modeshape.quickstart.ejb.controller;

import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modeshape.quickstart.ejb.beans.RepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.SingletonRepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.StatelessBMTRepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.StatelessCMTRepositoryDescriptor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Arquillian test which validates the functionality provided by the {@link org.modeshape.quickstart.ejb.controller.EJBController} class
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@RunWith(Arquillian.class)
public class EJBControllerTest {
    private static final String TEST_REPOSITORY = "test-ejb";

    @Inject
    private EJBController ejbController;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-ejb-controller-test.war")
                .addClass(RepositoryDescriptor.class)
                .addClass(SingletonRepositoryDescriptor.class)
                .addClass(StatelessBMTRepositoryDescriptor.class)
                .addClass(StatelessCMTRepositoryDescriptor.class)
                .addClass(EJBController.class)
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }

    @Before
    public void beforeEach() {
        assertNotNull("EJB Controller not injected", ejbController);
        ejbController.setRepositoryName(TEST_REPOSITORY);
    }

    @Test
    public void shouldDescribeRepository() throws Exception {
        retrieveAndAssertDescription(EJBController.RepoProvider.STATELESS_BMT);
        retrieveAndAssertDescription(EJBController.RepoProvider.STATELESS_CMT);
        retrieveAndAssertDescription(EJBController.RepoProvider.SINGLETON);
    }

    private void retrieveAndAssertDescription( EJBController.RepoProvider repoProvider ) {
        ejbController.setSelectedEJB(repoProvider.name());
        ejbController.retrieveDescription();
        assertTrue(!ejbController.getRepositoryDescription().isEmpty());
    }

    @Test
    public void shouldCountNodesInRepository() throws Exception {
        retrieveAndAssertNodesCount(EJBController.RepoProvider.SINGLETON);
        retrieveAndAssertNodesCount(EJBController.RepoProvider.STATELESS_CMT);
        retrieveAndAssertNodesCount(EJBController.RepoProvider.STATELESS_BMT);
    }

    private void retrieveAndAssertNodesCount( EJBController.RepoProvider repoProvider ) {
        ejbController.setSelectedEJB(repoProvider.name());
        ejbController.countNodes();
        assertTrue(ejbController.getNodesCount() > 0);
    }
}
