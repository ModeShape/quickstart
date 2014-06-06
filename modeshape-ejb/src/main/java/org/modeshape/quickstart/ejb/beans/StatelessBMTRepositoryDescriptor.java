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
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jcr.RepositoryException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * A stateless EJB that extends {@link RepositoryDescriptor}.
 */
@Stateless
@TransactionManagement( TransactionManagementType.BEAN )
public class StatelessBMTRepositoryDescriptor extends RepositoryDescriptor {

    @Resource
    private UserTransaction utxn;

    @Override
    public Map<String, String> describeRepository( String repositoryName ) {
        Map<String, String> descriptors = new TreeMap<String, String>();
        try {
            utxn.begin();
            descriptors = super.describeRepository(repositoryName);
            utxn.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                utxn.rollback();
            } catch (SystemException e1) {
                log.error(e1.getMessage(), e1);
            }
        }
        return descriptors;
    }

    @Override
    public long countNodes( String repositoryName ) throws RepositoryException {
        try {
            utxn.begin();
            long count = super.countNodes(repositoryName);
            utxn.commit();
            return count;
        } catch (Exception e) {
            try {
                utxn.rollback();
            } catch (SystemException e1) {
                log.error(e1.getMessage(), e1);
            }
            throw new RuntimeException(e);
        }
    }
}
