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

package org.modeshape.quickstart.servlet;

import java.io.File;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * Arquillian test for {@link RepositoryServlet}. For this to pass, make sure that the active server has a repository
 * named "sample".
 */
@RunWith(Arquillian.class)
@RunAsClient
public class RepositoryServletTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "modeshape-servlet-test.war").addClass(RepositoryServlet.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File("src/test/resources/log4j.properties"))
                .addAsWebResource(new File("src/main/webapp/dblue106.gif"))
                .addAsWebResource(new File("src/main/webapp/lgrey029.jpg"))
                .addAsWebResource(new File("src/main/webapp/main.jsp"))
                .addAsWebResource(new File("src/main/webapp/modeshape_icon_64px_med.png"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Test
    public void shouldExecuteQuery() throws Exception {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        Assert.assertEquals(200,
                            requestFactory.buildGetRequest(new GenericUrl(
                                    "http://localhost:8080/modeshape-servlet-test/session.do?repository=sample&path=/"))
                                    .execute().getStatusCode());
    }
}

