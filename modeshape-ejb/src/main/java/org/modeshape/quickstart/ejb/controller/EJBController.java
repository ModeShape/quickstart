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
package org.modeshape.quickstart.ejb.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.modeshape.quickstart.ejb.beans.RepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.SingletonRepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.StatelessBMTRepositoryDescriptor;
import org.modeshape.quickstart.ejb.beans.StatelessCMTRepositoryDescriptor;

/**
 * A simple request scoped bean that handles invocations towards different EJBs.
 *
 * @author Horia Chiorean
 */
@Named("ejbController")
@RequestScoped
public class EJBController implements Serializable {
    @EJB
    private SingletonRepositoryDescriptor singletonEJB;

    @EJB
    private StatelessBMTRepositoryDescriptor statelessBMTEJB;

    @EJB
    private StatelessCMTRepositoryDescriptor statelessCMTEJB;

    private String repositoryName;
    private String selectedEJB = RepoProvider.SINGLETON.name();

    private Long nodesCount;
    private Map<String, String> repositoryDescription;

    /**
     * Returns the objects used for the html select element that will allow users to select an EJB.
     *
     * @return a SelectItem[]
     */
    public SelectItem[] getAvailableEJBs() {
        RepoProvider[] ejbs = RepoProvider.values();
        List<SelectItem> items = new ArrayList<SelectItem>(ejbs.length);
        for (RepoProvider ejb : ejbs) {
            items.add(new SelectItem(ejb.name(), ejb.description));
        }
        return items.toArray(new SelectItem[items.size()]);
    }

    /**
     * Returns the EJB that was chosen by the user.
     *
     * @return a {@link RepositoryDescriptor} instance
     */
    public String getSelectedEJB() {
        return selectedEJB;
    }

    /**
     * Sets the active EJB to which calls will be delegated
     *
     * @param selectedEJB a {@link RepositoryDescriptor} instance
     */
    public void setSelectedEJB( String selectedEJB ) {
        this.selectedEJB = selectedEJB;
    }

    /**
     * Counts all the nodes in a repository using the user selected EJB and the inputted repository name.
     *
     * @return the nodes count or null, if there was an error.
     */
    public Long getNodesCount() {
        return nodesCount;
    }

    /**
     * Counts all the nodes in a repository, using the user selected EJB and the inputted repository name.
     */
    public void countNodes() {
        try {
            nodesCount = selectedEJB().countNodes(repositoryName);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    /**
     * Gets the description of a repository, using the user selected EJB and the inputted repository name.
     */
    public void retrieveDescription() {
        try {
            repositoryDescription = selectedEJB().describeRepository(repositoryName);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    /**
     * Returns a repository description map.
     *
     * @return a Map<String, String> or null, if there was an error.
     */
    public Map<String, String> getRepositoryDescription() {
        return repositoryDescription;
    }

    /**
     * Returns the name of the inputted repository.
     *
     * @return the name of a repository.
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the name of a repository.
     *
     * @param repositoryName a string.
     */
    public void setRepositoryName( String repositoryName ) {
        this.repositoryName = repositoryName;
    }

    private RepositoryDescriptor selectedEJB() {
        RepoProvider ejb = RepoProvider.valueOf(selectedEJB);
        switch (ejb) {
            case SINGLETON: {
                return singletonEJB;
            }
            case STATELESS_CMT: {
                return statelessCMTEJB;
            }
            case STATELESS_BMT: {
                return statelessBMTEJB;
            }
            default: {
                throw new IllegalArgumentException("Unknown EJB name: " + selectedEJB);
            }
        }
    }

    private enum RepoProvider {
        SINGLETON("Stateful Singleton EJB"),
        STATELESS_CMT("Stateless CMT EJB"),
        STATELESS_BMT("Stateless BMT EJB");

        String description;

        private RepoProvider( String description ) {
            this.description = description;
        }
    }
}
