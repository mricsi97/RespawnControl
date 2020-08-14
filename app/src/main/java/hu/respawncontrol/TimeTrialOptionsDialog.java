package hu.respawncontrol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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
        void sendResult(ArrayList<ItemType> itemTypes, Integer testAmount);
    }
    public DialogFinishedListener dialogFinishedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_time_trial_options, container, false);

        if(getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

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

        final TextView tvTestAmount = view.findViewById(R.id.tvTestAmount);
        final RadioGroup rgTestAmount = view.findViewById(R.id.rgTestAmount);

        final RadioButton rbCustom = view.findViewById(R.id.rbCustom);
        rbCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final LinearLayout customNumberLayout = (LinearLayout) view.findViewById(R.id.customNumberLayout);
                if(isChecked) {
                    customNumberLayout.setVisibility(View.VISIBLE);
                } else {
                    customNumberLayout.setVisibility(View.GONE);
                }
            }
        });

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
                int buttonId = rgTestAmount.getCheckedRadioButtonId();
                if(buttonId == -1) {
                    Snackbar.make(view, "Please select the desired amount of calculations.", Snackbar.LENGTH_LONG).show();
                    tvTestAmount.setError("");
                    return;
                }
                tvTestAmount.setError(null);

                ArrayList<ItemType> selectedItemTypes = new ArrayList<>();
                for(CheckBox checkBox : checkboxes) {
                    if(checkBox.isChecked()) {
                        int index = checkboxes.indexOf(checkBox);
                        selectedItemTypes.add(itemTypes.get(index));
                    }
                }

                final RadioButton selectedButton = view.findViewById(buttonId);
                String buttonText = selectedButton.getText().toString();

                int testAmount;
                if(buttonText.equals("Custom")){
                    final EditText etCustomTrial = view.findViewById(R.id.etCustomTrial);

                    testAmount = Integer.parseInt(etCustomTrial.getText().toString());
                    if(testAmount < 1) {
                        Snackbar.make(view, "Please enter a valid amount.", Snackbar.LENGTH_LONG).show();
                        tvTestAmount.setError("");
                        return;
                    }
                    tvTestAmount.setError(null);
                } else {
                    testAmount = Integer.parseInt(buttonText);
                }

                dialogFinishedListener.sendResult(selectedItemTypes, testAmount);
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
