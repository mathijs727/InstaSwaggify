package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_noise;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    NoiseFilter.java
 * This file contains the noise-filter. It links to the required RenderScript-object and
 * stores the values of the slider.
 */

public class NoiseFilter extends AbstractFilterClass {
    ScriptC_noise mScript;

    public NoiseFilter() {
        mName = "Noise";
        mNumValues = 1;
        mID = FilterID.NOISE;

        mLabels = new String[] {
                "noise"
        };
        mValues = new int[] {
                20
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_noise(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_noiseValue(normalizeValue(mValues[0], 0.f, 0.25f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_noise();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
