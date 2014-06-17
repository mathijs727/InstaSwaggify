package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptGroup;

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

    private Canvas mCanvasView;
    private RenderScript mRS;
    private ScriptGroup mScriptGroup;
    private AsyncTask mRenderTask;

    public void createRS(Context context) {
        mRS = RenderScript.create(context);
    }

    public void setBitmap(Bitmap origBitmap) {
        mBitmapIn = origBitmap;

        //Allocate buffers
        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn);
        mOutAllocations = new Allocation[NUM_BITMAPS];
        for (int i = 0; i < NUM_BITMAPS; ++i) {
            mOutAllocations[i] = Allocation.createFromBitmap(mRS, mBitmapsOut[i]);
        }
    }

    public void setCanvasView(Canvas canvasView) {
        mCanvasView = canvasView;
    }

    public void generateBitmap(List<IFilter> filters) {
        ScriptGroup.Builder builder = new ScriptGroup.Builder(mRS);
        if (filters.size() > 0) {
            for (IFilter filter : filters) {
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
        }
        mScriptGroup = builder.create();
        if (mRenderTask != null)
            mRenderTask.cancel(false);

        mRenderTask = new RenderScriptTask();
        mRenderTask.execute(filters);
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
                    filters[0].setInput(mInAllocation);
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
                //mCanvasView.setImage(mBitmapsOut[index]);
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
