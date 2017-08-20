package dydeve.db.datasource;

import org.springframework.core.Constants;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dy on 2017/8/20.
 */
public class MultiDataSourceRouter extends AbstractRoutingDataSource {




    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    /** Constants instance for TransactionDefinition */
    private static final Constants constants = new Constants(TransactionDefinition.class);


    /**
     * Supports Integer values for the isolation level constants
     * as well as isolation level names as defined on the
     * {@link org.springframework.transaction.TransactionDefinition TransactionDefinition interface}.
     */
    @Override
    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        if (lookupKey instanceof Integer) {
            return lookupKey;
        }
        else if (lookupKey instanceof String) {
            String constantName = (String) lookupKey;
            if (!constantName.startsWith(DefaultTransactionDefinition.PREFIX_ISOLATION)) {
                throw new IllegalArgumentException("Only isolation constants allowed");
            }
            return constants.asNumber(constantName);
        }
        else {
            throw new IllegalArgumentException(
                    "Invalid lookup key - needs to be isolation level Integer or isolation level name String: " + lookupKey);
        }
    }

    /*@Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
    }*/
}
