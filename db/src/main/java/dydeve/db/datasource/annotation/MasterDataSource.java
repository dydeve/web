package dydeve.db.datasource.annotation;

/**
 * usually, we read from slaves and write to master.
 * but if case of read operation in a transaction, we read from the master db.
 * Created by dy on 2017/8/20.
 */
public @interface MasterDataSource {
}
