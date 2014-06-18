package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Matthijs on 18-6-2014.
 */
public class BrightnessFilter extends AbstractFilterClass {
    ScriptC_brightness mScript;

    public BrightnessFilter() {
        mName = "Brightness";
        mNumValues = 1;

        mLabels = new String[] {
                "label 1"
        };
        mValues = new int[] {
                50
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS == null) {
            mRS = rs;
            mScript = new ScriptC_brightness(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_brightnessValue(normalizeValue(mValues[0], 0.f, 1.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_brightness();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
