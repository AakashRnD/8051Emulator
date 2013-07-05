/*
 * Description: This class implements the Canvas in the WorkBench module
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * Author: Sweksha tripathi	   Email id: swe1807@gmail.com
 * 
 */

package com.example.emulator8051;


import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class BreadBoard  extends View implements OnGestureListener{
	
	
	private Canvas canvas; 
	Paint paint = new Paint();
	Paint paint1 = new Paint();    //declaring paint for different colors
	Paint paint2 = new Paint();
	Paint paint3 = new Paint();
	static boolean setOscilloscope=false;
	BlurMaskFilter theBlur = new BlurMaskFilter(5F, BlurMaskFilter.Blur.NORMAL);
	int angle;
	double iout=0,vout=0;
	private GestureDetector gDetector;   //for scrolling the grid
	boolean moving=false,glow=false;
	float eventX , eventY;
	float originX=0, originY=0;
	
	@SuppressWarnings("deprecation")
	public BreadBoard(Context context) {
		super(context); 
		paint.setColor(Color.RED);
		paint.setAlpha(50);
		gDetector = new GestureDetector(this);
		 
	// TODO Auto-generated constructor stub
	}
	

	@SuppressWarnings("deprecation")
	public  BreadBoard(Context context, AttributeSet attrs) {
		
		
	 
	super( context, attrs );
	gDetector = new GestureDetector(this);

	
	}
	 
	@SuppressWarnings("deprecation")
	public BreadBoard(Context context, AttributeSet attrs, int defStyle) {
	 
	super( context, attrs, defStyle );
	gDetector = new GestureDetector(this);


	}

	public void onDraw(Canvas canvas)
	{
		paint.setColor(Color.GRAY);
		paint.setAlpha(100);
		paint1.setColor(Color.GREEN);
		paint3.setColor(Color.RED);
		paint3.setStrokeWidth(7);
		int posX = 0, posY = 0;
		String result = null;
		Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.final_motor);
		
		for(int i =canvas.getHeight()/2; i >=-2000; i=i-15){          //for drawing grid lines
			canvas.drawLine(-2000,i,canvas.getWidth()+2000,i,paint);
		}
		for(int i =canvas.getHeight()/2+15; i <=canvas.getHeight()+2000; i=i+15){
			canvas.drawLine(-2000,i,canvas.getWidth()+2000,i,paint);
		}
		for(int i =canvas.getWidth()/2+15; i <=canvas.getWidth()+2000; i=i+15){
			canvas.drawLine(i,canvas.getHeight()-2000,i,canvas.getHeight()+2000,paint);
		}
		for(int i =canvas.getWidth()/2; i >=-2000; i=i-15){
			canvas.drawLine(i,canvas.getHeight()-2000,i,canvas.getHeight()+2000,paint);
		}
		

        if(moving){											//if scrolled
			canvas.translate(eventX-canvas.getWidth()/2,eventY-canvas.getHeight()/2);
			Bitmap image=BitmapFactory.decodeResource(getResources(), R.drawable.latest8051_1);
	      
			originX = eventX-canvas.getWidth()/2;
			originY=eventY-canvas.getHeight()/2;
			canvas.drawBitmap(image, 317, 55,null);

			}
			
			else{
				canvas.translate(originX, originY);
				Bitmap image=BitmapFactory.decodeResource(getResources(), R.drawable.latest8051_1);
		        canvas.drawBitmap(image, 317, 55,null);

			}
        Bitmap fixedcircuit=BitmapFactory.decodeResource(getResources(), R.drawable.fixed_circuit1);
        
    	if (Circuit.component != null){									//storing images in array
			if(Circuit.component[0]==null && Circuit.component[1]==null && Circuit.component[2]==null && Circuit.component[3]==null){
				canvas.drawBitmap(fixedcircuit, 0,50, null);
			}
			else{
				canvas.drawBitmap(fixedcircuit, -100,50, null);
			}
			for (int k = 0; k<4; k++){
				if(Circuit.component[k]!=null){
					if(Circuit.component[k].getImage()!=null){
						switch(k){
						case 0 :canvas.drawBitmap(Circuit.component[k].getImage(),398 ,76, null);  //122,32
								break;
						case 1: canvas.drawBitmap(Circuit.component[k].getImage(),32,60, null);    //7,55
					    		break;
						case 2:
							if(Circuit.component[k].getId()==1)
							canvas.drawBitmap(Circuit.component[k].getImage(), 397,139, null);
							else if(Circuit.component[k].getId()==3)
							canvas.drawBitmap(Circuit.component[k].getImage(), 398,239, null);
							else if(Circuit.component[k].getId()==4)
							canvas.drawBitmap(Circuit.component[k].getImage(), 398,242, null);
							break;
						case 3:
							if(Circuit.component[k].getId()==1)
							canvas.drawBitmap(Circuit.component[k].getImage(), 32,94, null);
							else
							canvas.drawBitmap(Circuit.component[k].getImage(), 32,197, null);
							
					    break;
					    default:
						}//end of inner if
					}
			}//end of main if
				
			
        
            for(int e=0;e<4;e++){
            	if(Circuit.port_value[e]!=null && Circuit.component[e]!=null){		//changed
            		
            		switch(Circuit.component[e].getId())
		            	{
		            	
		            	case 1:  
		            		result = Circuit.port_value[e];//for animations  Led Circuit
		            		System.out.println(result);
		            		switch(e)
		            		{
		            		case 0: posX=530;
		            				posY=287;
		            				break;
		            			
		            		case 1: posX=192;
		            				posY=271;
		            				break;
		            		case 2: posX=530;
		            				posY=368;
		            				result = new StringBuffer(Circuit.port_value[2]).reverse().toString();
		            				break;
		            		case 3:posX= 192;
		            				posY=320;
		            				break;
		            		default: 
		            		}//end of inner switch
            		 for(int i=0;i<result.length();i++){
            	        if(result.charAt(i)=='1'){
            	        	this.paint1.setAntiAlias(true);
            	        	this.paint1.setMaskFilter(this.theBlur);
            	        	canvas.drawCircle(posX,posY-28*i, 6, this.paint1);
            	        	}
            	        }
            		
            		break;
            	case 2:                    //code for animations on 7 segment circuit
            		
            		posX=0;
            		posY=0;
            		switch(e)
            		{
            		case 0:
            			posY=181; 
            			if(Circuit.port_value[2].charAt(0)=='1'){
            				posX=448;
            			}
            			else if(Circuit.port_value[2].charAt(1)=='1'){
            				posX=520; 
            			}
            			else if(Circuit.port_value[2].charAt(2)=='1'){
            				posX=591;
            			}
            			else if(Circuit.port_value[2].charAt(3)=='1'){
            				posX=660;
            			}
            			result=Circuit.port_value[0];
            			break;
            		case 1:
            			posY=165; 
        				if(Circuit.port_value[3].charAt(5)=='1'){
        					posX=258;
        				}
        				else if(Circuit.port_value[3].charAt(4)=='1'){
        					posX=187;
        				}
        				else if(Circuit.port_value[3].charAt(3)=='1'){
        					posX=115;
        				}
        				else if(Circuit.port_value[3].charAt(2)=='1'){
        					posX=46;
        				}
        				result=Circuit.port_value[1];
            			
            			break;
            		default:
            		}
            		if(((!Circuit.port_value[3].equals("00000000")) && (posY==165))||((!Circuit.port_value[2].equals("00000000"))&&(posY==181))){
            			if(result.charAt(0)=='1'){ //for h
            		//canvas.drawLine(, startY, stopX, stopY, paint);
            			}
            			if(result.charAt(1)=='1'){ //for g
            				canvas.drawLine(posX, posY-40, posX+18, posY-40, paint3);  
            			}
            			if(result.charAt(2)=='1'){ //for f
            				canvas.drawLine(posX, posY-40, posX, posY-80, paint3);
            			}
            		
            			if(result.charAt(3)=='1'){  // for e
            				canvas.drawLine(posX, posY, posX, posY-40, paint3);
            			}
            		
            			if(result.charAt(4)=='1') {//for d
            				canvas.drawLine(posX, posY, posX+18, posY, paint3);
            			}
            		
            			if(result.charAt(5)=='1') {//for c
            				canvas.drawLine(posX+18, posY, posX+18, posY-40, paint3);
            			}
            		
            			if(result.charAt(6)=='1'){ //for b
            				canvas.drawLine(posX+18, posY-40, posX+18, posY-80, paint3);
            			}
            		
            			if(result.charAt(7)=='1'){ //for a
            				canvas.drawLine(posX, posY-77, posX+18, posY-77, paint3);
            			
            			}
            		}
            		
            		
            		
            		break;
            		
            	case 3: 
            		float px = 0;
            		float py = 0;
            		
            		result=Circuit.port_value[e].substring(4, 8);//for stepper motor
            		switch(e)
            		{
            		case 0:
            			px = 616;
            			py = 115;
            			break;
            		case 1:

            			px = 108;
            			py = 98;
            			break;
            		case 2:
            			px = 616;
            			py = 338;
            			break;
            		case 3:
            			px = 108 ;
            			py = 238 ;
            			break;
            		}
            			result= Integer.toHexString(Integer.parseInt(result,2));
         				angle = 0;
                		if(result.equals("c")){
                			angle=90;
                			//code for 90
            			}
                		else if(result.equals("6")){
            				angle=180;
            				//code for 180
            			}
            			else if(result.equals("3")){
            				angle=270;
            				//code for 270
            			}
            			else if(result.equals("9")){
            				angle=360;
            				//code for 360
            			}
            			else if(result.equals("8")){
            				angle=45;
            				//code for 45
            			}
            			
            			else if(result.equals("4")){
            				angle=135;
            				//code for 135
            			}
            			else if(result.equals("2")){
            				angle=225;
            				//code for 225
            			}
            			else if(result.equals("1")){
            				angle=315;
            				//code for 315
            			}
//                		
                		Matrix matrix = new Matrix();
                		matrix.postTranslate(-bitmap1.getWidth()/2, -bitmap1.getHeight()/2);
                		matrix.postRotate(angle);
                		matrix.postTranslate(px, py);
                		canvas.drawBitmap(bitmap1, matrix, null);
                		break;
                		
            	case 4:                                       //code for dac circuit
            		result=Circuit.port_value[e]; 
            		if (e == 2)
            				result=new StringBuffer(result).reverse().toString();
            		iout = 0;
            		double temp = 0;
            		for(int i=0;i<8;i++){
            			if(result.charAt(i)=='1'){
            				temp = Math.pow(2.0,i+1);
            				iout+=1.0/temp;
            			}
            		}
            		iout=2*iout;
            		vout=5*iout;
            		VoltageOutput.setValues(vout,Execute.timeline/12);
            		try{
            			oscilloscopeActivity.og.invalidate();
            		}catch(Exception exp){
            			
            		}
            		break;
            		
            	default:
            	}
            }
           }
		}
	}
   }
	

	@Override
	  public boolean onTouchEvent(MotionEvent event) {
		
	   float getx,gety;
	   getx=event.getX();
	   gety=event.getY();

		return gDetector.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//moving=false;
		
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

	@Override//USED TO TRANSLATE
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		moving=true;		
		eventX=e2.getX();
		eventY=e2.getY();
		
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
