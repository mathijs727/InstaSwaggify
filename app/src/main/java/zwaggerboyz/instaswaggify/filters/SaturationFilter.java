package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_saturation;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    SaturationFilter.java
 * This file contains the saturation-filter. It links to the required RenderScript-object and
 * stores the values of the slider.
 */

public class SaturationFilter extends AbstractFilterClass {
    ScriptC_saturation mScript;

    public SaturationFilter() {
        mID = FilterID.SATURATION;
        mName = "Saturation";
        mNumValues = 1;


        mLabels = new String[] {
                "intensity"
        };
        mValues = new int[] {
            50
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_saturation(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_saturationValue(normalizeValue(mValues[0], 0.f, 2.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_saturation();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }

}
