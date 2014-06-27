package zwaggerboyz.instaswaggify.dialogs;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import zwaggerboyz.instaswaggify.CanvasDraggableItem;
import zwaggerboyz.instaswaggify.R;

/**
 * Created by Tristan on 27-6-2014.
 */
public class OverlaySettingsDialog extends DialogFragment {

    CanvasDraggableItem item;
    onSettingsChangedListener mListener;

    public OverlaySettingsDialog(CanvasDraggableItem item, onSettingsChangedListener listener) {
        this.item = item;
        this.mListener = listener;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        getDialog().setTitle(R.string.overlay_dialog_settings_title);

        /* Setting up ListView and the adapter */
        View mView = inflater.inflate(R.layout.dialog_overlay_settings, container, false);

        Switch flip = (Switch) mView.findViewById(R.id.dialog_overlay_settings_flip);
        flip.setChecked(item.getFlippedState());
        Switch lock = (Switch) mView.findViewById(R.id.dialog_overlay_settings_lock);
        lock.setChecked(item.getLockedState());
        Button reset = (Button) mView.findViewById(R.id.dialog_overlay_settings_reset);

        flip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.flip(b);

                mListener.onSettingsChanged();
            }
        });

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.lock(b);

                mListener.onSettingsChanged();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.reset();

                mListener.onSettingsChanged();
            }
        });

        return mView;
    }

    public interface onSettingsChangedListener {
        public void onSettingsChanged();
    }
}
