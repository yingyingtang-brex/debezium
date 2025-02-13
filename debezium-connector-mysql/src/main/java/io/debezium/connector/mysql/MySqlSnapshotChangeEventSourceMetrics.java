/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mysql;

import java.util.concurrent.atomic.AtomicBoolean;

import io.debezium.connector.base.ChangeEventQueueMetrics;
import io.debezium.pipeline.metrics.DefaultSnapshotChangeEventSourceMetrics;
import io.debezium.pipeline.source.spi.EventMetadataProvider;

/**
 * @author Randall Hauch
 *
 */
class MySqlSnapshotChangeEventSourceMetrics extends DefaultSnapshotChangeEventSourceMetrics<MySqlPartition> implements MySqlSnapshotChangeEventSourceMetricsMXBean {

    private final AtomicBoolean holdingGlobalLock = new AtomicBoolean();

    private final MySqlDatabaseSchema schema;

    public MySqlSnapshotChangeEventSourceMetrics(MySqlTaskContext taskContext, ChangeEventQueueMetrics changeEventQueueMetrics,
                                                 EventMetadataProvider eventMetadataProvider) {
        super(taskContext, changeEventQueueMetrics, eventMetadataProvider);
        this.schema = taskContext.getSchema();
    }

    @Override
    public boolean getHoldingGlobalLock() {
        return holdingGlobalLock.get();
    }

    public void globalLockAcquired() {
        holdingGlobalLock.set(true);
    }

    public void globalLockReleased() {
        holdingGlobalLock.set(false);
    }

    @Override
    public String[] getMonitoredTables() {
        return schema.capturedTablesAsStringArray();
    }

    @Override
    public String[] getCapturedTables() {
        return schema.capturedTablesAsStringArray();
    }

    @Override
    public long getTotalNumberOfEventsSeen() {
        return getRowsScanned().values().stream()
                .mapToLong((x) -> x)
                .sum();
    }
}
