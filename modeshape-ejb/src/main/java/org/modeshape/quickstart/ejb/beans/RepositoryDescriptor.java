/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.quickstart.ejb.beans;

import java.util.Map;
import java.util.TreeMap;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class that describes a repository after looking it up in JNDI.
 */
public abstract class RepositoryDescriptor {

    protected Logger log = LoggerFactory.getLogger(getClass());
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
