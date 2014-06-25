package zwaggerboyz.instaswaggify.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE: FilterDialogListAdapter.java
 * This file manages the filter list and removes added filter from the list,
 *      so that filters cannot be added multiple times
 */

public class FilterDialogListAdapter extends ArrayAdapter<CharSequence> {
    private Boolean[] mEnabled;

    public FilterDialogListAdapter(
            Context context, int textViewResId, CharSequence[] strings) {
        super(context, textViewResId, strings);
        mEnabled = new Boolean[strings.length];
        for (int i = 0; i < strings.length; i++)
            mEnabled[i] = true;
    }

    public static FilterDialogListAdapter createFromResource(
        Context context, int textArrayResId, int textViewResId) {
        Resources resources = context.getResources();
        CharSequence[] strings = resources.getTextArray(textArrayResId);

        return new FilterDialogListAdapter(context, textViewResId, strings);
    }
}