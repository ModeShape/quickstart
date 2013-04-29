package org.modeshape.quickstart.cli;

import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.jcr.Repository;

/**
 * A simple JSF controller, that uses CDI to obtain a ModeShape {@link javax.jcr.Repository} that was created via a CLI script
 * and provides some information about the repository.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Named("cliController")
@RequestScoped
public class CLIController {

    @Resource( mappedName = "java:/jcr/test-cli" )
    private Repository repository;

    /**
     * Returns a map with the repository descriptors.
     *
     * @return a non-null {@link Map}
     */
    public Map<String, String> getRepositoryDescription() {
        TreeMap<String, String> descriptorsMap = new TreeMap<String, String>();
        if (repository == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(
                                                                 "Repository was not injected correctly. Please check for any errors during server startup"));
        } else {
            for (String descriptorKey : repository.getDescriptorKeys()) {
                descriptorsMap.put(descriptorKey, repository.getDescriptor(descriptorKey));
            }
        }
        return descriptorsMap;
    }


    /**
     * Returns the name of the repository.
     *
     * @return a non-null string.
     */
    public String getRepositoryName() {
        return repository.getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
    }
}
