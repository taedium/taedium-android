package me.taedium.android.profile;

import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import me.taedium.android.ApplicationGlobals;
import me.taedium.android.HeaderActivity;
import me.taedium.android.R;
import me.taedium.android.listener.LoggedInChangedListener;


public class ProfileActivity extends HeaderActivity implements LoggedInChangedListener {
	
	ViewSwitcher vsMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.profile_page_title);
		setContentView(R.layout.profile);
		addLoggedInListener(this);
		
		// Display appropriate view depending on if user is logged in or not
		vsMain = (ViewSwitcher) findViewById(R.id.vsProfile);
		if (ApplicationGlobals.getInstance().isLoggedIn(this)) {
			vsMain.showNext();
			setupLoggedInView();
		} else {
			vsMain.showNext();
			vsMain.showNext();
			setupNotLoggedInView();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeLoggedInListener(this);
	}
	
	private void setupLoggedInView() {
		
		// Set user's name in main text
		TextView tvWelcome = (TextView) findViewById(R.id.tvLoggedInAs);
		tvWelcome.setText(String.format(getString(R.string.tvLoggedInAs), ApplicationGlobals.getInstance().getUser(this)));
		
	}
	
	
	private void setupNotLoggedInView() {
		
		Button bLogin = (Button) findViewById(R.id.bLoginProfile);
		bLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		Button bRegister = (Button) findViewById(R.id.bRegisterProfile);
		bRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	@Override
	public void loggedIn() {
		vsMain.showNext();
		setupLoggedInView();
	}
	
	public void loggedOut() {}
	

}
