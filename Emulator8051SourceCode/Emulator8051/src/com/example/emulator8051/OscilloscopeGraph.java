/*
 * Description: This class implements the canvas(graph) of the oscilloscope
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */
package com.example.emulator8051;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class OscilloscopeGraph extends View implements OnGestureListener {
	/*paint variables declaration for different colors*/
	
	Paint paint = new Paint();
	Paint paint1 = new Paint();
	Paint paint2 = new Paint();
	private GestureDetector gDetector;   //declaring variables used
	boolean moving = false;
	float eventX, eventY;
	float originX = 0, originY = 0;

	public OscilloscopeGraph(Context context) {

		super(context);
		gDetector = new GestureDetector(this);
		// TODO Auto-generated constructor stub
	}

	public OscilloscopeGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		gDetector = new GestureDetector(this);
	}

	@SuppressWarnings("deprecation")
	public OscilloscopeGraph(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		gDetector = new GestureDetector(this);
	}

	public void onDraw(Canvas canvas) {
		paint.setColor(Color.GRAY);
		paint1.setStrokeWidth(5);
		paint1.setColor(Color.GREEN);
		paint2.setColor(Color.RED);
		int x = canvas.getWidth();
		int y = canvas.getHeight();
		int k = 0, j = 0;
		
		/*drawing grid goes here*/
		while (k < x) {
			canvas.drawLine(k, 0, k, y, paint);
			k += 15;
		}
		while (j < y) {
			canvas.drawLine(0, j, x, j, paint);
			j += 15;
		}

		if (moving) { // if scrolled

			canvas.translate(eventX - canvas.getWidth() / 2,
					eventY - canvas.getHeight() / 2);
			originX = eventX - canvas.getWidth() / 2;
			originY = eventY - canvas.getHeight() / 2;
		}

		else {
			canvas.translate(originX, originY);
		}
		canvas.drawLine(75, -480, 75, 480, paint1);
		canvas.drawLine(-800, 300, 800, 300, paint1);

		int x1 = 75, y1 = 300;
		if (VoltageOutput.timeline[0] != 0) {

			canvas.drawLine(x1, y1, x1 + VoltageOutput.timeline[0] * 5, y1,
					paint1);
			canvas.drawLine(x1 + VoltageOutput.timeline[0] * 5, y1, x1
					+ VoltageOutput.timeline[0] * 5, y1
					- (int) (VoltageOutput.vout[0]) * 20, paint1);
		}
		//for displaying the values 
		canvas.drawText(Integer.toString(VoltageOutput.timeline[0]), x1
				+ VoltageOutput.timeline[0] * 5, 310, paint2);
		canvas.drawText(Integer.toString((int) VoltageOutput.vout[0]), 65, y1
				- (int) (VoltageOutput.vout[0]) * 20, paint2);
		for (int i = 1; i <= VoltageOutput.counter; i++) {

			canvas.drawLine(x1 + VoltageOutput.timeline[i - 1] * 5, y1
					- (int) (VoltageOutput.vout[i - 1]) * 20, x1
					+ VoltageOutput.timeline[i] * 5, y1
					- (int) (VoltageOutput.vout[i - 1]) * 20, paint1);
			canvas.drawLine(x1 + VoltageOutput.timeline[i] * 5, y1
					- (int) (VoltageOutput.vout[i - 1]) * 20, x1
					+ VoltageOutput.timeline[i] * 5, y1
					- (int) (VoltageOutput.vout[i]) * 20, paint1);
			canvas.drawText(Integer.toString(VoltageOutput.timeline[i]), x1
					+ VoltageOutput.timeline[i] * 5, 310, paint2);
			canvas.drawText(Integer.toString((int) VoltageOutput.vout[i]), 65,
					y1 - (int) (VoltageOutput.vout[i]) * 20, paint2);

		}

	}
	
	/*detecting the event on touch goes here*/

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float getx, gety;
		getx = event.getX();
		gety = event.getY();

		return gDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	// USED TO TRANSLATE
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub

		eventX = e2.getX();
		eventY = e2.getY();
		moving = true;
		invalidate();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub

		return false;
	}

}