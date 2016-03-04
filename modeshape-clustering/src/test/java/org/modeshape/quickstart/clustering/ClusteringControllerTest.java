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
 * Arquillian test for {@link org.modeshape.quickstart.clustering.ClusteringController}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@RunWith(Arquillian.class)
public class ClusteringControllerTest {

    @Inject
    private ClusteringController clusteringController;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-clustering-controller-test.war")
                .addClass(ClusteringController.class)
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }

    @Before
    public void before() {
        assertNotNull("Clustering controller not injected", clusteringController);
    }

    @Test
    public void shouldReturnValidRepositoryName() throws Exception {
        assertEquals("sample", clusteringController.getRepositoryName());
    }

    @Test
    public void shouldLoadChildren() throws Exception {
        clusteringController.setParentPath("/");
        clusteringController.loadChildren();
        assertTrue(!clusteringController.getNodes().isEmpty());
    }

    @Test
    public void shouldAddNewChild() throws Exception {
        clusteringController.setParentPath("/");
        clusteringController.setNewNodeName("test");
        clusteringController.addChildNode();
        assertTrue(clusteringController.getNodes().contains("/test"));
    }

    @Test
    public void shouldSearchForNodes() throws Exception {
        clusteringController.setParentPath("/");
        clusteringController.setNewNodeName("test");
        clusteringController.addChildNode();

        clusteringController.setNodeNamePattern("test");
        clusteringController.searchForNodes();
        assertTrue(clusteringController.getNodes().contains("/test"));
    }
}
