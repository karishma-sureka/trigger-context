package com.trigger_context.action;

import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.trigger_context.R;

public class PostTweetAction extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String tweet = bundle.getString("tweetTextAction");

		Intent shareIntent = findTwitterClient();
		if (shareIntent != null) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, tweet);
			startActivity(Intent.createChooser(shareIntent, "Share"));
			finish();
		} else {

			noti("Twitter action failed.", "No valid twitter app installed");
			finish();
		}
	}

	public Intent findTwitterClient() {
		final String[] twitterApps = { "com.twitter.android", // official - 10
																// 000
				"com.twidroid", // twidroid - 5 000
				"com.handmark.tweetcaster", // Tweetcaster - 5 000
				"com.thedeck.android" }; // TweetDeck - 5 000 ;
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		return null;
	}

	private int mid = 1500;

	public void noti(String title, String txt) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(txt);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mid++, mBuilder.build());
	}
}
