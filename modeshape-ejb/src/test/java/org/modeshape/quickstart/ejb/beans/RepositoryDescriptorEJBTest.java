package org.modeshape.quickstart.ejb.beans;

import java.io.File;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Arquillian test which validates the functionality provided by the {@link StatelessBMTRepositoryDescriptor}, {@link SingletonRepositoryDescriptor}
 * and {@link StatelessCMTRepositoryDescriptor} ejbs.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@RunWith(Arquillian.class)
public class RepositoryDescriptorEJBTest {

    private static final String TEST_REPOSITORY = "sample";

    @EJB
    private SingletonRepositoryDescriptor singletonRepositoryDescriptor;

    @EJB
    private StatelessBMTRepositoryDescriptor statelessBMTRepositoryDescriptor;

    @EJB
    private StatelessCMTRepositoryDescriptor statelessCMTRepositoryDescriptor;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-ejb-test.war")
                .addClass(RepositoryDescriptor.class)
                .addClass(SingletonRepositoryDescriptor.class)
                .addClass(StatelessBMTRepositoryDescriptor.class)
                .addClass(StatelessCMTRepositoryDescriptor.class)
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }

    @Before
    public void beforeEach() {
        assertNotNull("Singleton bean not injected", singletonRepositoryDescriptor);
        assertNotNull("Stateless BMT bean not injected", statelessBMTRepositoryDescriptor);
        assertNotNull("Stateless CMT bean not injected", statelessCMTRepositoryDescriptor);
    }

    @Test
    public void shouldDescribeRepository() throws Exception {
        assertTrue(!singletonRepositoryDescriptor.describeRepository(TEST_REPOSITORY).isEmpty());
        assertTrue(!statelessBMTRepositoryDescriptor.describeRepository(TEST_REPOSITORY).isEmpty());
        assertTrue(!statelessCMTRepositoryDescriptor.describeRepository(TEST_REPOSITORY).isEmpty());
    }

    @Test
    public void shouldCountNodes() throws Exception {
        assertTrue(singletonRepositoryDescriptor.countNodes(TEST_REPOSITORY) > 0);
        assertTrue(statelessBMTRepositoryDescriptor.countNodes(TEST_REPOSITORY) > 0);
        assertTrue(statelessCMTRepositoryDescriptor.countNodes(TEST_REPOSITORY) > 0);
    }
}
