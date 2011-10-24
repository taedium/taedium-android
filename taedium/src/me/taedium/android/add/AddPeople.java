package me.taedium.android.add;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import me.taedium.android.R;

public class AddPeople extends WizardActivity {
    private EditText etMinPeople; 
    private EditText etMaxPeople;
    private RadioGroup rgAges; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_2_people);
        
        initializeWizard(this, AddTime.class, ACTIVITY_ADD_TIME);
        data = getIntent().getExtras();
        
        etMinPeople = (EditText)findViewById(R.id.etAddMinPeople);
        etMaxPeople = (EditText)findViewById(R.id.etAddMaxPeople);
        rgAges = (RadioGroup)findViewById(R.id.rgAddAges);
        restoreData();
    }
    
    @Override
    protected void fillData() {
    	addStringToData("min_people", etMinPeople);
    	addStringToData("max_people", etMaxPeople);
        data.putInt("ages", rgAges.getCheckedRadioButtonId());
    }

    @Override
    protected void restoreData() {
        // Restore saved settings
        if (data.containsKey("min_people")) {
            etMinPeople.setText(data.getString("min_people"));
        }
        if (data.containsKey("max_people")) {
            etMaxPeople.setText(data.getString("max_people"));
        }
        if (data.containsKey("ages")) {
            RadioButton selected = (RadioButton)findViewById(data.getInt("ages"));
            selected.setChecked(true);
        }
    }
}
