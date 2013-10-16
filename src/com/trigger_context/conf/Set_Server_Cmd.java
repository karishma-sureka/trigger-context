package com.trigger_context.conf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trigger_context.R;

public class Set_Server_Cmd extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_cmd);
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("RemoteServerCmd", true);
				bundle.putString("cmd",
						((EditText) findViewById(R.id.editText2)).getText()
								.toString());
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				Toast.makeText(getApplicationContext(),
						"Sever cmd settings saved!", Toast.LENGTH_SHORT).show();
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