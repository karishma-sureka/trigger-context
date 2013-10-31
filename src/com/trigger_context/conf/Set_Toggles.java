package com.trigger_context.conf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trigger_context.R;

public class Set_Toggles extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_toggles);
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("ToggleAction", true);
				RadioGroup bluetooth = (RadioGroup) findViewById(R.id.radioGroup1);
				RadioButton rb = (RadioButton) bluetooth.getChildAt(0);
				RadioButton rb1 = (RadioButton) bluetooth.getChildAt(1);
				if (rb.isChecked()) {
					bundle.putBoolean("bluetoothAction", true);
				} else if (rb1.isChecked()) {
					bundle.putBoolean("bluetoothAction", false);
				}
				RadioGroup wifi = (RadioGroup) findViewById(R.id.radioGroup2);
				RadioButton rb2 = (RadioButton) wifi.getChildAt(0);
				RadioButton rb3 = (RadioButton) wifi.getChildAt(1);
				if (rb2.isChecked()) {
					bundle.putBoolean("wifiAction", true);
				} else if (rb3.isChecked()) {
					bundle.putBoolean("wifiAction", false);
				}
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				Toast.makeText(getApplicationContext(),
						"Toggle settings saved!", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}