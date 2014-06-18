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
        mName = "Threshold Blur";
        mNumValues = 4;

        mLabels = new String[] {
                "Radius",
                "Threshold",
                "Drop",
                "Strength"
        };
        mValues = new int[] {
            100,
            10,
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
        if (mRS == null) {
            mRS = rs;
            mScript = new ScriptC_threshold_blur(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) {
        Log.i("Pevid", "allocation " + allocation);
        mScript.set_input(allocation);
    }

    @Override
    public void updateInternalValues() {
        mScript.set_imageHeight(imageHeight);
        mScript.set_imageWidth(imageWidth);
/*        mScript.set_radius((long) normalizeValue(mValues[0], 0.f, 100));
        mScript.set_threshold((int)normalizeValue(mValues[1], 0, 100));
        mScript.set_drop((int)normalizeValue(mValues[2], 0, 100));
        mScript.set_strength((short)normalizeValue(mValues[3], 0, 5));*/
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
