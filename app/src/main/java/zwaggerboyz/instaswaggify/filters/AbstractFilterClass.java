package zwaggerboyz.instaswaggify.filters;


import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.util.Log;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    AbstractFilterClass.java
 * This file contains the abstract class used to implement the different filters. It contains a
 * number of variables with getter- and setter-functions.
 */

public abstract class AbstractFilterClass implements IFilter, Cloneable {

    public enum FilterID {
        BRIGHTNESS,
        CONTRAST,
        GAUSSIAN,
        ROTATION,
        SATURATION,
        SEPIA,
        NOISE,
        INVERT,
        COLORIZE,
        THRESHOLD
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

    /* set input value to the according value between the min and max value */
    public float normalizeValue(int value, float min, float max) {
        return (float) ((max - min) * (value / 100.0) + min);
    }

    public void setArray(int[] array) {
        System.arraycopy(mValues, 0, array, 0, mNumValues);
        mValues = array;
    }

    public IFilter clone() {
        try {
            IFilter clone = (IFilter) super.clone();
            clone.setArray(new int[mNumValues]);
            return clone;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
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
