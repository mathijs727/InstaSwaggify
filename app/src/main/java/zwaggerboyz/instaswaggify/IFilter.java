package zwaggerboyz.instaswaggify;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script;
import android.support.v8.renderscript.ScriptGroup;

/*
 * IFilter.java
 *
 * This file contains the interface for the different filter-classes.
 */
public interface IFilter {
    /* Returns the name of the filter. */
    public String getName();

    /* Returns the i-th label of the filter. */
    public String getLabel(int i);

    /* Sets the i-th label of the filter. */
    public void setLabel(int i, String label);

    /* Returns the value of the i-th slider of the filter. */
    public int getValue(int i);

    /* Sets the value of the i-th slider of the filter. */
    public void setValue(int i, int value);

    /* Sets the render-script of the filter. */
    public void setRS(RenderScript rs);

    /* TODO: wtf does this function do */
    public int getNumValues();

    /* TODO: wtf does this function do */
    public void addKernel(ScriptGroup.Builder scriptBuilder);

    /* TODO: wtf does this function do */
    public void setInput(Allocation input);

    /* TODO: wtf does this function do */
    public Script.KernelID getKernelId();

    /* TODO: wtf does this function do */
    public Script.FieldID getFieldId();
}
