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
