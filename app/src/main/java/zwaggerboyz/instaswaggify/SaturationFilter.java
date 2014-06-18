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

        mLabels = new String[] {
                "amount"
        };
        mValues = new int[] {
            50
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
    public void updateInternalValues() {
        mScript.set_saturationValue(normalizeValue(mValues[0], 0.f, 1.f));
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
