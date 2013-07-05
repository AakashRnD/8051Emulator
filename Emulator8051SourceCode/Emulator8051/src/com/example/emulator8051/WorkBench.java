/*
 * Description: This class implements the UI of workbench module
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * Author: Sweksha tripathi	   Email id: swe1807@gmail.com			
 * 
 */
package com.example.emulator8051;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class WorkBench extends Activity {

	/* declaration of variables 
	 * goes here
	 */
	boolean isExecutionComplete = false;

	Timer timer;
	static BreadBoard board;  //BreadBoard class object
	static String circuit;
	int k;
	static boolean token[];
	int counter = 0;
	Button build; // submit button to build circuit
	static Spinner spinners[] = new Spinner[4]; // array to save item selected
												// in spinners
	Dialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workbench);
		board = (BreadBoard) findViewById(R.id.view1);

	}

	public void createCircuit(View v) // called when build is clicked
	{
		showDialog(0);

	}

	public void run(View v) // button click
	{
		timer = new Timer();
		isExecutionComplete = false;
		final int starting_address = Assemble.starting_address;
		k = starting_address;
		Execute.osc_period = 0;
		Execute.timeline = 0;
		VoltageOutput.counter = 0;
		for (int i = 0; i < 256; i++) {
			Execute.ram[i] = "0";
		}

		if (Assemble.isAssemble) {
			counter = 1;
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (k != Assemble.ending_address) {

								k = Execute.nextPC(Assemble.rom[k], k);
								if (Circuit.component != null) {
									board.invalidate();
								}
							}

							else
								isExecutionComplete = true;

							if (isExecutionComplete) {
								timer.cancel();
							}

						}// end of run
					});

				}
			}, 0, 100);

		} else {
			Toast.makeText(getApplicationContext(),
					"Please Assemble your code", Toast.LENGTH_SHORT).show();
		}
	}

	/*implementation of oscilloscope
	 * button goes here (for dac circuit)
	 */
	public void oscilloscope(View v) {
		boolean flag = false;

		for (int i = 0; i < 4; i++) {
			if (Circuit.component[i] != null) {
				if (Circuit.component[i].getId() == 4) {
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			Toast.makeText(getApplicationContext(), "DAC is not implemented",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i;
			i = new Intent("com.example.emulator8051.oscilloscopeActivity");
			startActivity(i);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private Button.OnClickListener bulid_circuit // submit button
	= new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ApplicationActivity.isSaved = false;
			ApplicationActivity.isNew=false;
			int id = 0;
			Bitmap image = null;
			for (int i = 0; i < spinners.length; i++) {
				if (spinners[i].getSelectedItem().toString().equals("None")) {
					id = 0;
					image = null;
				} else if (spinners[i].getSelectedItem().toString()
						.equals("Led Circuit")) {
					id = 1;
					switch (i) {
					case 0:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.ledcircuit_0);
						break;
					case 1:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.ledcircuit_1);
						break;
					case 2:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.ledcircuit_port2);
						break;
					case 3:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.ledcircuit_port3);
						break;
					default:
						image = null;
					}
				} else if (spinners[i].getSelectedItem().toString()
						.equals("7 Segment Circuit")) {
					id = 2;
					if (i == 0)
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.s7segementled);
					else
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.s7segementled_mirror);

				} else if (spinners[i].getSelectedItem().toString()
						.equals("Stepper Motor")) {
					id = 3;
					switch (i) {
					case 0:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.sm0);
						break;
					case 1:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.sm13);
						break;
					case 2:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.sm2);
						break;
					case 3:
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.sm13);
						break;
					default:
						image = null;

					}
				} else if (spinners[i].getSelectedItem().toString()
						.equals("DAC Circuit")) {
					id = 4;
					if (i == 0 || i == 2)
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.dac_circuit1);
					else
						image = BitmapFactory.decodeResource(getResources(),
								R.drawable.dac_circuit_mirror1);
				}
				Circuit.component[i] = new Component(id, image);
			}
			board.invalidate();
			dialog.dismiss();

		}

	};

	@Override
	protected Dialog onCreateDialog(int id) { // for creating a dialogue when
												// button is clicked
		// TODO Auto-generated method stub
		dialog = new Dialog(WorkBench.this);

		dialog.setContentView(R.layout.popup);
		dialog.setTitle("Circuit Panel");
		build = (Button) dialog.findViewById(R.id.buttonxml);
		spinners[0] = (Spinner) dialog.findViewById(R.id.spinner1);
		spinners[1] = (Spinner) dialog.findViewById(R.id.spinner2);
		spinners[2] = (Spinner) dialog.findViewById(R.id.spinner3);
		spinners[3] = (Spinner) dialog.findViewById(R.id.spinner4);

		spinners[0].setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String st0;
				st0 = spinners[0].getSelectedItem().toString();
				if (st0.equals("Led Circuit")
						|| st0.equals("7 Segment Circuit")) {
					spinners[2].setSelection(0);
					spinners[2].setEnabled(false);
				} else {
					spinners[2].setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		//to enable and disable spinners according to the circuits selected
		spinners[1].setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String st0;
				st0 = spinners[1].getSelectedItem().toString();
				if (st0.equals("Led Circuit")
						|| st0.equals("7 Segment Circuit")) {
					spinners[3].setSelection(0);
					spinners[3].setEnabled(false);
				} else {
					spinners[3].setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinners[2].setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String st0;
				st0 = spinners[2].getSelectedItem().toString();
				if (st0.equals("Led Circuit")) {
					spinners[0].setSelection(0);
					spinners[0].setEnabled(false);
				} else {
					spinners[0].setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinners[3].setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String st0;
				st0 = spinners[3].getSelectedItem().toString();
				if (st0.equals("Led Circuit")) {
					// spinners[2].setEmptyView(spinners[2]);
					spinners[1].setSelection(0);
					spinners[1].setEnabled(false);
				} else {
					spinners[1].setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		build.setOnClickListener(bulid_circuit);

		return dialog;
	}
}
