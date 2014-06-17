package zwaggerboyz.instaswaggify;


import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script;
import android.support.v8.renderscript.ScriptGroup;

/**
 * Created by Mathijs on 17/06/14.
 */
public interface IFilter {
    public String getName();
    public String getLabel(int i);
    public void setLabel(int i, String newLabel);
    public int getValue(int i);
    public void setValue(int i, int newValue);
    public void setRS(RenderScript rs);
    public int getNumValues();
    public void addKernel(ScriptGroup.Builder scriptBuilder);
    public Script.KernelID getKernelId();
    public Script.FieldID getFieldId();
}
