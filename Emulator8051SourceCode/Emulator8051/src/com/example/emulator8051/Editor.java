/*
 * Description: This class implements the UI of the editor
 * Author: Prayasee Pradhan    	   Email id: prayasee91@gmail.com
 * Author: Priyanka Padmanabhan	   Email id: priyanka.91092@gmail.com
 * 			
 * 
 */

package com.example.emulator8051;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Editor extends Activity {

	static EditText textarea; // declaring UI variables
	static TextView total_memory, opcode, logcat;
	Button compile, clear; // declaring other variables used
	static String asmcode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);

		textarea = (EditText) findViewById(R.id.editText1);
		opcode = (TextView) findViewById(R.id.rom_values);
		total_memory = (TextView) findViewById(R.id.memory_used);
		logcat = (TextView) findViewById(R.id.TV1);
		compile = (Button) findViewById(R.id.assemble);
		clear = (Button) findViewById(R.id.clear);
		opcode.setMovementMethod(new ScrollingMovementMethod());
		logcat.setMovementMethod(new ScrollingMovementMethod());
		
		
	}

	@Override
	public void onResume() {
		super.onResume();
		if (asmcode != null)
			textarea.setText(asmcode);
		else
			textarea.setText("");
	}

	@Override
	public void onPause() {
		super.onPause();
		if(!textarea.getText().toString().equals(asmcode)){
			ApplicationActivity.isSaved=false;
			ApplicationActivity.isNew=false;
		}
		if (textarea.getText().toString().length() > 0) {
			asmcode = textarea.getText().toString();
		}
		else{
			asmcode=null;
		}
		
	}
	
	/*on Clicking assemble button the function
	 * goes here to assemble the code
	 */

	public void onAssemble(View v) {
		asmcode = textarea.getText().toString();
		if (asmcode.length() > 0) {

			String[] line = asmcode.split("\n");
			for (int i = 0; i < line.length; i++) {
				System.out.println(line[i]);
			}
			Assemble.assembleFunction();
		} else {
			Toast.makeText(getApplicationContext(), "No code written",
					Toast.LENGTH_LONG).show();
		}
	}
	
	/*work to be done on clicking clear button 
	 * goes here
	 */

	public void onClear(View v) {
		textarea.setText("");
		opcode.setText("");
		logcat.setText("");
		total_memory.setText("");

	}

}
