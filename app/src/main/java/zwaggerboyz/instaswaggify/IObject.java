package zwaggerboyz.instaswaggify;

import android.media.Image;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by scoud on 21-6-14.
 */
public interface IObject {


    /* Returns the filter name. */
    public String getName();
    public String getDescription();
    public android.graphics.Bitmap getThumbnail();

}

