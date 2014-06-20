package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Matthijs on 20-6-2014.
 */
public class NoiseFilter extends AbstractFilterClass {
    ScriptC_noise mScript;

    public NoiseFilter() {
        mName = "Noise";
        mNumValues = 1;

        mLabels = new String[] {
                "noise"
        };
        mValues = new int[] {
                10
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
        mScript.set_noiseValue(normalizeValue(mValues[0], 0.02f, 1.f));
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
