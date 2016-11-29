package tr.wolflame.framework.base.util.helper;

import java.util.ArrayList;

import tr.wolflame.framework.base.adapters.BaseItemAdapter;
import tr.wolflame.framework.base.objects.BaseItem;

public class SearchRunnable implements Runnable {

    private String query;
    private ArrayList<BaseItem> entryList;
    private BaseItemAdapter rowsAdapter;

    public SearchRunnable(BaseItemAdapter rowsAdapter) {
        this.query = "";
        this.rowsAdapter = rowsAdapter;
    }

    public SearchRunnable(BaseItemAdapter rowsAdapter, String query) {
        this.query = query;
        this.rowsAdapter = rowsAdapter;
    }

    public void setSearchQuery(String query) {
        this.query = query;
    }

    public void setSearchAdapter(BaseItemAdapter rowsAdapter) {
        this.rowsAdapter = rowsAdapter;
    }

    @Override
    public void run() {

        search();
    }


    private void search() {
        // offload processing from the UI thread
       /* new AsyncTask<String, Void, ArrayList<BaseItem>>() {
            private final String initialQuery = query;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected ArrayList<BaseItem> doInBackground(String... params) {


                return new ListRow(header, listRowAdapter);
            }

            @Override
            protected void onPostExecute(ArrayList<BaseItem> resultList) {
                rowsAdapter.add(listRow);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        rowsAdapter.getFilter().filter(query);
    }

}