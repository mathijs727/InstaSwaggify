package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;

/**
 * Created by scoud on 21-6-14.
 */
public class AbstractObjectClass implements IObject {

    protected String mName;
    protected String mDescription;
    protected Bitmap mThumbnail;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Bitmap getThumbnail() {
        return mThumbnail;
    }
}
