/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mysql.legacy;

import java.util.concurrent.atomic.AtomicBoolean;

import io.debezium.connector.base.ChangeEventQueueMetrics;
import io.debezium.connector.mysql.MySqlPartition;
import io.debezium.pipeline.metrics.DefaultSnapshotChangeEventSourceMetrics;

/**
 * @author Randall Hauch
 *
 */
class SnapshotReaderMetrics extends DefaultSnapshotChangeEventSourceMetrics<MySqlPartition>
        implements SnapshotReaderMetricsMXBean {

    private final AtomicBoolean holdingGlobalLock = new AtomicBoolean();

    private final MySqlSchema schema;

    public SnapshotReaderMetrics(MySqlTaskContext taskContext, MySqlSchema schema, ChangeEventQueueMetrics changeEventQueueMetrics) {
        super(taskContext, changeEventQueueMetrics, null);
        this.schema = schema;
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
