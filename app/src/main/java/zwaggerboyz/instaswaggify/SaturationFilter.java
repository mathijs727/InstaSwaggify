package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.util.Log;

/**
 * Created by Mathijs on 17/06/14.
 */
public class SaturationFilter extends AbstractFilterClass {
    ScriptC_saturation mScript;

    public SaturationFilter() {
        mName = "Saturation";
        mNumValues = 1;

        mValues = new int[] {
            50
        };
        mMinValues = new float[] {
            0.f
        };
        mMaxValues = new float[] {
            2.f
        };
        mNormalizedValues = new float[] {
            1.f
        };
        mLabels = new String[] {
            "label 1"
        };

    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS == null) {
            mRS = rs;
            mScript = new ScriptC_saturation(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_saturation();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
