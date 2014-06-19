package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Mathijs on 17/06/14.
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
    private ScriptGroup mScriptGroup;
    private RenderScriptTask mRenderTask;

    private static final int BITMAP_MAX_WIDTH = 1000;
    private static final int BITMAP_MAX_HEIGHT = 1000;

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

    public void setBitmap(Bitmap origBitmap) {
        mBitmapIn = resizeBitmap(origBitmap);
        mBitmapsOut = new Bitmap[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mBitmapsOut[i] = Bitmap.createBitmap(mBitmapIn.getWidth(),
                    mBitmapIn.getHeight(), mBitmapIn.getConfig());
        }

        //Allocate buffers
        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn);
        mOutAllocations = new Allocation[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mOutAllocations[i] = Allocation.createFromBitmap(mRS, mBitmapsOut[i]);
        }
    }

    public void setCanvasView(CanvasView canvasView) {
        mCanvasView = canvasView;
    }

    public void generateBitmap(List<IFilter> filters) {
        if (mBitmapIn == null)
            return;

        if (filters.size() == 0) {
            mCanvasView.setBitmap(mBitmapIn);
            mCanvasView.invalidate();
            return;
        }

        ScriptGroup.Builder builder = new ScriptGroup.Builder(mRS);
        for (IFilter filter : filters) {
            filter.setRS(mRS);
            filter.setDimensions(mBitmapIn.getHeight(), mBitmapIn.getWidth());
            filter.updateInternalValues();
            builder.addKernel(filter.getKernelId());
        }

        for (int i = 0; i < filters.size()-1; i++) {
            if (filters.get(i+1).getFieldId() != null) {
                builder.addConnection(
                        mInAllocation.getType(),
                        filters.get(i).getKernelId(),
                        filters.get(i + 1).getFieldId());
            } else {
                builder.addConnection(
                        mInAllocation.getType(),
                        filters.get(i).getKernelId(),
                        filters.get(i + 1).getKernelId());
            }
        }
        mScriptGroup = builder.create();
        if (mRenderTask != null)
            mRenderTask.cancel(false);

        IFilter[] filtersArray = new IFilter[filters.size()];
        filters.toArray(filtersArray);
        mRenderTask = new RenderScriptTask();
        mRenderTask.execute(filtersArray);
    }

    /*
     * In the AsyncTask, it invokes RenderScript intrinsics to do a filtering.
     * After the filtering is done, an operation blocks at Allication.copyTo() in AsyncTask thread.
     * Once all operation is finished at onPostExecute() in UI thread, it can invalidate and update ImageView UI.
     */
    private class RenderScriptTask extends AsyncTask<IFilter, Integer, Integer> {
        Boolean issued = false;

        protected Integer doInBackground(IFilter... filters) {
            int index = -1;
            if (isCancelled() == false) {
                issued = true;
                index = mCurrentBitmap;

                if (filters[0].getFieldId() != null) {
                    filters[0].setInput(mInAllocation);;
                } else {
                    mScriptGroup.setInput(filters[0].getKernelId(),
                            mInAllocation);
                }

                mScriptGroup.setOutput(filters[filters.length-1].getKernelId(),
                        mOutAllocations[index]);
                mScriptGroup.execute();

                mOutAllocations[index].copyTo(mBitmapsOut[index]);
                mCurrentBitmap = (mCurrentBitmap + 1) % NUM_BITMAPS;
            }
            return index;
        }

        void updateView(Integer result) {
            if (result != -1) {
                // Request UI update
                mCanvasView.setBitmap(mBitmapsOut[result]);
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
