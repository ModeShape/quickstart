package org.modeshape.quickstart.cdi;

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
 * Arquillian test for {@link CDIController}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@RunWith(Arquillian.class)
public class CDIControllerTest {

    @Inject
    private CDIController cdiController;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-cdi-controller-test.war")
                .addClass(CDIController.class)
                .addClass(SessionProducer.class)
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }

    @Before
    public void before() {
        assertNotNull("CDI controller not injected", cdiController);
    }

    @Test
    public void shouldReturnValidRepositoryName() throws Exception {
        assertEquals("sample", cdiController.getRepositoryName());
    }

    @Test
    public void shouldLoadChildren() throws Exception {
        cdiController.setParentPath("/");
        cdiController.loadChildren();
        assertTrue(!cdiController.getChildren().isEmpty());
    }

    @Test
    public void shouldAddNewChild() throws Exception {
        cdiController.setParentPath("/");
        cdiController.setNewNodeName("test");
        cdiController.addChildNode();
        assertTrue(cdiController.getChildren().contains("/test"));
    }
}
