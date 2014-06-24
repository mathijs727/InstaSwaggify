package zwaggerboyz.instaswaggify.filters;


import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Mathijs on 16/06/14.
 */

public abstract class AbstractFilterClass implements IFilter {

    public enum FilterID {
        BRIGHTNESS(0), CONTRAST(1), GAUSSIAN(2), ROTATION(3), SATURATION(4), SEPIA(5), NOISE(6), INVERT(7), COLORIZE(8), THRESHOLD(9);

        /* Every enum has an integer value that can be retrieved (for indexing etc). */
        private final int value;
        private FilterID(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    protected FilterID mID;
    protected String mName;
    protected int mValues[];
    protected String mLabels[];
    protected int mNumValues;
    protected int imageHeight;
    protected int imageWidth;

    protected RenderScript mRS;

    public String getName() {
        return mName;
    }

    public FilterID getID() {
        return  mID;
    }

    public String getLabel(int i) {
        if (i < mNumValues) {
            return mLabels[i];
        } else {
            return "";
        }
    }

    public int getValue(int i) {
        if (i < mNumValues) {
            return mValues[i];
        } else {
            return 0;
        }
    }

    public void setValue(int i, int value) {
        if (i < mNumValues) {
            mValues[i] = value;
        }
    }

    public void setDimensions(int imageHeight, int imageWidth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    public float normalizeValue(int value, float min, float max) {
        return (float) ((max - min) * (value / 100.0) + min);
    }

    public int getNumValues() {
        return mNumValues;
    }

    public abstract void setRS(RenderScript rs);
    public abstract void setInput(Allocation allocation);
    public abstract void updateInternalValues();
    public abstract Script.KernelID getKernelId();
    public abstract Script.FieldID getFieldId();
}
