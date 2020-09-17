package hu.respawncontrol.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
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

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;
import hu.respawncontrol.viewmodel.TimeTrialViewModel;
import hu.respawncontrol.model.room.entity.Difficulty;


public class TimeTrialOptionsDialog extends DialogFragment {
    private static final String TAG = "TimeTrialDialog";

    private TimeTrialViewModel viewModel;

    private Spinner spinner;
    private RadioGroup rgTestAmount;

    public interface DialogFinishedListener {
        void dialogFinished();
    }

    public DialogFinishedListener dialogFinishedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_time_trial_options, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        spinner = (Spinner) view.findViewById(R.id.spinner_itemGroup);
        final TextView tvItemGroupQuestion = view.findViewById(R.id.tvItemGroupQuestion);
        final TextView tvTestAmount = view.findViewById(R.id.tvTestAmount);
        rgTestAmount = view.findViewById(R.id.rgTestAmount);

        viewModel = new ViewModelProvider(this.getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(TimeTrialViewModel.class);

        viewModel.getAllItemGroupsWithItems().observe(this,
                new Observer<List<ItemGroupWithItems>>() {
                    @Override
                    public void onChanged(List<ItemGroupWithItems> itemGroupWithItems) {
                        inflateSpinner(itemGroupWithItems);
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
                    Snackbar.make(view, "Please select an item group.", Snackbar.LENGTH_LONG).show();
                    tvItemGroupQuestion.setError("");
                    return;
                }
                tvItemGroupQuestion.setError(null);

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
                viewModel.setSelectedItemGroupWithItems(selectedSpinnerItemPosition);
                viewModel.setSelectedTestAmount(testAmount);

                dialogFinishedListener.dialogFinished();
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void inflateSpinner(List<ItemGroupWithItems> itemGroupsWithItems) {
        ArrayList<String> entryStrings = new ArrayList<>();

        for (ItemGroupWithItems itemGroupWithItems : itemGroupsWithItems) {
            ItemGroup itemGroup = itemGroupWithItems.itemGroup;
            List<Item> items = itemGroupWithItems.items;

            StringBuilder itemString = new StringBuilder();
            for (int i = 0; i < items.size() - 1; i++) {
                itemString.append(items.get(i).getShortName());
                itemString.append(", ");
            }
            itemString.append(items.get(items.size() - 1).getShortName());

            entryStrings.add(itemGroup.getItemGroupName() + " (" + itemString + ")");
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

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }
}
