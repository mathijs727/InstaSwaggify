package zwaggerboyz.instaswaggify.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import zwaggerboyz.instaswaggify.R;

/**
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE: OverlayDialog.java
 */

public class OverlayDialog extends DialogFragment {
    private static final String[] mAllOverlays =
            {
                    "InstaSwag",
                    "InstaWeed",
                    "InstaJoint",
                    "InstaSnoop",

                    "InstaBling",
                    "InstaBling2",
                    "InstaMoney",
                    "InstaCrown",
                    "InstaNova",

                    "InstaDoge",
                    "InstaDealWithIt",
                    "InstaFedora",
                    "InstaCap",

                    "InstaAchievement",
                    "InstaNoScope",
                    "InstaHitmarker",
                    "InstaPlus100",
                    "InstaDoritos",
                    "InstaDew",
                    "InstaMLG",

                    "InstaBeard",
                    "InstaBeard2",
                    "InstaMoustache",
                    "InstaGourmet",
            };
    private OnAddOverlayListener mListener = null;
    ArrayAdapter<String> mAdapter;

    public OverlayDialog(Context context) {
        mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mAllOverlays);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        getDialog().setTitle(R.string.overlay_dialog_title);

        /* Setting up ListView and the adapter */
        View mView = inflater.inflate(R.layout.dialog_list, container, false);
        ListView listView = (ListView) mView.findViewById(R.id.dialog_list_listview);

        listView.setAdapter(mAdapter);

        /* List item listeners */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int listId, long l) {
                if (mListener != null) {
                    mListener.OnAddOverlayListener(mAllOverlays[listId]);
                }
            }
        });
        return mView;
    }

    public void setOnAddOverlayListener(OnAddOverlayListener listener) {
        mListener = listener;
    }
    public interface OnAddOverlayListener {
        public void OnAddOverlayListener(String resourceName);
    }
}
