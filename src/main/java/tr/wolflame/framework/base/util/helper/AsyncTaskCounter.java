package tr.wolflame.framework.base.util.helper;

/**
 * Created by SADIK on 25/05/16.
 */
public class AsyncTaskCounter {

    private static final String TAG = "AsyncTaskCounter";

    private int counter = 0;
    private int maxSize = 0;

    public AsyncTaskCounter() {
        this(0);
    }

    public AsyncTaskCounter(int maxSize) {
        this.counter = 0;
        this.maxSize = maxSize;
    }

    public synchronized void increase() {
        this.counter++;
    }

    public synchronized void decrease() {
        this.counter--;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized int getMaxSize() {
        return maxSize;
    }

    public synchronized void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized boolean isFinished() {
        return maxSize != 0 ? counter >= maxSize : counter <= 0;
    }
}
