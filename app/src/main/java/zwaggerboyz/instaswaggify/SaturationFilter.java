package zwaggerboyz.instaswaggify;

import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.renderscript.ScriptGroup;
import android.support.v8.renderscript.Allocation;

/**
 * Created by Mathijs on 16/06/14.
 */

public class SaturationFilter implements IFilter {
    private String title = "Saturation", label1 = "value";
    private int slider1 = 50;
    private int nSilders = 1;
    private RenderScript script;

    public String getName() {
        return title;
    }

    public String getLabel(int i) {
        switch (i) {
            case 1: return label1;
            default: return "";
        }
    }

    public void setLabel(int i, String newLabel) {
        switch (i) {
            case 1:
                label1 = newLabel;
                return;
            default: return;
        }
    }

    public int getValue(int i) {
        switch (i) {
            case 1: return slider1;
            default: return 0;
        }
    }

    public void setValue(int i, int newValue) {
        switch (i) {
            case 1:
                slider1 = newValue;
                return;
            default: return;
        }
    }

    public void setRS(RenderScript rs) {
        script = rs;
        return;
    }

    public int getNumValues() {
        /* TODO: this is supposed to do at least something */
        return 0;
    }

    @Override
    public void setInput(Allocation input) {
        /* TODO: do we need this function? */
    }

    public void addKernel(ScriptGroup.Builder scriptBuilder) {
        /* TODO: this is supposed to do at least something */
    }

    public android.support.v8.renderscript.Script.KernelID getKernelId() {
        /* TODO: this is supposed to do at least something */
        return null;
    }

    public android.support.v8.renderscript.Script.FieldID getFieldId() {
        /* TODO: this is supposed to do at least something */
        return null;
    }
}
