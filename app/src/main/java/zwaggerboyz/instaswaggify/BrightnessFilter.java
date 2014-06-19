package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import java.util.logging.Filter;

/**
 * Created by Matthijs on 18-6-2014.
 */
public class BrightnessFilter extends AbstractFilterClass {
    ScriptC_brightness mScript;

    public BrightnessFilter() {
        mID = FilterID.BRIGHTNESS;
        mName = "Brightness";
        mNumValues = 1;

        mLabels = new String[] {
                "brightness"
        };
        mValues = new int[] {
                100
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_brightness(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_brightnessValue(normalizeValue(mValues[0], 0.25f, 1.f));
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
