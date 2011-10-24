package me.taedium.android.add;

import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import me.taedium.android.ApplicationGlobals;
import me.taedium.android.R;
import me.taedium.android.add.AddTags;

public class AddLocation extends WizardActivity {
    private static final String module = "ADD_LOCATION";    //used for log messages
    private static final int MAP_ADD_ACTIVITY = 61;
    private static final int DIALOG_ENTER_ADDRESS = 303;
    
    private double latitude = 0.0;
    private double longitude = 0.0;

    private RadioButton rbLocationEnabled, rbLocationDisabled;
    private Button bMyLocation, bUseMap, bSpecifyAddress;
    private TextView tvLatDisplay, tvLongDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_5_location);
        
        initializeWizard(this, AddTags.class, ACTIVITY_ADD_TAGS);
        data = getIntent().getExtras();
        
        // Setup the click listeners for the location buttons
        bMyLocation = (Button)findViewById(R.id.bAddMyLocation);
        bMyLocation.setEnabled(false);
        bMyLocation.setOnClickListener(new OnClickListener() {            
            public void onClick(View v) {
                // Find current GPS location
                Location location = ApplicationGlobals.getInstance().getCurrentLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    updateLatLongDisplay();
                }
            }
        });
        bUseMap = (Button)findViewById(R.id.bAddUseMap);
        bUseMap.setEnabled(false);
        bUseMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Start the mapview for retrieving location
                Intent intent = new Intent(AddLocation.this, MapAdd.class);
                startActivityForResult(intent, MAP_ADD_ACTIVITY);
            }
        });
        bSpecifyAddress = (Button)findViewById(R.id.bAddSpecifyAddress);
        bSpecifyAddress.setEnabled(false);
        bSpecifyAddress.setOnClickListener(new OnClickListener() {            
            public void onClick(View v) {
                // Show the enter address dialog
                showDialog(DIALOG_ENTER_ADDRESS);
            }
        });

        // Setup checkboxes
        rbLocationEnabled = (RadioButton)findViewById(R.id.rbAddLocationEnabled);
        rbLocationEnabled.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bMyLocation.setEnabled(true);
                bUseMap.setEnabled(true);
                bSpecifyAddress.setEnabled(true);
                updateLatLongDisplay();
            }
        });
        rbLocationDisabled = (RadioButton)findViewById(R.id.rbAddLocationDisabled);
        rbLocationDisabled.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bMyLocation.setEnabled(false);
                bUseMap.setEnabled(false);
                bSpecifyAddress.setEnabled(false);
                updateLatLongDisplay();
            }
        });
        
        // Set up lat/long textview displays
        tvLatDisplay = (TextView)findViewById(R.id.tvAddLatDisplay);
        tvLongDisplay = (TextView)findViewById(R.id.tvAddLongDisplay);
        updateLatLongDisplay();
        restoreData();
        
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        switch (requestCode) {
        case MAP_ADD_ACTIVITY:
            if (resultCode == RESULT_OK) {
                latitude = i.getExtras().getDouble("long");
                longitude = i.getExtras().getDouble("lat");
                updateLatLongDisplay();
            }
            break;
        default:
            super.onActivityResult(requestCode, resultCode, i);
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        Button bOk;
        final Dialog dialog;
        dialog = new Dialog(new ContextThemeWrapper(this, R.style.Dialog));
        switch(id) {
            case DIALOG_ENTER_ADDRESS:
                dialog.setContentView(R.layout.add_address);
                dialog.setTitle("Enter Address");
                bOk = (Button)dialog.findViewById(R.id.bEnterAddress);
                bOk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Obtain the address
                        EditText streetNum = (EditText)dialog.findViewById(R.id.etStreetNumber);
                        EditText streetName = (EditText)dialog.findViewById(R.id.etStreetName);
                        EditText etCity = (EditText)dialog.findViewById(R.id.etCity);
                        EditText etProvince = (EditText)dialog.findViewById(R.id.etProvince);
                        String city = etCity.getText().toString();
                        if (city.equalsIgnoreCase("")) city = getString(R.string.default_city);                        
                        String province = etProvince.getText().toString();
                        if (province.equalsIgnoreCase("")) province = getString(R.string.default_province);
                        
                        String address = streetNum.getText().toString() + " " + streetName.getText().toString() +
                            ", " + city + ", " + province;
                        
                        getLocation(address);
                        dismissDialog(DIALOG_ENTER_ADDRESS);
                    }
                });
                break;
            default:
                // This activity dialog not known to this activity
                return super.onCreateDialog(id);
        }
        return dialog;
    }
    
    private void getLocation(String address) {
        Geocoder coder = new Geocoder(this);

        List<Address> addressList;

        try {
            addressList = coder.getFromLocationName(address,5);
            if (address != null) {            
                Address location = addressList.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                updateLatLongDisplay();
            }
        }
        catch (IOException e) {
            Log.e(module,e.getStackTrace().toString());
        }
    }
    
    private void updateLatLongDisplay() {
        Boolean hasValue = (latitude != 0 || longitude != 0);
        tvLatDisplay.setText("Latitude: " + (hasValue ? latitude : "None"));
        tvLongDisplay.setText("Longitude: " + (hasValue ? longitude : "None"));
        if (rbLocationEnabled.isChecked()) {
            if (!hasValue) {
                bNext.setEnabled(false);
            }
            tvLatDisplay.setVisibility(View.VISIBLE);
            tvLongDisplay.setVisibility(View.VISIBLE);
        } else {
            bNext.setEnabled(true);
            tvLatDisplay.setVisibility(View.GONE);
            tvLongDisplay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void fillData() {
        data.putDouble("lat", latitude);
        data.putDouble("long", longitude);
    }

    @Override
    protected void restoreData() {
        // Restore previously saved data
        if (data.containsKey("long") && data.containsKey("lat") && (data.getDouble("long") != 0 || data.getDouble("lat") != 0)) {
            rbLocationEnabled.setChecked(true);
            latitude = data.getDouble("lat");
            longitude = data.getDouble("long");
            updateLatLongDisplay();
        }
    }
}
