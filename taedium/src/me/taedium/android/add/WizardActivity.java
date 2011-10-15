package me.taedium.android.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import me.taedium.android.HeaderActivity;
import me.taedium.android.R;

public abstract class WizardActivity extends HeaderActivity {
    protected final static int ACTIVITY_ADD_PEOPLE = 550;
    protected final static int ACTIVITY_ADD_TIME = 551;
    protected final static int ACTIVITY_ADD_ENVIRONMENT = 552;
    protected final static int ACTIVITY_ADD_LOCATION = 553;
    protected final static int ACTIVITY_ADD_TAGS = 554;
    protected final static int ACTIVITY_FIRST_START = 555;
    
    protected Bundle data;
    protected Button bBack, bNext;
    
    protected void initializeWizard(final Context context, final Class<?> cls, final int requestCode) {
        initializeHeader();
        
        // Set header button to no longer be clickable since we are already on "Add Activity" page
        Button bAddRec = (Button)findViewById(R.id.bAdd);
        bAddRec.setEnabled(false);
        bAddRec.setVisibility(Button.INVISIBLE);

        // Initialize the back and previous buttons
        bNext = (Button)findViewById(R.id.bAddNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fillData();
                Intent i = new Intent(context, cls);
                i.putExtras(data);
                startActivityForResult(i, requestCode);
            }
        });
        bBack = (Button)findViewById(R.id.bAddBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fillData();
                Intent i = new Intent();
                i.putExtras(data);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
    
    // Helper to format string correctly when adding them to the data bundle
    // Inserts newlines correctly and removes tabs
    protected void addStringToData(String key, EditText text) {
    	String value = text.getText().toString().replace("\n", "\\n").replace("\t", "");
    	data.putString(key, value);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode == RESULT_OK) {
            data = i.getExtras();
        }
    }
    
    protected abstract void fillData();
    protected abstract void restoreData();
}
