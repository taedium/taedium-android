package me.taedium.android.add;

import me.taedium.android.R;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddEnvironment extends WizardActivity {
    CheckBox cbIndoor, cbOutdoor, cbTown;
    RadioGroup rgCostType;
    RadioButton rbFree, rbFlat, rbPerPerson;
    EditText etCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_4_environment);
        
        initializeWizard(this, AddLocation.class, ACTIVITY_ADD_LOCATION);
        data = getIntent().getExtras();

        title = this.getResources().getString(R.string.help_add_environment_title);
        helpText = this.getResources().getString(R.string.help_add_environment); 
        
        cbIndoor = (CheckBox)findViewById(R.id.cbAddIndoor);
        cbOutdoor = (CheckBox)findViewById(R.id.cbAddOutdoor);
        cbTown = (CheckBox)findViewById(R.id.cbAddAroundTown);
        rbFree = (RadioButton)findViewById(R.id.rbAddCostFree);
        rbFlat = (RadioButton)findViewById(R.id.rbAddCostFlat);
        rbPerPerson = (RadioButton)findViewById(R.id.rbAddCostPerPerson);
        etCost = (EditText)findViewById(R.id.etAddCost);
        
        rgCostType = (RadioGroup)findViewById(R.id.rgAddCostType);
        rgCostType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbFree.getId()) {
                    etCost.setEnabled(false);
                } else {
                    etCost.setEnabled(true);
                }
            }
        });
        
        restoreData();
        if (rgCostType.getCheckedRadioButtonId() == rbFree.getId()) {
            etCost.setEnabled(false);
        } else {
            etCost.setEnabled(true);
        }
    }

    @Override
    protected void fillData() {
        data.putBoolean("indoor", cbIndoor.isChecked());
        data.putBoolean("outdoor", cbOutdoor.isChecked());
        data.putBoolean("town", cbTown.isChecked());
        data.putInt("cost_type", rgCostType.getCheckedRadioButtonId());
        data.putString("cost", escapeString(etCost.getText().toString()));
    }

    @Override
    protected void restoreData() {
        // Restore defaults
        if (data.containsKey("indoor")) {
            cbIndoor.setChecked(data.getBoolean("indoor"));
        }
        if (data.containsKey("outdoor")) {
            cbOutdoor.setChecked(data.getBoolean("outdoor"));
        }
        if (data.containsKey("town")) {
            cbTown.setChecked(data.getBoolean("town"));
        }
        if (data.containsKey("cost_type")) {
            rgCostType.check(data.getInt("cost_type"));
        }
        if (data.containsKey("cost")) {
            etCost.setText(data.getString("cost"));
        }
    }
}
