/*
 * Description: This class contains the implementation of webview to show html documents
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */

package com.example.emulator8051;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Help extends Activity {

	WebView webview;
	ListView listview;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		webview = (WebView) findViewById(R.id.webView1);
		listview = (ListView) findViewById(R.id.list);
		try {															//for default display of user manual in help screen
			InputStream fin = getAssets().open("user_manual.html");
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			webview.loadData(new String(buffer), "text/html", "UTF-8");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String list[] = getResources().getStringArray(R.array.helplist);
		if (listview != null) {
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					String text = "User Manual";

					try {
						text = parent.getItemAtPosition(position).toString();
						System.out.println(text);
					} catch (Exception e) {

					}

					if (text != null) {
						if (text.equals("User Manual")) {			//on selection of user manual
							try {
								InputStream fin = getAssets().open(
										"user_manual.html");
								byte[] buffer = new byte[fin.available()];
								fin.read(buffer);
								fin.close();
								webview.loadData(new String(buffer),
										"text/html", "UTF-8");
							} catch (IOException e) {
								e.printStackTrace();
							}

						} 
						else if (text.equals("About 8051")) {		//on selection about 8051
							try {
								InputStream fin = getAssets().open(
										"microcontroller.html");
								byte[] buffer = new byte[fin.available()];
								fin.read(buffer);
								fin.close();
								webview.loadDataWithBaseURL("file:///android_asset/", new String(
										buffer), "text/html", "utf-8", null);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} 
						else if (text.equals("Experiments List")) {		//on selection of Experiments list
							
							try {
								InputStream fin = getAssets().open(
										"list_of_expt.html");
								byte[] buffer = new byte[fin.available()];
								fin.read(buffer);
								fin.close();
								webview.loadData(new String(buffer),
										"text/html", "UTF-8");
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					}
				}

			});

		}
	}

}
