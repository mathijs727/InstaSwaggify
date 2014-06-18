package zwaggerboyz.instaswaggify;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/**
 * Created by Matthijs on 18-6-2014.
 */
public class RotationFilter extends AbstractFilterClass {
    ScriptC_rotation mScript;

    public RotationFilter() {
        mName = "Rotation";
        mNumValues = 1;

        mLabels = new String[] {
                "Angle"
        };
        mValues = new int[] {
                0
        };
    }

    @Override
    public void setRS(RenderScript rs) {
        if (mRS == null) {
            mRS = rs;
            mScript = new ScriptC_rotation(mRS);
        }
    }

    @Override
    public void setInput(Allocation allocation) {
        mScript.set_input(allocation);
        mScript.invoke_calculateMatrix();
    }

    @Override
    public void updateInternalValues() {
        mScript.set_rotationAngle(normalizeValue(mValues[0], 0.f, 360.f));
    }

    @Override
    public Script.KernelID getKernelId() {
        return mScript.getKernelID_rotation();
    }

    @Override
    public Script.FieldID getFieldId() {
        return mScript.getFieldID_input();
    }

}
