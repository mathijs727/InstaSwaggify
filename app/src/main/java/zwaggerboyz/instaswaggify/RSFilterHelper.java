package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptGroup;
import android.util.Log;

import java.util.List;

import zwaggerboyz.instaswaggify.filters.IFilter;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    RSFilterHelper.java
 * This file contains a helper-class to apply the selected filter on the image.
 */

public class RSFilterHelper {
    private final int NUM_BITMAPS = 3;
    private int mCurrentBitmap = 0;
    private Bitmap mBitmapIn;
    private Bitmap[] mBitmapsOut;
    private Allocation mInAllocation;
    private Allocation[] mOutAllocations;
    private CanvasView mCanvasView;
    private RenderScript mRS;
    private RenderScriptTask mRenderTask;
    private ScriptGroup mScriptGroup;

    private static final int BITMAP_MAX_WIDTH = 600;
    private static final int BITMAP_MAX_HEIGHT = 600;

    public void createRS(Context context) {
        mRS = RenderScript.create(context);
    }

    private Bitmap resizeBitmap(Bitmap in) {
        int width = in.getWidth();
        int height = in.getHeight();

        if (width < BITMAP_MAX_WIDTH &&
            height < BITMAP_MAX_HEIGHT)
            return in;

        double xScale = (double)BITMAP_MAX_WIDTH / width;
        double yScale = (double)BITMAP_MAX_HEIGHT / height;

        if (xScale > yScale) {
            return Bitmap.createScaledBitmap(in, (int)(width * xScale), (int)(height * xScale), false);
        } else {
            return Bitmap.createScaledBitmap(in, (int)(width * yScale), (int)(height * yScale), false);
        }
    }

    public void setBitmap(Bitmap origBitmap, boolean resize) {
        if (resize)
            mBitmapIn = resizeBitmap(origBitmap);
        else
            mBitmapIn = origBitmap;

        mBitmapsOut = new Bitmap[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mBitmapsOut[i] = Bitmap.createBitmap(mBitmapIn.getWidth(),
                    mBitmapIn.getHeight(), mBitmapIn.getConfig());
        }

        /* Allocate buffers */
        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn);
        mOutAllocations = new Allocation[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mOutAllocations[i] = Allocation.createFromBitmap(mRS, mBitmapsOut[i]);
        }
    }

    public void setCanvasView(CanvasView canvasView) {
        mCanvasView = canvasView;
    }

    //public void generateBitmap(List<IFilter> filters, Context context) {
    //    generateBitmap(filters, context, true, false);
    //}

    public void generateBitmap(List<IFilter> filters, boolean listChanged, boolean forceUpdate) {
        if (mBitmapIn == null)
            return;

        if (filters.size() == 0) {
            mCanvasView.setBitmap(mBitmapIn, false);
            mCanvasView.invalidate();
            return;
        }

        if (mRenderTask != null)
            if (forceUpdate) {
                mRenderTask.cancel(true);
            } else {
                if (mRenderTask.getStatus() == AsyncTask.Status.RUNNING)
                    return;
                else if (mRenderTask.getStatus() == AsyncTask.Status.PENDING)
                    mRenderTask.cancel(false);
            }

        IFilter[] filtersArray = new IFilter[filters.size()];
        filters.toArray(filtersArray);
        mRenderTask = new RenderScriptTask();
        mRenderTask.setListChanged(listChanged);
        mRenderTask.execute(filtersArray);
    }

    /*
     * In the AsyncTask, it invokes RenderScript intrinsics to do a filtering.
     * After the filtering is done, an operation blocks at Allication.copyTo() in AsyncTask thread.
     * Once all operation is finished at onPostExecute() in UI thread, it can invalidate and update ImageView UI.
     */
    private class RenderScriptTask extends AsyncTask<IFilter, Integer, Integer> {
        private boolean issued = false;
        private boolean mListChanged;

        public void setListChanged(boolean listChanged) {
            mListChanged = listChanged;
        }

        protected Integer doInBackground(IFilter... filters) {
            int index = -1;
            if (isCancelled() == false) {
                issued = true;
                index = mCurrentBitmap;

                if (mListChanged || mScriptGroup == null) {
                    int length = filters.length;

                    ScriptGroup.Builder builder = new ScriptGroup.Builder(mRS);
                    for (IFilter filter : filters) {
                        filter.setRS(mRS);
                        filter.setDimensions(mBitmapIn.getHeight(), mBitmapIn.getWidth());
                        filter.updateInternalValues();
                        builder.addKernel(filter.getKernelId());
                    }

                    for (int i = 0; i < length - 1; i++) {
                        if (filters[i + 1].getFieldId() != null) {
                            builder.addConnection(
                                    mInAllocation.getType(),
                                    filters[i].getKernelId(),
                                    filters[i + 1].getFieldId());
                        } else {
                            builder.addConnection(
                                    mInAllocation.getType(),
                                    filters[i].getKernelId(),
                                    filters[i + 1].getKernelId());
                        }
                    }
                    mScriptGroup = builder.create();

                    if (filters[0].getFieldId() != null) {
                        filters[0].setInput(mInAllocation);
                    } else {
                        mScriptGroup.setInput(filters[0].getKernelId(),
                                mInAllocation);
                    }
                } else {
                    for (IFilter filter : filters) {
                        filter.setRS(mRS);
                        filter.setDimensions(mBitmapIn.getHeight(), mBitmapIn.getWidth());
                        filter.updateInternalValues();
                    }
                }

                mScriptGroup.setOutput(filters[filters.length - 1].getKernelId(),
                        mOutAllocations[index]);
                mScriptGroup.execute();

                mOutAllocations[index].copyTo(mBitmapsOut[index]);
                mCurrentBitmap = (mCurrentBitmap + 1) % NUM_BITMAPS;
            }
            return index;
        }

        void updateView(Integer result) {
            if (result != -1) {
                /* Request UI update */
                mCanvasView.setBitmap(mBitmapsOut[result], false);
                mCanvasView.invalidate();
            }
        }

        protected void onPostExecute(Integer result) {
            updateView(result);
        }

        protected void onCancelled(Integer result) {
            if (issued) {
                updateView(result);
            }
        }
    }
}