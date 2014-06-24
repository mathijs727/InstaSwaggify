package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_threshold_blur;
/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    ThresholdBlurFilter.java
 * This file contains the threshold-filter. It links to the required RenderScript-object and
 * stores the values of the sliders.
 */

public class ThresholdBlurFilter extends AbstractFilterClass {
    ScriptC_threshold_blur mScript;

    public ThresholdBlurFilter() {
        mID = FilterID.THRESHOLD;
        mName = "Threshold Blur";
        mNumValues = 3;

        mLabels = new String[] {
                "radius",
                "threshold",
                "strength",
                "drop"
        };
        mValues = new int[] {
            4,
            50,
            5,
            5,
        };
    }

    @Override
    public void setValue(int i, int value) {
        super.setValue(i, value);
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_threshold_blur(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) {
        mScript.set_input(allocation);
    }

    @Override
    public void updateInternalValues() {
        mScript.set_imageHeight(imageHeight);
        mScript.set_imageWidth(imageWidth);
        mScript.set_radius((int) normalizeValue(mValues[0], 1.f, 30));
        mScript.set_threshold((int)normalizeValue(mValues[1], 0, 500));
        mScript.set_strength(5f - normalizeValue(mValues[2], 0, 4.9f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_threshold_blur();
    }

    @Override
    public Script.FieldID getFieldId() {
        return mScript.getFieldID_input();
    }
}
