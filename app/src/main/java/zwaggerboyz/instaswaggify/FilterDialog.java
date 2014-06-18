package zwaggerboyz.instaswaggify;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by scoud on 18-6-14.
 */
public class FilterDialog extends DialogFragment {
    private String[] filters = {"Brightness", "Contrast", "Rotation", "Saturation", "Sepia"};

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filterdialog, container, false);
        ListView listView = (ListView) v.findViewById(R.id.filter_dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, filters);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).addFilter(i);
            }
        });

        return v;
    }
}
