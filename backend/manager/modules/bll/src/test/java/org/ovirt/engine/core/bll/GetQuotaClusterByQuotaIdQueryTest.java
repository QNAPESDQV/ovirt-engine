package org.ovirt.engine.core.bll;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.ovirt.engine.core.common.businessentities.QuotaCluster;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dao.QuotaDao;

/**
 * A test for the {@link GetQuotaClusterByQuotaIdQuery} class.
 * This is a flow test that uses mocking to verify the correct Daos are called.
 */
public class GetQuotaClusterByQuotaIdQueryTest
        extends AbstractQueryTest<IdQueryParameters, GetQuotaClusterByQuotaIdQuery<IdQueryParameters>> {

    @Test
    public void testExecuteQueryCommand() {
        // Mock the parameters
        Guid quotaId = Guid.newGuid();
        when(params.getId()).thenReturn(quotaId);

        // Create the return value
        QuotaCluster group = new QuotaCluster();
        group.setQuotaId(quotaId);

        // Mock the Dao
        QuotaDao quotaDao = mock(QuotaDao.class);
        when(quotaDao.getQuotaClusterByQuotaGuidWithGeneralDefault(quotaId)).thenReturn(Collections.singletonList(group));
        when(getDbFacadeMockInstance().getQuotaDao()).thenReturn(quotaDao);

        // Execute the query
        getQuery().executeQueryCommand();

        // Assert the result
        @SuppressWarnings("unchecked")
        List<QuotaCluster> results = getQuery().getQueryReturnValue().getReturnValue();
        assertEquals("Wrong number of results returned", 1, results.size());
        assertEquals("Wrong results returned", group, results.get(0));
    }
}
