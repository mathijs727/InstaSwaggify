package zwaggerboyz.instaswaggify.filters;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_sepia;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    SepiaFilter.java
 * This file contains the sepia-filter. It links to the required RenderScript-object and
 * stores the values of the sliders.
 */

public class SepiaFilter extends AbstractFilterClass {
    ScriptC_sepia mScript;

    public SepiaFilter() {
        mID = FilterID.SEPIA;
        mName = "Sepia";
        mNumValues = 2;

        /* slider labels */
        mLabels = new String[] {
                "intensity",
                "depth"
        };

        /* slider default values */
        mValues = new int[] {
                10,
                20
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_sepia(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_intensity(normalizeValue(mValues[0], 0.05f, 0.4f));
        mScript.set_depth(normalizeValue(mValues[1], 0.f, 0.5f));
        mScript.invoke_calculateVector();
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_sepia();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}