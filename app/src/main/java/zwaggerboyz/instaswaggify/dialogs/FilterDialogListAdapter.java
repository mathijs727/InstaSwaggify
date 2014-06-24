package zwaggerboyz.instaswaggify.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

/**
 * Created by Mathijs on 23/06/14.
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