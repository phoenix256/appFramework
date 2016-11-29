package tr.wolflame.framework.base.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tr.wolflame.framework.R;

public class ExampleSearchAdapter extends CursorAdapter {

    private ArrayList<String> items;

    private TextView text;

    public ExampleSearchAdapter(Context context, Cursor cursor, ArrayList<String> items) {

        super(context, cursor, false);

        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        text.setText(items.get(cursor.getPosition()));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_search_item, parent, false);

        text = (TextView) view.findViewById(R.id.text);

        return view;

    }

}