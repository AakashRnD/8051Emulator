/*
 * Description: This class implements the tab host and sliding drawer in the appplication
 * Author:Sumanth Myrala    Email id: sumanthmyrala123@gmail.com
 * 
 */
package com.example.emulator8051;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Internals8051 extends Activity {
	
	/*declaring UI variables*/
	static TextView acc, b, r0, r1, r2, r3, r4, r5, r6, r7, psw, pc, dph, dpl,
			sp, pt0, pt1, pt2, pt3, pv0, pv1, pv2, pv3, th1, tl1, tmod, tcon,
			th0, tl0, ie, ip;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internals);

		acc = (TextView) findViewById(R.id.textView31);
		b = (TextView) findViewById(R.id.textView32);

		r0 = (TextView) findViewById(R.id.textView33);
		r1 = (TextView) findViewById(R.id.textView34);
		r2 = (TextView) findViewById(R.id.textView35);
		r3 = (TextView) findViewById(R.id.textView36);
		r4 = (TextView) findViewById(R.id.textView37);
		r5 = (TextView) findViewById(R.id.textView38);
		r6 = (TextView) findViewById(R.id.textView39);
		r7 = (TextView) findViewById(R.id.textView40);

		psw = (TextView) findViewById(R.id.textView41);
		pc = (TextView) findViewById(R.id.textView42);
		dph = (TextView) findViewById(R.id.textView43);
		dpl = (TextView) findViewById(R.id.textView44);
		sp = (TextView) findViewById(R.id.textView45);

		pt0 = (TextView) findViewById(R.id.textView46);
		pt1 = (TextView) findViewById(R.id.textView47);
		pt2 = (TextView) findViewById(R.id.textView48);
		pt3 = (TextView) findViewById(R.id.textView49);

		pv0 = (TextView) findViewById(R.id.textView58);
		pv1 = (TextView) findViewById(R.id.textView59);
		pv2 = (TextView) findViewById(R.id.textView60);
		pv3 = (TextView) findViewById(R.id.textView61);

		th1 = (TextView) findViewById(R.id.textView54);
		tl1 = (TextView) findViewById(R.id.textView55);
		tmod = (TextView) findViewById(R.id.textView56);
		tcon = (TextView) findViewById(R.id.textView57);
		th0 = (TextView) findViewById(R.id.textView50);
		tl0 = (TextView) findViewById(R.id.textView51);

		ie = (TextView) findViewById(R.id.textView52);
		ip = (TextView) findViewById(R.id.textView53);

	}

	public void onResume() {
		super.onResume();
		updateInternals();
	}

	public static void updateInternals() {
		try {
			int ptr = Execute.ptr;
			
			/*setting updated values in the variables
			 * goes here
			 */

			acc.setText(Execute.ram[224]);
			b.setText(Execute.ram[240]);

			r0.setText(Execute.ram[0 + ptr]);
			r1.setText(Execute.ram[1 + ptr]);
			r2.setText(Execute.ram[2 + ptr]);
			r3.setText(Execute.ram[3 + ptr]);
			r4.setText(Execute.ram[4 + ptr]);
			r5.setText(Execute.ram[5 + ptr]);
			r6.setText(Execute.ram[6 + ptr]);
			r7.setText(Execute.ram[7 + ptr]);

			psw.setText(Execute.ram[208]);
			pc.setText(Assemble.end_add);
			dph.setText(Execute.ram[131]);
			dpl.setText(Execute.ram[130]);
			sp.setText(Execute.ram[129]);

			pv3.setText(Execute.ram[176]);
			pv2.setText(Execute.ram[160]);
			pv1.setText(Execute.ram[144]);
			pv0.setText(Execute.ram[128]);

			th1.setText(Execute.ram[141]);
			tl1.setText(Execute.ram[139]);
			tmod.setText(Execute.ram[137]);
			tcon.setText(Execute.ram[136]);
			th0.setText(Execute.ram[140]);
			tl0.setText(Execute.ram[138]);

			ip.setText(Execute.ram[184]);
			ie.setText(Execute.ram[168]);
		} catch (Exception e) {
		}
	}
}
