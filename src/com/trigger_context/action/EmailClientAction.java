package com.trigger_context.action;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class EmailClientAction extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { bundle.getString("toAction", null) });
		sendIntent.setData(Uri.parse(bundle.getString("toAction", null)));
		sendIntent.putExtra(Intent.EXTRA_SUBJECT,
				bundle.getString("subjectAction", null));
		sendIntent.setType("plain/text");
		sendIntent.putExtra(Intent.EXTRA_TEXT,
				bundle.getString("emailMessageAction", null));
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(sendIntent);
		finish();
	}
}
