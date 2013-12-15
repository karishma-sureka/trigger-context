package com.trigger_context;

import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final SharedPreferences data_sp = getSharedPreferences(Main_Service.MY_DATA,
				MODE_PRIVATE);
		final SharedPreferences.Editor editor = data_sp.edit();
		final Button save = (Button) findViewById(R.id.button1);
		final EditText et1 = (EditText) findViewById(R.id.editText1);
		final EditText et2 = (EditText) findViewById(R.id.editText2);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editor.putString("username", et1.getText().toString());
				editor.putLong("timeout", Integer.parseInt(et2.getText().toString()) * 1000);
				editor.commit();
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
