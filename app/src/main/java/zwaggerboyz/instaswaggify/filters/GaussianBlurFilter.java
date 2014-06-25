package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.renderscript.ScriptIntrinsicBlur;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    GuassianBlurFilter.java
 * This file contains the gaussian-blur-filter. It links to the required RenderScript-object and
 * stores the values of the slider.
 */

public class GaussianBlurFilter extends AbstractFilterClass {
    ScriptIntrinsicBlur mScript;

    public GaussianBlurFilter() {
        mID = FilterID.GAUSSIAN;
        mName = "Gaussian Blur";
        mNumValues = 1;

        /* slider label */
        mLabels = new String[] {
                "radius"
        };

        /* slider default value */
        mValues = new int[] {
                50
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
        }
    }

    @Override
    public void setInput(Allocation allocation) {
        mScript.setInput(allocation);
    }

    @Override
    public void updateInternalValues() {
        mScript.setRadius(normalizeValue(mValues[0], 1.f, 20.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID();
    }

    @Override
    public Script.FieldID getFieldId() {
        return mScript.getFieldID_Input();
    }
}
