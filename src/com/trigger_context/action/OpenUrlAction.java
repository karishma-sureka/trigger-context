package com.trigger_context.action;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class OpenUrlAction extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("urlAction");
		if (!url.startsWith("https://") && !url.startsWith("http://")) {
			url = "http://" + url;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
		finish();
	}
}
