package zwaggerboyz.instaswaggify.filters;


import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Script;

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

    public String getName();

    public AbstractFilterClass.FilterID getID();

    public String getLabel(int i);

    public int getValue(int i);

    public void setValue(int i, int value);

    public void setRS(RenderScript rs);

    public int getNumValues();

    public void updateInternalValues();

    public void setDimensions(int imageHeight, int imageWidth);

    public void setInput(Allocation input);

    public Script.KernelID getKernelId();

    public Script.FieldID getFieldId();

    public IFilter clone();

    public void setArray(int[] array);
}