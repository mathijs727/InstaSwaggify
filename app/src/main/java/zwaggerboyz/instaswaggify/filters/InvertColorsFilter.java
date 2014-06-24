package zwaggerboyz.instaswaggify.filters;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import zwaggerboyz.instaswaggify.ScriptC_invert_colors;

/**
 * Created by Matthijs on 20-6-2014.
 */
public class InvertColorsFilter extends AbstractFilterClass {
    ScriptC_invert_colors mScript;

    public InvertColorsFilter() {
        mID = FilterID.INVERT;
        mName = "Invert Colors";
        mNumValues = 0;

        mLabels = new String[] {
        };
        mValues = new int[] {
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_invert_colors(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_invert();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
