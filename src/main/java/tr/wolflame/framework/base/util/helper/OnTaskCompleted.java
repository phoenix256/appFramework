package tr.wolflame.framework.base.util.helper;

public interface OnTaskCompleted<T> {
    void onTaskCompleted(String result);

    void onTaskCompleted(T result);

    void onTaskError(String error);
}
