package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Matthijs on 18-6-2014.
 */
public class ContrastFilter extends AbstractFilterClass {
    ScriptC_contrast mScript;

    public ContrastFilter() {
        mID = FilterID.CONTRAST;
        mName = "Contrast";
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
            mScript = new ScriptC_contrast(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_contrastValue(normalizeValue(mValues[0], 0.f, 1.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_contrast();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
