package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by scoud on 21-6-14.
 */
public class PlaceholderObject extends AbstractObjectClass {
    public PlaceholderObject(Context context) {
        mName = "Placeholder";
        mDescription = "This object is a placeholder.";
        mThumbnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.blazeit);
    }
}
