package zwaggerboyz.instaswaggify.filters;


import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Script;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    IFILTER.java
 * This file contains interface for the filter-classes.
 */

public interface IFilter {


    /* Returns the filter name. */
    public String getName();

    /* Returns the filter ID */
    public AbstractFilterClass.FilterID getID();

    /* Returns label i. */
    public String getLabel(int i);

    /* */
    public int getValue(int i);

    /* */
    public void setValue(int i, int value);

    /* */
    public void setRS(RenderScript rs);

    /* */
    public int getNumValues();

    public void updateInternalValues();

    public void setDimensions(int imageHeight, int imageWidth);

    /* */
    public void setInput(Allocation input);
    public Script.KernelID getKernelId();
    public Script.FieldID getFieldId();
}