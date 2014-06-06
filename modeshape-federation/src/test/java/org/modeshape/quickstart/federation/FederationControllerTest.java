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
