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
package org.modeshape.quickstart.ejb.beans;

import java.util.Map;
import java.util.TreeMap;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.logging.Logger;
import org.modeshape.jcr.api.JcrTools;

/**
 * A utility class that describes a repository after looking it up in JNDI.
 */
public abstract class RepositoryDescriptor {

    protected Logger log = Logger.getLogger(getClass());
    protected JcrTools jcrTools = new JcrTools();

    /**
     * Returns a map which contains the repository descriptors for a given repository.
     *
     * @param repositoryName a {@code non-null} {@link String} representing the simple name of a repository
     * @return a {@link Map} of {@code descriptor key, descriptor value} pairs.
     */
    public Map<String, String> describeRepository( String repositoryName ) {
        log.info("Retrieving description for repository: " + repositoryName);
        Repository repository = getRepositoryFromJndi(repositoryName);
        Map<String, String> descriptors = new TreeMap<String, String>();
        for (String descriptorKey : repository.getDescriptorKeys()) {
            descriptors.put(descriptorKey, repository.getDescriptor(descriptorKey));
        }
        return descriptors;
    }

    /**
     * Counts the number of nodes in a respository, by using a default session.
     *
     * @param repositoryName a {@code non-null} {@link String} representing the simple name of a repository
     * @return the number of nodes in the given repository.
     * @throws RepositoryException if anything unexpected fails.
     */
    public long countNodes( String repositoryName ) throws RepositoryException {
        log.info("Counting nodes for repository: " + repositoryName);
        Repository repository = getRepositoryFromJndi(repositoryName);
        Session session = repository.login();
        JcrTools.CountNodes countNodes = new JcrTools.CountNodes(jcrTools);
        countNodes.run(session);
        return countNodes.numNonSystemNodes;
    }

    protected Repository getRepositoryFromJndi( String jndiName ) throws IllegalArgumentException {
        if (!jndiName.startsWith("java:")) {
            jndiName = "java:/jcr/" + jndiName;
        }

        try {
            InitialContext context = new InitialContext();
            Object object = context.lookup(jndiName);
            if (!(object instanceof Repository)) {
                throw new IllegalArgumentException("Expected to find a javax.jcr.Repository instance, but found a " +
                                                           object.getClass().toString() + " instance in JNDI at: " + jndiName);
            }
            log.info("Successfully retrieved repository at: " + jndiName);
            return (Repository)object;
        } catch (NamingException e) {
            throw new IllegalArgumentException(jndiName + " cannot be located in JNDI");
        }
    }
}
