package pcd.ass04.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {

    private final Lock readLock;
    private final Lock writeLock;

    protected AbstractRepository() {
        final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    protected <U> U read(Supplier<? extends U> operation) {
        return Utils.get(readLock, operation);
    }

    protected void write(Runnable operation) {
        Utils.run(writeLock, operation);
    }

    protected <U> U write(Supplier<? extends U> operation) {
        return Utils.get(writeLock, operation);
    }

}