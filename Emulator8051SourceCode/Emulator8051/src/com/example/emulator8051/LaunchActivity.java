/*
 * Description: This class implements the Canvas in the WorkBench module
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * Author: Sweksha tripathi	   Email id: swe1807@gmail.com
 * 
 */



package com.example.emulator8051;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class LaunchActivity extends Activity{
	
	private final int DISPLAY_LENGTH = 8000;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle icicle) {						//to show the splash screen for 5seconds
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,          
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.launchsreen);
		
		WebView mWebView1 = (WebView) findViewById(R.id.webView1);
			   mWebView1.getSettings().setJavaScriptEnabled(true);
			   mWebView1.getSettings().setPluginsEnabled(true);
			    mWebView1.getSettings().getBuiltInZoomControls();
			    mWebView1.loadUrl("file:///android_asset/splash_final.swf");

	/* New Handler to start the Menu-Activity
	* and close this Splash-Screen after DISPLAY_LENGTH/1000 seconds.*/
	new Handler().postDelayed(new Runnable(){

		@Override
		public void run() {
			/* Create an Intent that will start the Home-Activity. */
			Intent mainIntent = new Intent(LaunchActivity.this,HomeActivity.class);
			LaunchActivity.this.startActivity(mainIntent);
			LaunchActivity.this.finish();
		}
	}, DISPLAY_LENGTH);
	}
}
