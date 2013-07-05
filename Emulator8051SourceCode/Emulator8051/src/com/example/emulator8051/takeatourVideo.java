/*
 * Description: This class implements the webview to playvideo of swf fomat

 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * Author: Sweksha Tripati     Email id: swe1807@gmail.com			
 * 
 */

package com.example.emulator8051;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

public class takeatourVideo extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takeatour);
//getting the swf file
		
		WebView mWebView1 = (WebView) findViewById(R.id.webView1);
	    mWebView1.getSettings().setJavaScriptEnabled(true);
	    mWebView1.getSettings().setPluginsEnabled(true);
	    mWebView1.getSettings().getBuiltInZoomControls();
	    mWebView1.loadUrl("file:///android_asset/takeatour.swf");
		
	
	}

}
