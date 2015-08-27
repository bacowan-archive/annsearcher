package moe.cowan.b.annsearcher.frontend.utils.StringSelectors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 19/07/2015.
 */
public class StringSelectorArrayAdapter extends ArrayAdapter {

    private Context context;
    private List<? extends Serializable> objects;

    private SearchItemStringSelector stringSelector;

    public StringSelectorArrayAdapter(SearchItemStringSelector stringSelector, Context context, int resource, List<? extends Serializable> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.stringSelector = stringSelector;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
        textView.setText(stringSelector.getString(objects.get(position)));
        return rowView;

    }

}
