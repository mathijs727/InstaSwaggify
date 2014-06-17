package zwaggerboyz.instaswaggify;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script;
import android.support.v8.renderscript.ScriptGroup;

/**
 * Created by Mathijs on 17/06/14.
 */
public interface IFilter {
    public String getName();
    public String getLabel(int i);
    public void setLabel(int i, String label);
    public int getValue(int i);
    public void setValue(int i, int value);
    public void setRS(RenderScript rs);
    public int getNumValues();
    public void addKernel(ScriptGroup.Builder scriptBuilder);
    public void setInput(Allocation input);
    public Script.KernelID getKernelId();
    public Script.FieldID getFieldId();
}
