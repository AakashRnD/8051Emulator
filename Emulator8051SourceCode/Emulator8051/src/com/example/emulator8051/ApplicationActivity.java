/*
 * Description: This class implements the tab host and sliding drawer in the appplication

 * Author: Alekhya T.M.S.K.    Email id: tmsk.alekhya@gmail.com
 * Author: Anupam Sanghi	   Email id: sanghi.anupam@gmail.com
 * 			
 * 
 */




package com.example.emulator8051;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationActivity  extends TabActivity {
    /** Called when the activity is first created. */
	static TabHost tabHost;						//declaring  UI variables
	TextView project;
	
	final Context context =this;
	static boolean isSaved=false;
	static boolean isNew=true;
	static String project_name;
	static Execute execute;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_launcher);
         tabHost = getTabHost();
         project=(TextView)findViewById(R.id.textView1);
         tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
           public void onTabChanged(String arg0) {							//to change color of tab when selected
            	setSelectedTabColor();
           
           }     
     });  
        
        // Tab for Workbench
        TabSpec workbench = tabHost.newTabSpec("WORKBENCH");
        workbench.setIndicator("WORKBENCH");
        Intent workbenchIntent = new Intent(this, WorkBench.class);
        workbench.setContent(workbenchIntent);
     
        
        // Tab for Editor
        TabSpec editor = tabHost.newTabSpec("EDITOR");
        // setting Title and Icon for the Tab
        editor.setIndicator("EDITOR");
        Intent editorIntent = new Intent(this, Editor.class);
        editor.setContent(editorIntent);
        
        // Tab for Internals
        TabSpec internals = tabHost.newTabSpec("INTERNALS 8051");
        internals.setIndicator("INTERNALS 8051");
        Intent internalIntent = new Intent(this,Internals8051.class);
        internals.setContent(internalIntent);
        
        TabSpec help = tabHost.newTabSpec("HELP");   //for help
        help.setIndicator("HELP");
        Intent helpIntent = new Intent(this,Help.class);
        help.setContent(helpIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(workbench); 
        tabHost.addTab(editor); 
        tabHost.addTab(internals);
        tabHost.addTab(help);
       
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                // reduce height of the tab
                view.getLayoutParams().height *= 0.66;

                //  get title text view
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                    // just in case check the type

                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                   
                }
            }
        }
        if(openActivty.set){
        	setOpen();
        }
    }
   
    //loading the circuit and code of item selected goes here
    public void setOpen(){
    	Bundle extras = getIntent().getExtras();   //for retrieving filename when item selected in open
		if (extras != null) {
			String path = extras.getString("projectname");
		    project_name=path;
		    isSaved=true;
		    isNew=false;
		    project.setText(path);
		  
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				try {
					String circuitname = "/sdcard/Emulator_8051/".concat(path+"/Circuit.sch");
					String codename = "/sdcard/Emulator_8051/".concat(path+"/Code.asm");
					File f = new File(circuitname);
					FileReader fr = new FileReader(circuitname);
					BufferedReader br = new BufferedReader(fr);
					String line;
					int id = 0;
					Bitmap image = null;
					line = br.readLine();
					System.out.println("inside for......rgdf");
					if(line!=null){
					for(int i = 0;i<line.length();i++){
						System.out.println("inside for..................");
						//WorkBench.spinners[i].setSelection(Character.getNumericValue(line.charAt(i)));   //need to change
						 if(line.charAt(i)=='0'){
								 id=0;
								 image = null;
						}
					
						 else if(line.charAt(i)=='1'){
								 id = 1;
								  switch(i){
									case 0:  
									image = BitmapFactory.decodeResource(getResources(), R.drawable.ledcircuit_0);
									break;
									case 1: image = BitmapFactory.decodeResource(getResources(), R.drawable.ledcircuit_1);break;
									case 2: image = BitmapFactory.decodeResource(getResources(), R.drawable.ledcircuit_port2);break;
									case 3: image = BitmapFactory.decodeResource(getResources(), R.drawable.ledcircuit_port3);break;
									default: image = null;
								 }
					   }
						else if(line.charAt(i)=='2') {
								 id = 2;
								 if(i == 0) image = BitmapFactory.decodeResource(getResources(), R.drawable.s7segementled);
									else image = BitmapFactory.decodeResource(getResources(), R.drawable.s7segementled_mirror);
									
						}
						else if(line.charAt(i)=='3'){
								 id = 3;
								 switch(i){
									case 0: image = BitmapFactory.decodeResource(getResources(), R.drawable.sm0);break;
									case 1: image = BitmapFactory.decodeResource(getResources(), R.drawable.sm13);break;
									case 2: image = BitmapFactory.decodeResource(getResources(), R.drawable.sm2);break;
									case 3: image = BitmapFactory.decodeResource(getResources(), R.drawable.sm13);break;
									default: image = null;
									}
						}
						else if(line.charAt(i)=='4') {
								 id = 4;
								 if(i == 0 ||i==2) image = BitmapFactory.decodeResource(getResources(), R.drawable.dac_circuit1);
									else image = BitmapFactory.decodeResource(getResources(), R.drawable.dac_circuit_mirror1);
							 }
							 Circuit.component[i] = new Component(id,image);
						}
						 
						WorkBench.board.invalidate();
						br.close();
						fr.close();
					}
						System.out.println("inside for..6585");
					 f = new File(codename);
					 fr = new FileReader(codename);
					 br = new BufferedReader(fr);
					 Editor.asmcode="";
					 System.out.println("before while");
					 while ((line = br.readLine()) != null){
						 System.out.println("inside while");
						 Editor.asmcode=Editor.asmcode+"\n"+line;
						 
					 }
					 System.out.println("outside while");
					 
					 if(!Editor.asmcode.equals("")){
						 System.out.println("inside iffff");
						 Editor.textarea.setText(Editor.asmcode);
						 
					 }
					 else{
						 System.out.println("inside else");
						 
						 Editor.textarea.setText(" ");
					 }
					 System.out.println("outside else");
					 
					 br.close();
					 System.out.println("1111111111");
					 
					fr.close();
					System.out.println("222222222222222222222");
					 
					System.out.println("inside for..khuy869");
					
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"No Experiments are saved", Toast.LENGTH_SHORT).show();
				}
		   }
		}
       
    }
    
     @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

    
    /*to set the tab color when touched*/
    private void setSelectedTabColor() {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
         tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.BLACK);  
         }  
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.rgb(0,153,204)); //246,149,38  0.200.200
      }
    
    /*function of new button goes here*/
    public void newClick(View v){
    	if(isNew){
    		
    	}
    	else{
    	if(!isSaved){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
		    .setTitle("Do you want to save the existing file")
			.setCancelable(false)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id){
							saveProject();
						}
					})
		   .setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						reload();
						dialog.cancel();
				}
		   });
			AlertDialog alertDialog = alertDialogBuilder.create();
	
			// show it
			alertDialog.show();
		}
    	else{
    		reload();
    	}
     }
    	
 }
    public void reload(){
    	//project_name=null;
    	//WorkBench.circuit=null;
    	isSaved=false;
    	isNew=true;
    	project.setText("untitled project");    //change 1
    	
    	try{
    		project_name=null;
        	WorkBench.circuit=null;
    		Editor.textarea.setText("");
    		Editor.total_memory.setText("");
    		Editor.logcat.setText("");
    		Editor.opcode.setText("");
    		Editor.asmcode=null;
    	}catch(Exception e){
    	}
    	
    	for(int i=0;i<Circuit.component.length;i++){
    		Circuit.component[i]=null;
    	}
    	
    	//for(int i = 0; i< 4;i++)
		//WorkBench.spinners[i].setSelection(0);
    	
    	//to set none when new is called and no is selected to save
    	WorkBench.board.invalidate();
    }
    /*function of open button goes here*/
    public void openClick(View v){
    	if(!isSaved && !isNew){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
		    .setTitle("Do you want to save the existing file")
			.setCancelable(false)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							saveProject();
							}
			    })
		   .setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					Intent i = new Intent(ApplicationActivity.this, openActivty.class);
					startActivity(i);
							}
				});
	
			// create alert dialog
		   	AlertDialog alertDialog = alertDialogBuilder.create();
	
		   	// show it
		   	alertDialog.show();
		}
		else{
			Intent i1 = new Intent(ApplicationActivity.this,openActivty.class);
			startActivity(i1);
		}
	 }
    
    public void saveClick(View v){
    	saveProject();
    }
    public void saveProject(){
    	  String content = "";
		  for (int k = 0; k < Circuit.component.length;k++){
			  
			   if(Circuit.component[k]!=null)
				   content = content+Circuit.component[k].getId();
		   }
		  final String toWrite = content;
    	
    	  LayoutInflater li = LayoutInflater.from(context);
		  View promptsView = li.inflate(R.layout.prompts, null);
		  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			 // set prompts.xml to alertdialog builder
		  alertDialogBuilder.setView(promptsView);
		  final EditText userInput = (EditText) promptsView.findViewById(R.id.fileNameInput);
    	  if(isSaved){
    		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);
    		alertDialogBuilder1
       	    .setTitle("file already saved")
       		.setCancelable(false)
       		.setPositiveButton("ok",
       				new DialogInterface.OnClickListener() {
       					public void onClick(DialogInterface dialog,int id) {
       						dialog.cancel();
       					}
       			});
    		// create alert dialog
    		AlertDialog alertDialog = alertDialogBuilder1.create();

    		// show it
    		alertDialog.show();
    		
    	}//end of if
    	
    	else{
    		if(!isSaved && project_name != null) userInput.setText(project_name);
    		
    		 alertDialogBuilder
 			.setCancelable(false)
 			.setPositiveButton("OK",
 					new DialogInterface.OnClickListener() {
 						public void onClick(DialogInterface dialog,int id) {
 							// get user input and set it to result
 							if(userInput.getText().toString().length()>0){
 							//checking for sdcard
 								String state = Environment.getExternalStorageState();
 								 if (Environment.MEDIA_MOUNTED.equals(state)) {
 									String path="sdcard/Emulator_8051/"+userInput.getText().toString();
 									File direct = new File(path);
									final String filepath= "/sdcard/Emulator_8051/"+userInput.getText().toString().concat("/Circuit.sch");
									final String filepath1= "/sdcard/Emulator_8051/"+userInput.getText().toString().concat("/Code.asm");
 									
 									   if(!direct.exists()){
 									   		if(direct.mkdirs()){
 									   			try{
 									    		   	direct.createNewFile();
 									    		   	File f=new File(filepath);
 		 						                    f.createNewFile();
 		 						                    FileWriter fw = new FileWriter(filepath);
 		 							                BufferedWriter br= new BufferedWriter(fw);
 		 							                isSaved=true;
 		 							                isNew=false;
 		 							                 project_name =userInput.getText().toString();
 		 							                 project.setText(project_name);
 		 							                 br.write(toWrite);
 		 							                 Toast.makeText(getApplicationContext(), "Experiment Saved Successfully", Toast.LENGTH_SHORT).show();
 		 							                 br.close();
 		 							                 fw.close();
 		 							                 f=new File(filepath1);
 		 							                 fw = new FileWriter(filepath1);
 		 							                  br= new BufferedWriter(fw);
 		 							                  br.write(Editor.textarea.getText().toString());
 									                  br.close();
 									                  fw.close();
 		 						                      }catch(Exception e){
 									           }
 									   		}
 									   }
 									   
 									   else{
 										   		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 										   		alertDialogBuilder
											     .setTitle("file already exists do you want to repalce it?")
											     .setCancelable(false)
											     .setPositiveButton("Yes",
											        new DialogInterface.OnClickListener() {
											              public void onClick(DialogInterface dialog,int id) {
											                   						try{
											                   							 FileWriter fw = new FileWriter(filepath);
								 		 							                      BufferedWriter br= new BufferedWriter(fw);
								 		 							                      br.write(toWrite);
								 		 							                      Toast.makeText(getApplicationContext(), "Experiment Saved Successfully", Toast.LENGTH_SHORT).show();
								 		 							                      br.close();
								 		 							                      fw.close();
								 		 							                      fw = new FileWriter(filepath1);
								 		 							                      br= new BufferedWriter(fw);
								 		 							                      isSaved=true;
								 		 							                      isNew=false;
								 									                      project_name=userInput.getText().toString();
								 									                      project.setText(project_name);
								 									                      br.write(Editor.asmcode);
								 									                      br.close();
								 									                      fw.close();
								 		 						                       
											                   						}catch(Exception e){
											                   						}
											                   		
											                   					}
											                   					})
											                   	   .setNegativeButton("No",new DialogInterface.OnClickListener() {
											                   			public void onClick(DialogInterface dialog,int id) {
											                   				dialog.cancel();
											                   			}
											                   		});
								
											                   // create alert dialog
											                   AlertDialog alertDialog = alertDialogBuilder.create();
								
											                   // show it
											                   alertDialog.show();
									 }
 							   }
 							}
 							else{
 								Toast.makeText(getApplicationContext(), "Nofile name , please fill the name", Toast.LENGTH_LONG).show();
 							}
 						}
 						
 					})
 			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
 						public void onClick(DialogInterface dialog,int id) {
 							dialog.cancel();
 						}
 					});
 	
    		 // create alert dialog
 		   AlertDialog alertDialog = alertDialogBuilder.create();

 		   // show it
 		   alertDialog.show();
 			}
    		
    	}
    	
    	
   }

   
