package hu.respawncontrol;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hu.respawncontrol.data.ItemType;


public class TimeTrialOptionsDialog extends DialogFragment {

    private static final String TAG = "TimeTrialDialog";

    public interface DialogFinishedListener {
        void sendResult(ArrayList<ItemType> itemTypes, String calcNum);
    }
    public DialogFinishedListener dialogFinishedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_time_trial_options, container, false);

        final TextView tvItemType = view.findViewById(R.id.tvItemType);
        final GridLayout checkboxGrid = view.findViewById(R.id.checkboxGridTimeTrial);

        Bundle bundle = getArguments();
        if(bundle == null) {
            Log.e(TAG, "No argument was passed to the dialog fragment.");
            return null;
        }

        final ArrayList<ItemType> itemTypes = bundle.getParcelableArrayList(MainActivity.ITEM_TYPES);
        if(itemTypes == null) {
            Log.e(TAG, "Dialog did not receive valid item type list.");
            return null;
        }

        final List<CheckBox> checkboxes = new ArrayList<>();

        for(ItemType itemType : itemTypes) {
            CheckBox checkBox = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.checkbox_time_trial_dialog, checkboxGrid, false);
            checkBox.setText(itemType.getName());
            checkboxGrid.addView(checkBox);
            checkboxes.add(checkBox);
        }

        final TextView tvCalcNum = view.findViewById(R.id.tvCalcNum);
        final RadioGroup rgCalcNum = view.findViewById(R.id.rgCalcNum);

        final Button btnTrialOk = view.findViewById(R.id.btnTrialOk);

        btnTrialOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure at least one checkbox is checked
                boolean isAtLeastOneChecked = false;
                for(CheckBox checkBox : checkboxes) {
                    if(checkBox.isChecked()) {
                        isAtLeastOneChecked = true;
                        break;
                    }
                }
                if(!isAtLeastOneChecked) {
                    Snackbar.make(view, "Please select at least one item type.", Snackbar.LENGTH_LONG).show();
                    tvItemType.setError("");
                    return;
                }
                tvItemType.setError(null);

                // Make sure a radio button is checked
                int buttonId = rgCalcNum.getCheckedRadioButtonId();
                if(buttonId == -1) {
                    Snackbar.make(view, "Please select the desired amount of calculations.", Snackbar.LENGTH_LONG).show();
                    tvCalcNum.setError("");
                    return;
                }
                tvCalcNum.setError(null);

                ArrayList<ItemType> selectedItemTypes = new ArrayList<>();
                for(CheckBox checkBox : checkboxes) {
                    if(checkBox.isChecked()) {
                        int index = checkboxes.indexOf(checkBox);
                        selectedItemTypes.add(itemTypes.get(index));
                    }
                }

                final RadioButton selectedButton = view.findViewById(buttonId);
                String calcNumText = selectedButton.getText().toString();
                if(calcNumText.equals("Custom")) {
                    final EditText etCustomTrial = view.findViewById(R.id.etCustomTrial);
                    calcNumText = etCustomTrial.getText().toString();
                }

                dialogFinishedListener.sendResult(selectedItemTypes, calcNumText);
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogFinishedListener = (DialogFinishedListener) getActivity();
    }
}
