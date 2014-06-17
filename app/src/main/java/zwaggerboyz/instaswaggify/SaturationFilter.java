package zwaggerboyz.instaswaggify;

import android.renderscript.RenderScript;
import android.renderscript.Script;
import android.renderscript.ScriptGroup;

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

    }

    public void addKernel(ScriptGroup.Builder scriptBuilder) {
        // builder dingen doen yolololo
    }

    public Script.KernelID getKernelId() {
        // rs stuff
        return null;
    }

    public Script.FieldID getFieldId() {
        // nog meer rs dingen
        return null;
    }
}
