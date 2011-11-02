package me.taedium.android.add;

import me.taedium.android.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class AddName extends WizardActivity {
    private EditText etName, etDesc;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.add_1_name);
        super.onCreate(savedInstanceState);
        
        initializeWizard(this, AddPeople.class, ACTIVITY_ADD_PEOPLE);
        data = new Bundle();
       
        bNext.setEnabled(false);
        
        etName = (EditText)findViewById(R.id.etAddName);
        etDesc = (EditText)findViewById(R.id.etAddDescription);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (etName.getText().length() > 0) {
                    bNext.setEnabled(true);
                } else if (etName.getText().length() == 0) {
                    bNext.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    @Override
    protected void fillData() {
    	addStringToData("name", etName);
    	addStringToData("desc", etDesc);
    	// If name/desc changed re-generate autotags
    	if (data.containsKey("autotags")) {
    	    data.remove("autotags");
    	}
    }

    @Override
    protected void restoreData() {
        // No need to restore data as this is first activity
    }
}
