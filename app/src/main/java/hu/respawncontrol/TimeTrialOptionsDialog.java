package hu.respawncontrol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hu.respawncontrol.data.TimeTrialViewModel;
import hu.respawncontrol.data.room.entity.Difficulty;
import hu.respawncontrol.data.room.entity.ItemType;
import hu.respawncontrol.data.room.entity.ItemTypeGroup;
import hu.respawncontrol.data.room.helper.ItemTypeGroupWithItemTypes;


public class TimeTrialOptionsDialog extends DialogFragment {
    private static final String TAG = "TimeTrialDialog";

    private TimeTrialViewModel viewModel;

    private Spinner spinner;
    private RadioGroup rgTestAmount;

    public interface DialogFinishedListener {
        void dialogFinished();
    }

    public DialogFinishedListener dialogFinishedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_time_trial_options, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        spinner = (Spinner) view.findViewById(R.id.spinner_itemTypeGroup);
        final TextView tvItemType = view.findViewById(R.id.tvItemType);
        final TextView tvTestAmount = view.findViewById(R.id.tvTestAmount);
        rgTestAmount = view.findViewById(R.id.rgTestAmount);

        viewModel = new ViewModelProvider(this.getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(TimeTrialViewModel.class);

        viewModel.getAllItemTypeGroupsWithItemTypes().observe(this, new Observer<List<ItemTypeGroupWithItemTypes>>() {
            @Override
            public void onChanged(List<ItemTypeGroupWithItemTypes> itemTypeGroupWithItemTypes) {
                inflateSpinner(itemTypeGroupWithItemTypes);
            }
        });

        viewModel.getDifficulties().observe(this, new Observer<List<Difficulty>>() {
            @Override
            public void onChanged(List<Difficulty> difficulties) {
                inflateDifficultyRadioButtons(difficulties, view);
            }
        });

        final Button btnTrialOk = view.findViewById(R.id.btnTrialOk);
        btnTrialOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make sure a spinner item is selected
                int selectedSpinnerItemPosition = spinner.getSelectedItemPosition();
                if (selectedSpinnerItemPosition == AdapterView.INVALID_POSITION) {
                    Snackbar.make(view, "Please select an item type group.", Snackbar.LENGTH_LONG).show();
                    tvItemType.setError("");
                    return;
                }
                tvItemType.setError(null);

                // Make sure a radio button is checked
                int checkedRadioButtonId = rgTestAmount.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Snackbar.make(view, "Please select the desired amount of calculations.", Snackbar.LENGTH_LONG).show();
                    tvTestAmount.setError("");
                    return;
                }
                tvTestAmount.setError(null);

                final RadioButton selectedButton = view.findViewById(checkedRadioButtonId);
                Difficulty selectedDifficulty = (Difficulty) selectedButton.getTag(R.id.difficulty_tag);

                int testAmount;
                if (selectedDifficulty.getName().equals("Custom")) {
                    final EditText etCustomTrial = view.findViewById(R.id.etCustomTrial);

                    try {
                        testAmount = Integer.parseInt(etCustomTrial.getText().toString());
                    } catch (NumberFormatException e) {
                        Snackbar.make(view, "Please enter a valid amount.", Snackbar.LENGTH_LONG).show();
                        tvTestAmount.setError("");
                        return;
                    }
                    if (testAmount < 1) {
                        Snackbar.make(view, "Please enter a valid amount.", Snackbar.LENGTH_LONG).show();
                        tvTestAmount.setError("");
                        return;
                    }
                    tvTestAmount.setError(null);
                } else {
                    testAmount = Integer.parseInt(selectedButton.getText().toString());
                }

                viewModel.setSelectedDifficulty(selectedDifficulty);
                viewModel.setSelectedItemTypeGroupWithItemTypes(selectedSpinnerItemPosition);
                viewModel.setSelectedTestAmount(testAmount);

                dialogFinishedListener.dialogFinished();
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void inflateSpinner(List<ItemTypeGroupWithItemTypes> itemTypeGroupsWithItemTypes) {
        ArrayList<String> entryStrings = new ArrayList<>();

        for (ItemTypeGroupWithItemTypes itemTypeGroupWithItemTypes : itemTypeGroupsWithItemTypes) {
            ItemTypeGroup itemTypeGroup = itemTypeGroupWithItemTypes.itemTypeGroup;
            List<ItemType> itemTypes = itemTypeGroupWithItemTypes.itemTypes;

            StringBuilder itemTypeString = new StringBuilder();
            for (int i = 0; i < itemTypes.size() - 1; i++) {
                itemTypeString.append(itemTypes.get(i).getShortName());
                itemTypeString.append(", ");
            }
            itemTypeString.append(itemTypes.get(itemTypes.size() - 1).getShortName());

            entryStrings.add(itemTypeGroup.getName() + " (" + itemTypeString + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                R.layout.spinner_item_custom, entryStrings);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        spinner.setAdapter(adapter);
    }

    private void inflateDifficultyRadioButtons(List<Difficulty> difficulties, final View view) {
        for (Difficulty difficulty : difficulties) {
            String difficultyName = difficulty.getName();

            RadioButton radioButton = (RadioButton) LayoutInflater.from(this.getActivity())
                    .inflate(R.layout.radiobutton_custom, null);
            radioButton.setText(difficultyName);
            radioButton.setTag(R.id.difficulty_tag, difficulty);

            if (difficultyName.equals("Custom")) {
                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final LinearLayout customNumberLayout = (LinearLayout) view.findViewById(R.id.customAmountLayout);
                        if (isChecked) {
                            customNumberLayout.setVisibility(View.VISIBLE);
                        } else {
                            customNumberLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }

            rgTestAmount.addView(radioButton);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogFinishedListener = (DialogFinishedListener) getActivity();
    }
}
