package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Matthijs on 17-6-2014.
 */
public class SepiaFilter extends AbstractFilterClass {
    ScriptC_sepia mScript;

    public SepiaFilter() {
        mName = "Sepia";
        mNumValues = 2;

        mLabels = new String[] {
                "Intensity",
                "Depth"
        };
        mValues = new int[] {
                90,
                60
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS == null) {
            mRS = rs;
            mScript = new ScriptC_sepia(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_intensity(normalizeValue(mValues[0], 0.05f, 0.4f));
        mScript.set_depth(normalizeValue(mValues[1], 0.f, 0.5f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_sepia();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
