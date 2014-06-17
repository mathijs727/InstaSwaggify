package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.renderscript.ScriptGroup;
import android.util.Log;

public class SaturationFilter extends AbstractFilterClass {
    ScriptC_saturation mScript;

    public SaturationFilter() {
        mName = "Saturation";
        mValues = new int[] {
                50
        };
        mLabels = new String[] {
                "label 1"
        };
        mNumValues = 1;
    }


    @Override
    public void setRS(RenderScript rs) {
        if (mRS == null) {
            mRS = rs;
            if (mRS == null) {
                Log.v("SaturationFilter", "RS is null");
            }
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