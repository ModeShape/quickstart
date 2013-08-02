package org.modeshape.quickstart.federation;

import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Arquillian test for {@link FederationController}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@RunWith(Arquillian.class)
public class FederationControllerTest {

    @Inject
    private FederationController federationController;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-federation-controller-test.war")
                .addClass(FederationController.class)
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }

    @Before
    public void before() {
        assertNotNull("Clustering controller not injected", federationController);
    }

    @Test
    public void shouldReturnValidRepositoryName() throws Exception {
        assertEquals("federated-repository", federationController.getRepositoryName());
    }

    @Test
    public void shouldLoadChildrenForFSSource() throws Exception {
        federationController.setExternalSource(FederationController.FS_SOURCE_PROJECTION);
        federationController.loadChildren();
        assertTrue(!federationController.getNodes().isEmpty());
    }

    @Test
    public void shouldLoadChildrenForDBMetadataSource() throws Exception {
        federationController.setExternalSource(FederationController.DB_METADATA_SOURCE_PROJECTION);
        federationController.loadChildren();
        assertTrue(!federationController.getNodes().isEmpty());
    }
}
