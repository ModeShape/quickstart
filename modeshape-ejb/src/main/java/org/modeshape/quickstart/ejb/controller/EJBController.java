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

    protected enum RepoProvider {
        SINGLETON("Stateful Singleton EJB"),
        STATELESS_CMT("Stateless CMT EJB"),
        STATELESS_BMT("Stateless BMT EJB");

        String description;

        private RepoProvider( String description ) {
            this.description = description;
        }
    }
}
