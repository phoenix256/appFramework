package tr.wolflame.framework.base.interfaces;

import java.util.ArrayList;

/**
 * Created by sadikaltintoprak on 25/11/2016.
 */

public interface ItemValueEventListener<T> {

    void onArrayListFilled(ArrayList<T> arrayList);

    void onObjectFilled(T object);

    void onCancelled();
}
