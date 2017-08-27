package dydeve.db.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * Created by dy on 2017/8/27.
 */
public class MultiDataSourceTransactionManager extends DataSourceTransactionManager {

    /**
     * 确保只读事务尽量读读库
     * 读写、写事务 只走写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        if (definition.isReadOnly()) {

        } else {

        }

        super.doBegin(transaction, definition);
    }

    /**
     * 清理threadLocal
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
    }
}
