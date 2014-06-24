package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_contrast;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    ContrastFilter.java
 * This file contains the contrast-filter. It links to the required RenderScript-object and stores
 * the values of the slider.
 */

public class ContrastFilter extends AbstractFilterClass {
    ScriptC_contrast mScript;

    public ContrastFilter() {
        mID = FilterID.CONTRAST;
        mName = "Contrast";
        mNumValues = 1;

        mLabels = new String[] {
                "intensity"
        };
        mValues = new int[] {
                11
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_contrast(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_contrastValue(normalizeValue(mValues[0], 0.75f, 3.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_contrast();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
