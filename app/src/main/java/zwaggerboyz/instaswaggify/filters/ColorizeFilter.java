package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_colorize;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    ColorizeFilter.java
 * This file contains the colorize-filter. It links to the required RenderScript-object and stores
 * the values of the sliders.
 */

public class ColorizeFilter extends AbstractFilterClass {
    ScriptC_colorize mScript;

    public ColorizeFilter() {
        mID = FilterID.COLORIZE;
        mName = "Colorize";
        mNumValues = 3;

        /* slider labels */
        mLabels = new String[] {
                "red",
                "green",
                "blue"
        };

        /* slider default values */
        mValues = new int[] {
                0,
                0,
                0
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_colorize(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_redValue(normalizeValue(mValues[0], 0.f, 1.f));
        mScript.set_greenValue(normalizeValue(mValues[1], 0.f, 1.f));
        mScript.set_blueValue(normalizeValue(mValues[2], 0.f, 1.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_colorize();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}