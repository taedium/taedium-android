package me.taedium.android.add;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import me.taedium.android.R;

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
        
        etName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText name = (EditText)v;
                if (name.getText().length() > 0) {
                    bNext.setEnabled(true);
                } else if (name.getText().length() == 0) {
                    bNext.setEnabled(false);
                }
                return false;
            }
        });
    }

    @Override
    protected void fillData() {
    	addStringToData("name", etName);
    	addStringToData("desc", etDesc);
    }

    @Override
    protected void restoreData() {
        // TODO Auto-generated method stub
        
    }
}
