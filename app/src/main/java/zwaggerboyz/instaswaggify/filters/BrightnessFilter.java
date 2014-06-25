package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.util.Log;

import zwaggerboyz.instaswaggify.ScriptC_brightness;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    BrightnessFilter.java
 * This file contains the brightness-filter. It links to the required RenderScript-object and stores
 * the values of the slider.
 */

public class BrightnessFilter extends AbstractFilterClass {
    ScriptC_brightness mScript;

    public BrightnessFilter() {
        mID = FilterID.BRIGHTNESS;
        mName = "Brightness";
        mNumValues = 1;

        /* slider label */
        mLabels = new String[] {
                "intensity"
        };

        /* slider default value */
        mValues = new int[] {
                33
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_brightness(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_brightnessValue(normalizeValue(mValues[0], 0.5f, 2.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_brightness();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
