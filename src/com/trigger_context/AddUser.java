package com.trigger_context;

import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUser extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
		final Button saveConfigure = (Button) findViewById(R.id.button1);
		saveConfigure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getBaseContext(), Conditions_Config.class);
				myIntent.putExtra("mac", ((EditText)findViewById(R.id.editText2)).getText());
				myIntent.putExtra("name", ((EditText)findViewById(R.id.editText1)).getText());
				startActivity(myIntent);

			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}

}
