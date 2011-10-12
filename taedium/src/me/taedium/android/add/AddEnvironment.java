package me.taedium.android.add;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import me.taedium.android.R;

public class AddEnvironment extends WizardActivity {
    CheckBox cbIndoor, cbOutdoor, cbTown, cbCostPerPerson;
    EditText etCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_4_environment);
        
        initializeWizard(this, AddLocation.class, ACTIVITY_ADD_LOCATION);
        data = getIntent().getExtras();
        
        cbIndoor = (CheckBox)findViewById(R.id.cbIndoor);
        cbOutdoor = (CheckBox)findViewById(R.id.cbOutdoor);
        cbTown = (CheckBox)findViewById(R.id.cbAroundTown);
        etCost = (EditText)findViewById(R.id.etCost);
        cbCostPerPerson = (CheckBox)findViewById(R.id.cbCostPerPerson);
        
        restoreData();
    }

    @Override
    protected void fillData() {
        data.putBoolean("indoor", cbIndoor.isChecked());
        data.putBoolean("outdoor", cbOutdoor.isChecked());
        data.putBoolean("town", cbTown.isChecked());
        addStringToData("cost", etCost);
        data.putBoolean("cost_per_person", cbCostPerPerson.isChecked());
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
        if (data.containsKey("cost")) {
            etCost.setText(data.getString("cost"));
        }
        if (data.containsKey("cost_per_person")) {
            cbCostPerPerson.setChecked(data.getBoolean("cost_per_person"));
        }
    }
}
