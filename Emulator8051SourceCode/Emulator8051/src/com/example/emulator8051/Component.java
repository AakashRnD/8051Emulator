/*
 * Description: This class implements the constructors required to fetch and set values
 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */

package com.example.emulator8051;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Component extends Activity {
	int id;
	Bitmap image;

	public Component(int id, Bitmap image) // declaring constructor
	{
		this.id = id;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public Bitmap getImage() {
		return image;
	}

}
