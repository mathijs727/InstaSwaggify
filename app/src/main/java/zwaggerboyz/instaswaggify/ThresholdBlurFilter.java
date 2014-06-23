package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.util.Log;

/**
 * Created by Mathijs on 17/06/14.
 */
public class ThresholdBlurFilter extends AbstractFilterClass {
    ScriptC_threshold_blur mScript;

    public ThresholdBlurFilter() {
        mID = FilterID.THRESHOLD;
        mName = "Threshold Blur";
        mNumValues = 3;

        mLabels = new String[] {
                "Radius",
                "Threshold",
                "Strength",
                "Drop"
        };
        mValues = new int[] {
            4,
            50,
            5,
            5,
        };
    }

    @Override
    public void setValue(int i, int value) {
        super.setValue(i, value);
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_threshold_blur(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) {
        mScript.set_input(allocation);
    }

    @Override
    public void updateInternalValues() {
        mScript.set_imageHeight(imageHeight);
        mScript.set_imageWidth(imageWidth);
        mScript.set_radius((int) normalizeValue(mValues[0], 1.f, 30));
        mScript.set_threshold((int)normalizeValue(mValues[1], 0, 500));
        mScript.set_strength(5f - normalizeValue(mValues[2], 0, 4.9f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_threshold_blur();
    }

    @Override
    public Script.FieldID getFieldId() {
        return mScript.getFieldID_input();
    }
}
