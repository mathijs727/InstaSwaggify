package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

import java.util.logging.Filter;

/**
 * Created by Matthijs on 18-6-2014.
 */
public class ColorizeFilter extends AbstractFilterClass {
    ScriptC_colorize mScript;

    public ColorizeFilter() {
        mID = FilterID.COLORIZE;
        mName = "Colorize";
        mNumValues = 3;

        mLabels = new String[] {
                "red",
                "green",
                "blue"
        };
        mValues = new int[] {
                20,
                20,
                20
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS != rs) {
            mRS = rs;
            mScript = new ScriptC_colorize(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) { }

    @Override
    public void updateInternalValues() {
        mScript.set_redValue(normalizeValue(mValues[0], 0.f, 1.f));
        mScript.set_greenValue(normalizeValue(mValues[1], 0.f, 1.f));
        mScript.set_blueValue(normalizeValue(mValues[2], 0.f, 1.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_colorize();
    }

    @Override
    public Script.FieldID getFieldId() {
        return null;
    }
}
