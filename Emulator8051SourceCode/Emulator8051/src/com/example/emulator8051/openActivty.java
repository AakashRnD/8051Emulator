/*
 * Description: This class contains the implementation of open and delete functions of experiments saved
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */

package com.example.emulator8051;

import java.io.File;
import java.io.FileFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class openActivty extends Activity {

	ArrayAdapter<String> adapter;
	ListView listView;
	Context context;
	static boolean set = false;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);
		listView = (ListView) findViewById(R.id.list);

		listView.setAdapter(new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getlist()));
		
		/*retreiving the experiment name selected in listview
		 * 
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String name = parent.getItemAtPosition(position).toString();
				set = true;
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						ApplicationActivity.class);
				intent.putExtra("projectname", name);
				startActivity(intent);

			}
		});
		
		//on long click of item in listview
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View arg1,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				final String name = parent.getItemAtPosition(position)
						.toString();
				System.out.println(name);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						openActivty.this);
				alertDialogBuilder
						.setTitle("Are you sure to Delete?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										String root = Environment
												.getExternalStorageDirectory()
												.getPath();
										String deletefile = root
												+ "/Emulator_8051/" + name;
										System.out.println(deletefile);
										File file = new File(deletefile);
										if (file.exists()) {
											File[] files = file.listFiles();

											for (int i = 0; i < files.length; i++) {

												files[i].delete();
											}

											boolean result = file.delete();
											ApplicationActivity.isSaved=false;
											ApplicationActivity.project_name=null;
											Toast.makeText(getApplicationContext(), "deleted successfully", Toast.LENGTH_SHORT).show();
											}
										refreshData();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;

			}
		});

	}
	
	/*to refresh the listview
	 * after deletion of item
	 */
	public void refreshData() {

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getlist());
		listView.setAdapter(adapter);

	}

	static String[] mFiles = null;
	
	//to retrieve the files from sdcard
	public static String[] getlist() {

		// File lists= new File();
		System.out.println("inside getlist");
		File dir = new File("/sdcard/Emulator_8051");
		System.out.println("anupam");
		FileFilter fileFilter = (new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});

		File[] filelist = dir.listFiles(fileFilter);

		mFiles = new String[filelist.length];
		for (int i = 0; i < filelist.length; i++) {

			int endIndex = filelist[i].getAbsolutePath().lastIndexOf("/");
			if (endIndex != -1) {
				mFiles[i] = filelist[i].getAbsolutePath().substring(
						endIndex + 1);
			}
		}
		return mFiles;
	}
}
