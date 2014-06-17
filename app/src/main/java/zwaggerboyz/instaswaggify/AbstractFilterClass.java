package zwaggerboyz.instaswaggify;


import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Mathijs on 16/06/14.
 */

public abstract class AbstractFilterClass implements IFilter {
    protected String mName;
    protected int mValues[];
    protected String mLabels[];
    protected int mNumValues;

    protected RenderScript mRS;

    public String getName() {
        return mName;
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
