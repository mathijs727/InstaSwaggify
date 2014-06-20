package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Mathijs on 19/06/14.
 */
public class GaussianBlurFilter extends AbstractFilterClass {
    ScriptIntrinsicBlur mScript;

    public GaussianBlurFilter() {
        mID = FilterID.GAUSSIAN;
        mName = "Guassian Blur";
        mNumValues = 1;

        mLabels = new String[] {
                "radius"
        };
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
        mScript.setRadius(normalizeValue(mValues[0], 5.f, 20.f));
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
