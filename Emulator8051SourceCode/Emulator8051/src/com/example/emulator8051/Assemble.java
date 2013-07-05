/*
 * Description: This class implements the instruction set 0f 8051 microcontroller and convert them to opcodes
 * Author: Prayasee Pradhan    	   Email id: prayasee91@gmail.com
 * Author: Priyanka Padmanabhan	   Email id: priyanka.91092@gmail.com
 * 			
 * 
 */



package com.example.emulator8051;

import java.util.Arrays;
 
public class Assemble {

	static int pc=0;// initialize pc address to 0
	static String[] rom = new String[4096]; // for 4k memory 
	static String[][] label_string = new String[100][2];
	static String[][] module_address= new String[100][2];
	static int starting_address=0;
	static int ending_address=0;
	static String end_add="";
	static String err_msg="";
	static int linenumber=0;
	static int ctr=0,endctr=0;
	static int label_count=0;
	static int total_mem = 0;
	static int x=0;
	static boolean isAssemble=false;
	static boolean flag = true;
	
	public static void assembleFunction() {
		
		pc=0;// initialize pc address to 0
		starting_address=0;
		ending_address=0;
		end_add="";
		err_msg="";
		linenumber=0;
		ctr=0;endctr=0;
		label_count=0;
		total_mem = 0;
		String text ="";
		String[] asm_split;
		String[][] equ_string = new String[100][2];
		String[] equ_split= new String[3];
		//String[] subsplit= new String[4];
		int dbctr=0;
		int n=0;
		
		for(int i=0;i<4096;i++){
			rom[i]= "0";
		}
		
		
		asm_split= Editor.asmcode.split("\n");
		for(int i=0;i<asm_split.length;i++) {
			linenumber++;
			if(asm_split[i].equals("")){
			}
			else{
				System.out.println(asm_split[i]);
				//asm_split[i]= asm_split[i].;
				asm_split[i] = asm_split[i].trim().toUpperCase();
				
				if(asm_split[i].contains("EQU")){
					equ_split = asm_split[i].split(" ");
					equ_string[n][0]= equ_split[0];
					equ_string[n][1]= equ_split[2];
					n++;
				}
				else if(asm_split[i].contains("DB")){
					equ_split = asm_split[i].split(" ");
					if(equ_split[2].endsWith("H")){
						equ_split[2]= equ_split[2].substring(0, equ_split[2].length()-1);
					}
					rom[Integer.parseInt(equ_split[0], 16)]= equ_split[2];
					dbctr++;
				}
				else{
					for(int k=0;k<n;k++){
						if(asm_split[i].contains(equ_string[k][0])){
							asm_split[i]= asm_split[i].replace(equ_string[k][0], equ_string[k][1]);
						}
					}
					String[] splitters = splitForDelimeter(asm_split[i]);
					for (int m = 0; m < splitters.length; m++) {
						splitters[m] = splitters[m].trim();
					}
					
					if(!splitters[1].contains(" ")&&(!(splitters[1].equals("NOP") || splitters[1].equals("END")))){
						err_msg+="Syntax Error in line number"+linenumber +"\n";
					}
					else{
					String[] subsplit = splitters[1].split(" ");
					for (int j = 0; j < subsplit.length; j++) {
						subsplit[j] = subsplit[j].trim();
						if(subsplit[j]!=""&&j!=0)
						{
							subsplit[1]=subsplit[j];
						}
					}
					
					firstPassAssemble(splitters, subsplit);
				}
				}
			
			}
			text += asm_split[i] + "\n"  ;
		}
		
		Editor.textarea.setText(text);
		
		//If there is no org and no end
				if(ctr==0 && endctr==0){
					module_address[0][0]= Integer.toHexString(starting_address);
					module_address[0][1]= Integer.toHexString(pc);
					ctr++;
				}//If there is no org and one end
				if(ctr==0 && endctr==1){
					module_address[0][0]= Integer.toHexString(starting_address);
					ctr++;
				}
				else{
					module_address[ctr-1][1]= Integer.toHexString(pc);
				}
				// Find the ending address of main module.. 
				ending_address = Integer.parseInt(module_address[0][1],16);
				end_add = Integer.toHexString(ending_address);
				int p = end_add.length();
				for(int i=0;i<4-p;i++){
					end_add = "0".concat(end_add);
				}
				secondPassAssemble(rom, starting_address,pc, label_string,label_count );
		
	}
	
	public static void secondPassAssemble(String[] rom, int starting_address, int pc,
			String[][] label_string, int label_count) {
		
		String address ="";
		String bin_val = "";
		String rel_add;
		int l=0;
	
		// 2 byte jump instructions having relative address
		String[] opcode2rel = new String[]{ "40","50", "60",  "70", "80", "D8", "D9", "DA", "DB" ,"DC", "DD", "DE", "DF"};
		// 3 byte jump instructions having relative address like CJNE
		String[] opcode3rel = new String[]{"10", "20", "30", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "D5"};
		// 2 Byte AJMP or ACALL absolute 11 adress instructions 
		String[] opcode2a = new String[]{"01","11","21","31","41", "51", "61", "71", "81", "91", "A1", "B1", "C1", "D1","E1", "F1"};
		
		for(int m=0;m<ctr;m++){
			
			total_mem += Integer.parseInt(module_address[m][1],16) - Integer.parseInt(module_address[m][0],16);
			
			for(int i= Integer.parseInt(module_address[m][0], 16); i<Integer.parseInt(module_address[m][1], 16); i++){
				if(rom[i].contains("$")){
					
					String[] add_split = rom[i].split("[$]");
					if(Arrays.asList(opcode2rel).contains(add_split[0])|| Arrays.asList(opcode3rel).contains(add_split[0])){
						int temp;
						
						if(Arrays.asList(opcode2rel).contains(add_split[0])){
							l= i+1;
							temp = i+2;
						}
						else{
							l= i+2;
							temp= i+3;
						}
						
						rom[i]= add_split[0];
						for(int p=0;p<label_count;p++){
							if(label_string[p][1].equals(add_split[1])){
								address= label_string[p][0];
							}
						}
						
						int label_add = Integer.parseInt(address, 16);
						if((label_add - temp)>=0){
							bin_val = Integer.toBinaryString(label_add - temp);
							if(bin_val.length()<8){
								int p = 8- bin_val.length();
								for(int n=0; n<p;n++){
									bin_val= "0".concat(bin_val);
								}
							}
							 
							rel_add = Integer.toHexString(Integer.parseInt(bin_val,2));
						}
						else{
							bin_val = Integer.toBinaryString(256-(temp - label_add));
							rel_add = Integer.toHexString(Integer.parseInt(bin_val,2));
						}
						rom[l]= rel_add;
					}
					
					else if(Arrays.asList(opcode2a).contains(add_split[0])){
						rom[i]= add_split[0];
						for(int p=0;p<label_count;p++){
							if(label_string[p][1].equals(add_split[1])){
								address= label_string[p][0];
							}
						}
						
						if(address.length()<4){
							int p = 4 -address.length();
							for(l=0;l<p;l++){
						
								address = "0".concat(address);
							}
						}
						
						String address_2 = address.substring(2, 4);
						bin_val = Integer.toBinaryString(Integer.parseInt(address,16));
						if(bin_val.length()<16){
							int p = 16 -bin_val.length();
							for(l=0;l<p;l++){
								bin_val = "0".concat(bin_val);
							}
						}
						
						if(rom[i].equals("01")){
							bin_val = bin_val.substring(5,8).concat("00001");
						}
						else if(rom[i].equals("11")){
							bin_val = bin_val.substring(5,8).concat("10001");
						}
						rom[i] = Integer.toHexString(Integer.parseInt(bin_val,2));
						rom[i] = rom[i].toUpperCase();
						rom[i+1] = address_2;
					}
					
					// For LCALL or LJMP 3 byte instructions
					else {
						rom[i]= add_split[0];
						for(int p=0;p<label_count;p++){
							if(label_string[p][1].equals(add_split[1])){
								address= label_string[p][0];
							}
						}
						
						if(address.length()<4){
							int p = 4 -address.length();
							for(l=0;l<p;l++){
								address = "0".concat(address);
							}
						}
						
						String address_1 = address.substring(0, 2);
						String address_2 = address.substring(2, 4);
						
						rom[i+1]= address_1;
						rom[i+2]= address_2;
					}
				}
			} 
		}
		
		if(err_msg.equals("")){
			String value = "";
			for(int m=0;m<ctr;m++){
	        	for(int i= Integer.parseInt(module_address[m][0], 16); i<Integer.parseInt(module_address[m][1], 16); i++){
	        		
	        		bin_val = Integer.toHexString(i);
	        		int len = bin_val.length();
	        		for(int k=0;k<4-len;k++){
	        			bin_val = "0".concat(bin_val);
	        		}
	        		
	        		len = rom[i].length();
	        		for(int k=0;k<2-len;k++){
	        			rom[i] = "0".concat(rom[i]);
	        		}
	        		rom[i] = rom[i].toUpperCase();
	        		value = value + bin_val + "    " + rom[i] + "\n";
	        	}
	        }
		  isAssemble=true;
			Editor.total_memory.setText(Integer.toString(total_mem) + "  bytes");
			Editor.opcode.setText(value);
			Editor.logcat.setText("Compiled successfully!!!");
		}
		else{
			Editor.opcode.setText("");
			err_msg= "Program could not be assembled"+ "\n" + err_msg;
			Editor.logcat.setText(err_msg);
			Editor.total_memory.setText("");
			
		//	Execute.executeFuction();
		}
	}

	public static String[] splitForDelimeter(String str) {
		
		if (str.contains(":")) {
			if (str.contains(",")) {

			} else {
				str = str.concat(",0");

			}
		} 
		else {
			if (str.contains(",")) {
				str = "0:".concat(str);
			} 
			else {
				str = "0:".concat(str);
				str = str.concat(",0");
			}

		}
		
		if(str.contains(";")){
			int p= str.indexOf(";");
			str= str.substring(0, p);
		}
		String[] splitters = str.split("[:\\,]");
		return splitters;
	}

	public static void firstPassAssemble(String[] splitters, String[] subsplit) {
		
		String hex="";
		String code="";// for hex code of each instruction
		int temporary_pc= pc;
		
		if(!splitters[0].equals("0")){
			hex = Integer.toHexString(pc);
			label_string[label_count][0] = hex;
			label_string[label_count][1] = splitters[0];
			label_count++;
		}
		
		// ORG address starts here...
					if(subsplit[0].equals("ORG") && subsplit[1].endsWith("H") && !subsplit[1].startsWith("#")){
						System.out.println("BEGINNING OF ORG : " + ctr);
						x++;
						subsplit[1] = subsplit[1].substring(0,subsplit[1].length()-1 );
						if(ctr==0){
							if(pc==0){
								starting_address = Integer.parseInt(subsplit[1],16);
								module_address[0][0]= subsplit[1];
								pc = starting_address;
								ctr++;
								pc--;
							}
							else{
								module_address[0][0]= Integer.toHexString(starting_address);
								module_address[0][1]= Integer.toHexString(pc);
								ctr++;
								pc = Integer.parseInt(subsplit[1],16);
								module_address[1][0]= Integer.toHexString(pc);
								ctr++;
								pc--;
							}
						}
						else if(ctr==1 | (ctr==2 && endctr==1) & flag){
							System.out.println("INSIDE THE REQUIRED ORG LOOP");
							if(ctr==2 && endctr==1){flag=false;}
							else {
								ctr++;
								module_address[0][1]= Integer.toHexString(pc);
							}
							pc = Integer.parseInt(subsplit[1],16);
							module_address[1][0]= Integer.toHexString(pc);
							pc--;
						}
						else{
							module_address[ctr-1][1]= Integer.toHexString(pc);
							pc = Integer.parseInt(subsplit[1],16);
							module_address[ctr][0]= Integer.toHexString(pc);
							ctr++;
							pc--;
						}
						
						System.out.println("end OF ORG : " + ctr);
					}// ORG instruction ends here...
					
					//END structure starts here...
					else if(splitters[1].equals("END")){
						System.out.println("BEGINNING OF END");
						x++;
						if(ctr==0){
							module_address[0][0]= Integer.toHexString(starting_address);
							module_address[0][1]= Integer.toHexString(pc);
							ctr++;
							module_address[1][0]= Integer.toHexString(pc);
							ctr++;
						}
						else if (ctr==1){
							module_address[0][1]= Integer.toHexString(pc);
							module_address[1][0]= Integer.toHexString(pc);
							ctr++;
						}
						pc--;
						endctr++;
						System.out.println("END OF END");
					}// END structure ends here...


			
			// NOP instruction starts here..
			else if (subsplit[0].equals("NOP")) {
				code = "00";
				rom[pc] = code;
			} // NOP instruction ends here...
			
			// code for AJMP starts here...
			else if(subsplit[0].equals("AJMP")){
				code = "01";
				 rom[pc] = code.concat("$").concat(subsplit[1]);
				 pc++;
			}// code for AJMP ends here.......
			
			// code for LJMP starts here...
			else if(subsplit[0].equals("LJMP")){
				 code = "02";
				 rom[pc] = code.concat("$").concat(subsplit[1]);
				 pc+=2;
			}// code for LJMP ends here....... 
			
			// RR Rotate right function
			else if(splitters[1].equals("RR A")){
				code ="03";
				rom[pc] = code;
			}// RR function ends here
			
			// INC instruction starts here..
			else if (subsplit[0].equals("INC")) {
				if (subsplit[1].equals("A")) {
					code = "04";
					rom[pc] = code;
				} 
				else if (subsplit[1].endsWith("H")){
					code = "05";
					subsplit[1] = subsplit[1].substring(0, subsplit[1].length()-1);
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
				} 
				else if (subsplit[1].startsWith("@")) {
					subsplit[1] = subsplit[1].substring(1);
					if (subsplit[1].equals("R0")) {
						code = "06";
					} 
					else if (subsplit[1].equals("R1")) {
						code = "07";
					}
					rom[pc] = code;
				} 
				else if (subsplit[1].startsWith("R")) {
					if (subsplit[1].equals("R0")) {
						code = "08";
					} else if (subsplit[1].equals("R1")) {
						code = "09";
					} else if (subsplit[1].equals("R2")) {
						code = "0A";
					} else if (subsplit[1].equals("R3")) {
						code = "0B";
					} else if (subsplit[1].equals("R4")) {
						code = "0C";
					} else if (subsplit[1].equals("R5")) {
						code = "0D";
					} else if (subsplit[1].equals("R6")) {
						code = "0E";
					} else if (subsplit[1].equals("R7")) {
						code = "0F";
					}
					rom[pc] = code;
				}
				else if (subsplit[1].equals("DPTR")){
					code = "A3";
					rom[pc] = code;
				}
			}// INC instruction ends here.. 
			
			// Start of JBC instruction
			else if(subsplit[0].equals("JBC")){
				code = "10";
				rom[pc]= code.concat("$").concat(splitters[2]);
				pc++;
				String add_bit = addressOfBitValue(subsplit[1]);
				rom[pc]= add_bit;
				pc++;
			}// End of JBC instruction
			
			// ACALL instruction starts here...
			else if(subsplit[0].equals("ACALL")){
				code = "11";
				rom[pc]= code.concat("$").concat(subsplit[1]);
				pc++;	
			}// ACALL ends here...
			
			//LCALL instruction starts here...
			else if(subsplit[0].equals("LCALL")){
				code = "12";
				rom[pc]= code.concat("$").concat(subsplit[1]);
				pc+=2;
			}// LCALL ends here..
			
			// RRC function starts here (Rotate through carry)
			else if(splitters[1].equals("RRC A")){
					code ="13";
					rom[pc]= code;
			}//RRC function ends here

			// DEC instruction starts here..
			else if (subsplit[0].equals("DEC")) {
				if (subsplit[1].equals("A")) {
					code = "14";
					rom[pc] = code;
				} 
				else if (subsplit[1].endsWith("H")) {
					code = "15";
					subsplit[1] = subsplit[1].substring(0, subsplit[1].length()-1);
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
				} 
				else if (subsplit[1].startsWith("@")) {
					subsplit[1] = subsplit[1].substring(1);
					if (subsplit[1].equals("R0")) {
						code = "16";
					} 
					else if (subsplit[1].equals("R1")) {
						code = "17";
					}
					rom[pc] = code;
				} 
				else if (subsplit[1].startsWith("R")) {
					if (subsplit[1].equals("R0")) {
						code = "18";
					} else if (subsplit[1].equals("R1")) {
						code = "19";
					} else if (subsplit[1].equals("R2")) {
						code = "1A";
					} else if (subsplit[1].equals("R3")) {
						code = "1B";
					} else if (subsplit[1].equals("R4")) {
						code = "1C";
					} else if (subsplit[1].equals("R5")) {
						code = "1D";
					} else if (subsplit[1].equals("R6")) {
						code = "1E";
					} else if (subsplit[1].equals("R7")) {
						code = "1F";
					}
					rom[pc] = code;
				}
			}// DEC instruction ends here...
			
			// Start of JB instruction
			else if(subsplit[0].equals("JB")){
				code = "20";
				rom[pc]= code.concat("$").concat(splitters[2]);
				pc++;
				String add_bit = addressOfBitValue(subsplit[1]);
				rom[pc]= add_bit;
				pc++;
			}// End of JB instruction
			
			// RET instruction starts here..
			else if (splitters[1].equals("RET")){
				code = "22";
				rom[pc]= code;
			}// RET instruction ends here...
			
			//RL A  starts here...
			else if(splitters[1].equals("RL A")){
				code ="23";
				rom[pc] = code;
			}// RL function ends here
			
			// ADD instruction starts here...
			else if (subsplit[0].equals("ADD") && subsplit[1].equals("A")) {
				if(splitters[2].startsWith("#")){
						if(splitters[2].endsWith("H")){
							splitters[2] = splitters[2].substring(1, splitters[2].length()-1);
						}
						else{
							splitters[2] = splitters[2].substring(1);
						}
					code = "24";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(splitters[2].endsWith("H")){
					code = "25";
					splitters[2] = splitters[2].substring(0, splitters[2].length()-1);
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if (splitters[2].startsWith("@")){
					splitters[2] = splitters[2].substring(1);
					if(splitters[2].equals("R0")){
						code = "26";
					}
					else if (splitters[2].equals("R1")){
						code = "27";
					}
					rom[pc]= code;
				}
				
				else if (splitters[2].startsWith("R")){
					if (splitters[2].equals("R0")) {
						code = "28";
					}else if (splitters[2].equals("R1")) {
						code = "29";
					}else if (splitters[2].equals("R2")) {
						code = "2A";
					}else if (splitters[2].equals("R3")) {
						code = "2B";
					}else if (splitters[2].equals("R4")) {
						code = "2C";
					}else if (splitters[2].equals("R5")) {
						code = "2D";
					}else if (splitters[2].equals("R6")) {
						code = "2E";
					}else if (splitters[2].equals("R7")) {
						code = "2F";
					} 
					rom[pc]= code;
				} 
			}// Instruction for ADD completed here...
			
			// Start of JBC instruction
			else if(subsplit[0].equals("JNB")){
				code = "30";
				rom[pc]= code.concat("$").concat(splitters[2]);
				pc++;
				String add_bit = addressOfBitValue(subsplit[1]);
				rom[pc]= add_bit;
				pc++;
			}// End of JNB instruction
			
			// RETI instruction starts here...
			else if (splitters[1].equals("RETI")){
				code = "32";
				rom[pc]= code;
			}// RETI instruction ends here...
			
			// RLC function starts here (Rotate left through carry)
			else if(splitters[1].equals("RLC A")){
				code ="33";
				rom[pc]= code;
			}//RLC function ends here
			
			// Instruction for ADDC starts here...
			else if (subsplit[0].equals("ADDC") && subsplit[1].equals("A")) {
				if(splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2] = splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2] = splitters[2].substring(1);
					}
					code = "34";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
			
				else if(splitters[2].endsWith("H")){
					code = "35";
					splitters[2] = splitters[2].substring(0, splitters[2].length()-1);
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
					
				else if (splitters[2].startsWith("@")){
						splitters[2] = splitters[2].substring(1);
					if(splitters[2].equals("R0")){
						code = "36";
					}
					else if (splitters[2].equals("R1")){
						code = "37";
					}
					rom[pc]= code;
				}
				
				else if (splitters[2].startsWith("R")){
					if (splitters[2].equals("R0")) {
						code = "38";
					}else if (splitters[2].equals("R1")) {
						code = "39";
					}else if (splitters[2].equals("R2")) {
						code = "3A";
					}else if (splitters[2].equals("R3")) {
						code = "3B";
					}else if(splitters[2].equals("R4")) {
						code = "3C";
					}else if (splitters[2].equals("R5")) {
						code = "3D";
					}else if (splitters[2].equals("R6")) {
						code = "3E";
					}else if (splitters[2].equals("R7")) {
						code = "3F";
					} 
					rom[pc]= code;
				}
			}// Instruction for ADDC ends here... 
			
			//Instruction for JC starts here..
			else if(subsplit[0].equals("JC")){
				code = "40";
				rom[pc] = code.concat("$").concat(subsplit[1]);
				pc++;
			}
			//JC ends here..
			
			//ORL (Bitwise OR) opearion starts here
			else if(subsplit[0].equals("ORL")){
				if(subsplit[1].endsWith("H")&&splitters[2].equals("A")){
					subsplit[1]= subsplit[1].substring(1, subsplit[1].length()-1);
					code = "42";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
				}
				else if(subsplit[1].endsWith("H")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2]= splitters[2].substring(1);	
					}
					
					subsplit[2]= subsplit[2].substring(1, subsplit[2].length()-1);
					code = "43";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
					pc++;
					rom[pc] = splitters[2];
				}

				else if(subsplit[1].equals("A")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2]= splitters[2].substring(1);	
					}
						
					code = "44";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&!splitters[2].startsWith("#")&&splitters[2].endsWith("H")){
					splitters[2]= splitters[2].substring(0, splitters[2].length()-1);
					code = "45";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&splitters[2].startsWith("@")){
					splitters[2]= splitters[2].substring(1);	
					if(splitters[2].equals("R0")){
						code = "46";
					}
					else if(splitters[2].equals("R1")){
						code = "47";
					}
					rom[pc] = code;
				}
					
				else if(subsplit[1].equals("A")){
						if(splitters[2].equals("R0")){
							 code = "48";
						}else if(splitters[2].equals("R1")){
							 code = "49";
						}else if(splitters[2].equals("R2")){
							 code = "4A";
						}else if(splitters[2].equals("R3")){
							 code = "4B";
						}else if(splitters[2].equals("R4")){
							 code = "4C";
						}else if(splitters[2].equals("R5")){
							 code = "4D";
						}else if(splitters[2].equals("R6")){
							 code = "4E";
						}else if(splitters[2].equals("R7")){
							 code = "4F";
						}
						rom[pc] = code;
				}

				else if(subsplit[1].equals("C")){
					if(splitters[2].startsWith("/")){
						splitters[2] = splitters[2].substring(1);
						code = "A0";
					}
					else{
						code = "72";	
					}
					
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(splitters[2]);
					rom[pc]= add_bit;
				}
			}// End of ORL instructions
			
			// Start of JNC instruction
			else if(subsplit[0].equals("JNC")){
				code = "50";
				rom[pc]= code.concat("$").concat(subsplit[1]);
			}// End of JNC instruction
			
			//ANL (Bitwise AND) opearion starts here
			else if(subsplit[0].equals("ANL")){
				if(subsplit[1].endsWith("H")&&splitters[2].equals("A")){
					subsplit[1]= subsplit[1].substring(1, subsplit[1].length()-1);
					code = "52";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
				}
				
				else if(subsplit[1].endsWith("H")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
							splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
							splitters[2]= splitters[2].substring(1);	
					}
					subsplit[2]= subsplit[2].substring(1, subsplit[2].length()-1);
					code = "53";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
					pc++;
					rom[pc] = splitters[2];
				}

				else if(subsplit[1].equals("A")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2]= splitters[2].substring(1);	
					}
					code = "54";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&!splitters[2].startsWith("#")&&splitters[2].endsWith("H")){
					splitters[2]= splitters[2].substring(0, splitters[2].length()-1);
					code = "55";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&splitters[2].startsWith("@")){
					splitters[2]= splitters[2].substring(1);	
					if(splitters[2].equals("R0")){
						code = "56";
					}
					else if(splitters[2].equals("R1")){
						code = "57";
					}
					rom[pc] = code;
				}
					
				else if(subsplit[1].equals("A")){
						if(splitters[2].equals("R0")){
							 code = "58";
						}else if(splitters[2].equals("R1")){
							 code = "59";
						}else if(splitters[2].equals("R2")){
							 code = "5A";
						}else if(splitters[2].equals("R3")){
							 code = "5B";
						}else if(splitters[2].equals("R4")){
							 code = "5C";
						}else if(splitters[2].equals("R5")){
							 code = "5D";
						}else if(splitters[2].equals("R6")){
							 code = "5E";
						}else if(splitters[2].equals("R7")){
							 code = "5F";
						}
						rom[pc] = code;
				}

				else if(subsplit[1].equals("C")){
					if(splitters[2].startsWith("/")){
						splitters[2] = splitters[2].substring(1);
						code = "B0";
					}
					else{
						code = "82";	
					}
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(splitters[2]);
					rom[pc]= add_bit;
				}
				
			}// End of ANL instructions
			
			// Start of JNC instruction
			else if(subsplit[0].equals("JZ")){
				code = "60";
				rom[pc]= code.concat("$").concat(subsplit[1]);
				pc++;
			}// End of JNC instruction
			
			//XRL (Bitwise XOR) opearion starts here
			else if(subsplit[0].equals("XRL")){
				if(subsplit[1].endsWith("H")&&splitters[2].equals("A")){
					subsplit[1]= subsplit[1].substring(1, subsplit[1].length()-1);
					code = "62";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
				}
				
				else if(subsplit[1].endsWith("H")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2]= splitters[2].substring(1);	
					}
					
					subsplit[2]= subsplit[2].substring(1, subsplit[2].length()-1);
					code = "63";
					rom[pc] = code;
					pc++;
					rom[pc] = subsplit[1];
					pc++;
					rom[pc] = splitters[2];
				}

				else if(subsplit[1].equals("A")&&splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2]= splitters[2].substring(1);	
					}
					
					code = "64";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&!splitters[2].startsWith("#")&&splitters[2].endsWith("H")){
					splitters[2]= splitters[2].substring(0, splitters[2].length()-1);
					code = "65";
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if(subsplit[1].equals("A")&&splitters[2].startsWith("@")){
					splitters[2]= splitters[2].substring(1);	
					if(splitters[2].equals("R0")){
						code = "66";
					}
					else if(splitters[2].equals("R1")){
						code = "67";
					}
					rom[pc] = code;
				}
					
				else if(subsplit[1].equals("A")){
					if(splitters[2].equals("R0")){
						 code = "68";
					}else if(splitters[2].equals("R1")){
						 code = "69";
					}else if(splitters[2].equals("R2")){
						 code = "6A";
					}else if(splitters[2].equals("R3")){
						 code = "6B";
					}else if(splitters[2].equals("R4")){
						 code = "6C";
					}else if(splitters[2].equals("R5")){
						 code = "6D";
					}else if(splitters[2].equals("R6")){
						 code = "6E";
					}else if(splitters[2].equals("R7")){
						 code = "6F";
					}
					rom[pc] = code;
				}
			}// End of XRL instructions
			
			//Start of JNZ instruction
			else if(subsplit[0].equals("JNZ")){
				code = "70";
				rom[pc]= code.concat("$").concat(subsplit[1]);
				pc++;
			}// End of JNC instruction
			
			// code for JMP @A+DPTR starts here..
			else if(subsplit[0].equals("JMP")){
				if(subsplit[1].equals("@A+DPTR")){
				 code = "73";
				 rom[pc]= code;
				}
			}// code for JMP ends here.......
			
				//Start of MOV instruction
			else if(subsplit[0].equals("MOV")){
				if(splitters[2].startsWith("#")){
						if(splitters[2].endsWith("H")){
							splitters[2]= splitters[2].substring(1,splitters[2].length()-1);
						}
						else{
							splitters[2]= splitters[2].substring(1);	
						}
					if(subsplit[1].equals("DPTR")){
						code= "90";
						rom[pc]= code;
						pc++;
						int len = splitters[2].length();
						if(len<4){
							int p= 4-len;
							for(int i=0;i<p;i++){
								splitters[2]= "0".concat(splitters[2]);
							}
						}
						
						String[] datasplit= new String[2];
						datasplit[0]= splitters[2].substring(0,2);
						datasplit[1]= splitters[2].substring(2,4);
						
						rom[pc] = datasplit[0];
						pc++;
						rom[pc] = datasplit[1];
					}
					else if(subsplit[1].startsWith("@")){
						subsplit[1]= subsplit[1].substring(1);
						if(subsplit[1].equals("R0")){
							code="76";
						}
						else if(subsplit[1].equals("R1")){
							code="77";
						}
						rom[pc]= code;
						pc++;
						rom[pc]= splitters[2];
					}
					else if(subsplit[1].startsWith("R")){
						if(subsplit[1].equals("R0")){
							code ="78";
						}else if(subsplit[1].equals("R1")){
							code ="79";
						}else if(subsplit[1].equals("R2")){
							code ="7A";
						}else if(subsplit[1].equals("R3")){
							code ="7B";
						}else if(subsplit[1].equals("R4")){
							code ="7C";
						}else if(subsplit[1].equals("R5")){
							code ="7D";
						}else if(subsplit[1].equals("R6")){
							code ="7E";
						}else if(subsplit[1].equals("R7")){
							code ="7F";
						}
					
						rom[pc]= code;
						pc++;
						rom[pc]= splitters[2];
					}
					
					else if(subsplit[1].equals("A")){
						 code = "74";
						 rom[pc]= code;
						 pc++;
						 rom[pc]= splitters[2];
					}
					else if(subsplit[1].endsWith("H")){
						subsplit[1]= subsplit[1].substring(0, subsplit[1].length()-1);
						code= "75";
						rom[pc]= code;
						pc++;
						rom[pc]= subsplit[1];
						pc++;
						rom[pc]= splitters[2];
					}
					else if(subsplit[1].startsWith("P")){
						subsplit[1]= subsplit[1].substring(1);
						int c = Integer.parseInt(subsplit[1], 16);
						code= "75";
						rom[pc]= code;
						pc++;
						rom[pc]= Integer.toHexString(128 + 16*c);
						pc++;
						rom[pc]=splitters[2];
					}
				}
				
				else if(splitters[2].endsWith("H") && !subsplit[1].equals("C")){
					splitters[2]=splitters[2].substring(0, splitters[2].length()-1);
					if(subsplit[1].equals("A")){
						code = "E5";
						 rom[pc]= code;
						 pc++;
						rom[pc]= splitters[2];
					}
					else if(subsplit[1].startsWith("@")){
						subsplit[1]= subsplit[1].substring(1);
						if(subsplit[1].equals("R0")){
							code="A6";
						}
						else if(subsplit[1].equals("R1")){
							code="A7";
						}
						rom[pc]= code;
						pc++;
						rom[pc]= splitters[2];
					}
					else if(subsplit[1].startsWith("R")){
						if(subsplit[1].equals("R0")){
							code = "A8";
						}else if(subsplit[1].equals("R1")){
							code = "A9";
						}else if(subsplit[1].equals("R2")){
							code = "AA";
						}else if(subsplit[1].equals("R3")){
							code = "AB";
						}else if(subsplit[1].equals("R4")){
							code = "AC";
						}else if(subsplit[1].equals("R5")){
							code = "AD";
						}else if(subsplit[1].equals("R6")){
							code = "AE";
						}else if(subsplit[1].equals("R7")){
							code = "AF";
						}
					
						rom[pc]= code;
						pc++;
						rom[pc]= splitters[2];
					}
					else if(subsplit[1].endsWith("H")){
						subsplit[1]= subsplit[1].substring(0,subsplit[1].length()-1);
						code = "85";
						rom[pc]= code;
						pc++;
						rom[pc]= splitters[2];
						pc++;
						rom[pc]= subsplit[1];
					}
				}
				
				else if(splitters[2].equals("A")){
					if(subsplit[1].startsWith("R")){
						if(subsplit[1].equals("R0")){
							 code = "F8";
						}else if(subsplit[1].equals("R1")){
							 code = "F9";
						}else if(subsplit[1].equals("R2")){
							 code = "FA";
						}else if(subsplit[1].equals("R3")){
							 code = "FB";
						}else if(subsplit[1].equals("R4")){
							 code = "FC";
						}else if(subsplit[1].equals("R5")){
							 code = "FD";
						}else if(subsplit[1].equals("R6")){
							code = "FE";
						}else if(subsplit[1].equals("R7")){
							 code = "FF";
						}
						rom[pc]= code;
					}
					
					else if(subsplit[1].startsWith("P")){
						subsplit[1]= subsplit[1].substring(1);
						int c = Integer.parseInt(subsplit[1], 16);
						code= "F5";
						rom[pc]= code;
						pc++;
						rom[pc]= Integer.toHexString(128 + 16*c);
					}
					else if(subsplit[1].startsWith("@")){
						subsplit[1]=subsplit[1].substring(1);
						if(subsplit[1].equals("R0")){
							code = "F6";
						}
						else if(subsplit[1].equals("R1")){
							code = "F7";
						}
						rom[pc]= code;
					}
					else if( subsplit[1].endsWith("H")){
						subsplit[1]= subsplit[1].substring(0, subsplit[1].length()-1);
						code ="F5";
						rom[pc]= code;
						pc++;
						rom[pc] = subsplit[1];
					}
				}
				
				else if(splitters[2].equals("C")){
					code = "92";
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(subsplit[1]);
					rom[pc]= add_bit;
				}
				
				else if(splitters[2].startsWith("@")){
				 splitters[2]= splitters[2].substring(1);
					if(splitters[2].equals("R0")){
						if(subsplit[1].equals("A")){
							 code = "E6";
							 rom[pc]= code;
						}
						else if(subsplit[1].endsWith("H")){
							subsplit[1]= subsplit[1].substring(0, subsplit[1].length()-1);
							code = "86";
							rom[pc]= code;
							pc++;
							rom[pc]= subsplit[1];
						}
					}
					else if(splitters[2].equals("R1")){
						if(subsplit[1].equals("A")){
							 code = "E7";
							 rom[pc]= code;
						}
						else if(subsplit[1].endsWith("H")){
							subsplit[1]= subsplit[1].substring(0, subsplit[1].length()-1);
							code = "87";
							rom[pc]= code;
							pc++;
							rom[pc]= subsplit[1];
						}
					}
				}
				
				else if(splitters[2].startsWith("R")){
					splitters[2] = splitters[2].substring(1);
					int des = Integer.parseInt(splitters[2],16);
					if(des <8){
						if(subsplit[1].equals("A")){
							 code = Integer.toHexString(232 + des);
							 rom[pc]= code;
						}
						else if(subsplit[1].endsWith("H")){
							subsplit[1]= subsplit[1].substring(0, subsplit[1].length()-1);
							code = Integer.toHexString(136 + des);
							rom[pc]= code;
							pc++;
							rom[pc]= subsplit[1];
						}
					}
				}
				else if(subsplit[1].equals("C")){
					code = "A2";
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(splitters[2]);
					rom[pc]= add_bit;
				}
			}// END of MOV instruction*/
			
			else if(subsplit[0].equals("SJMP")){
				code = "80";
				rom[pc]= code.concat("$").concat(subsplit[1]);
			}// End of JNC instruction
			
			//DIV instruction starts here...
			else if (subsplit[0].equals("DIV") && subsplit[1].equals("AB")){
				code =  "84";
				rom[pc]= code;
			}// DIV instruction ends here...
			
			// SUBB instruction starts here...
			else if (subsplit[0].equals("SUBB") && subsplit[1].equals("A")) {
				if(splitters[2].endsWith("H")){
					if(splitters[2].startsWith("#")){
						code = "94";
						splitters[2] = splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						code = "95";
						splitters[2] = splitters[2].substring(0, splitters[2].length()-1);
					}
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				
				else if (splitters[2].startsWith("@")){
					splitters[2] = splitters[2].substring(1);
					if(splitters[2].equals("R0")){
						code = "96";
					}
					else if (splitters[2].equals("R1")){
						code = "97";
					}
					rom[pc]= code;
				}
				
				else if (splitters[2].startsWith("R")){
					if (splitters[2].equals("R0")) {
						code = "98";
					}else if (splitters[2].equals("R1")) {
						code = "99";
					}else if (splitters[2].equals("R2")) {
						code = "9A";
					}else if (splitters[2].equals("R3")) {
						code = "9B";
					}else if (splitters[2].equals("R4")) {
						code = "9C";
					}else if (splitters[2].equals("R5")) {
						code = "9D";
					}else if (splitters[2].equals("R6")) {
						code = "9E";
					}else if (splitters[2].equals("R7")) {
						code = "9F";
					} 
					rom[pc]= code;
				} 
			}// SUBB instructions ends here... 
			
			// MUL starts here...
			else if (subsplit[0].equals("MUL") && subsplit[1].equals("AB")){
				code =  "A4";
				rom[pc]= code;
			}// MUL instruction ends here...
			
			// CPL  function starts here	
			else if(subsplit[0].equals("CPL")){
				if(subsplit[1].equals("C")){
					code ="B3";
					rom[pc] = code;
				}
				else if(subsplit[1].equals("A")){
					code= "F4";
					rom[pc] = code;
				}
				else{
					code = "B2";
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(subsplit[1]);
					rom[pc]= add_bit;
				
				}
			}// CPL function ends here
			
			// CJNE starts here...
			else if(subsplit[0].equals("CJNE")){
				if(subsplit[1].equals("A")){
					if(splitters[2].startsWith("#")){
						code = "B4";
						rom[pc]= code.concat("$").concat(splitters[3]);
						pc++;
						if(splitters[2].endsWith("H")){
							splitters[2]= splitters[2].substring(1, splitters[2].length()-1);
						}
						else{
							splitters[2]= splitters[2].substring(1);	
						}
						rom[pc]= splitters[2];
						pc++;
					}
					
					if(!splitters[2].startsWith("#")&&splitters[2].endsWith("H")){
						code = "B5";
						rom[pc]= code.concat("$").concat(splitters[3]);
						pc++;
						splitters[2]= splitters[2].substring(0, splitters[2].length()-1);
						rom[pc]= splitters[2];
						pc++;
					}
				}
				else if(splitters[2].startsWith("#") && subsplit[1].startsWith("@")){
					subsplit[1]= subsplit[1].substring(1);
					if(splitters[2].endsWith("H")){
						splitters[2] = splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2] = splitters[2].substring(1);
					}
					if(subsplit[1].equals("R0")){
						code = "B6";
					}
					else if(subsplit[1].equals("R1")){
						code = "B7";
					}
					
					rom[pc] = code.concat("$").concat(splitters[3]);
					pc++;
					rom[pc] = splitters[2];
					pc ++;
				}
				
				else if(splitters[2].startsWith("#")){
					if(splitters[2].endsWith("H")){
						splitters[2] = splitters[2].substring(1, splitters[2].length()-1);
					}
					else{
						splitters[2] = splitters[2].substring(1);
					}
					
					if(subsplit[1].equals("R0")){
						code = "B8";
					}else if(subsplit[1].equals("R1")){
						code = "B9";
					}else if(subsplit[1].equals("R2")){
						code = "BA";
					}else if(subsplit[1].equals("R3")){
						code = "BB";
					}else if(subsplit[1].equals("R4")){
						code = "BC";
					}else if(subsplit[1].equals("R5")){
						code = "BD";
					}else if(subsplit[1].equals("R6")){
						code = "BE";
					}else if(subsplit[1].equals("R7")){
						code = "BF";
					}
					
					rom[pc] = code.concat("$").concat(splitters[3]);
					pc++;
					rom[pc] = splitters[2];
					pc ++;
				}
				
			}// CJNE ends here...
			
			//PUSH instruction starts here...
			else if (subsplit[0].equals("PUSH")){
				code = "C0";
				rom[pc]= code;
				pc++;
				if(subsplit[1].endsWith("H")){
					subsplit[1] = subsplit[1].substring(0, subsplit[1].length()-1);
					
					int temp = Integer.parseInt(subsplit[1],16);
					if(temp<127){
						rom[pc]= subsplit[1];
					}
				}
				else if(subsplit[1].equals("A")){
					rom[pc]= "E0";
				}
				else if(subsplit[1].equals("B")){
					rom[pc]= "F0";
				}
				else if(subsplit[1].equals("DPL")){
					rom[pc]= "82";
				}
				else if(subsplit[1].equals("DPH")){
					rom[pc]= "83";
				}
			}// PUSH instruction ends here...
			
			// CLR  function starts here	
			else if(subsplit[0].equals("CLR")){
				if(subsplit[1].equals("C")){
						code ="C3";
						rom[pc] = code;
				}
				else if(subsplit[1].equals("A")){
					code= "E4";
					rom[pc] = code;
				}
				else {
					code = "C2";
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(subsplit[1]);
					rom[pc]= add_bit;
				}
			}// CLR function ends here
			
			// code for SWAP instruction starts here..
			else if (subsplit[0].equals("SWAP")&& subsplit[1].equals("A")){
				code = "C4";
				rom[pc]= code;
			}// code for SWAP ends here...
	
			// Instruction for XCH starts here...
			else if (subsplit[0].equals("XCH") && subsplit[1].equals("A")) {
				if(splitters[2].contains("H")){
					code = "C5";
					splitters[2] = splitters[2].substring(0, splitters[2].length()-1);
					rom[pc] = code;
					pc++;
					rom[pc] = splitters[2];
				}
				else if (splitters[2].startsWith("@")){
					splitters[2] = splitters[2].substring(1);
					if(splitters[2].equals("R0")){
						code = "C6";
					}else if (splitters[2].equals("R1")){
						code = "C7";
					}
					rom[pc]= code;
				}
				else if (splitters[2].contains("R")){
					if (subsplit[1].equals("R0")) {
						code = "C8";
					} else if (subsplit[1].equals("R1")) {
						code = "C9";
					} else if (subsplit[1].equals("R2")) {
						code = "CA";
					} else if (subsplit[1].equals("R3")) {
						code = "CB";
					} else if (subsplit[1].equals("R4")) {
						code = "CC";
					} else if (subsplit[1].equals("R5")) {
						code = "CD";
					} else if (subsplit[1].equals("R6")) {
						code = "CE";
					} else if (subsplit[1].equals("R7")) {
						code = "CF";
					} 
					hex = Integer.toHexString(pc);
					rom[pc]= code;
				}  
				
			}// code for XCH ends here...
			
			// POP instruction starts here...
			else if (subsplit[0].equals("POP")){
				code = "D0";
				rom[pc]= code;
				pc++;
				if(subsplit[1].endsWith("H")){
					subsplit[1] = subsplit[1].substring(0, subsplit[1].length()-1);
					int temp = Integer.parseInt(subsplit[1],16);
					if(temp<127){
						rom[pc]= subsplit[1];	
					}
					
				}
				else if(subsplit[1].equals("A")){
					rom[pc]= "E0";
				}
				else if(subsplit[1].equals("B")){
					rom[pc]= "F0";
				}
				else if(subsplit[1].equals("DPL")){
					rom[pc]= "82";
				}
				else if(subsplit[1].equals("DPH")){
					rom[pc]= "83";
				}
			}// POP instruction ends here...
			
	// code for SETB instruction starts here...
			else if (subsplit[0].equals("SETB")){
				if(subsplit[1].equals("C")){
					code = "D3";
					rom[pc]= code;
				}
				else{
					code = "D2";
					rom[pc]= code;
					pc++;
					String add_bit = addressOfBitValue(subsplit[1]);
					rom[pc]= add_bit;
				}
			}// SETB instruction ends here...
			
			
			// DA instruction starts here...
			else if (subsplit.equals("DA") && subsplit.equals("A")){
				code = "D4";
				rom[pc]= code;
			}//DA instruction ends here...
			
			//DJNZ instruction starts here
			else if (subsplit[0].equals("DJNZ")){
				if(subsplit[1].endsWith("H")){
					code = "D5";
					rom[pc] = code.concat("$").concat(splitters[2]);
					subsplit[1] = subsplit[1].substring(0, subsplit[1].length()-1);
					pc++;
					rom[pc] = subsplit[1];
					pc++;
				}
				else{ 
					
					if (subsplit[1].equals("R0")){
						code = "D8";
					}else if (subsplit[1].equals("R1")){
						code = "D9";
					}else if (subsplit[1].equals("R2")){
						code = "DA";
					}else if (subsplit[1].equals("R3")){
						code = "DB";
					}else if (subsplit[1].equals("R4")){
						code = "DC";
					}else if (subsplit[1].equals("R5")){
						code = "DD";
					}else if (subsplit[1].equals("R6")){
						code = "DE";
					}else if (subsplit[1].equals("R7")){
						code = "DF";
					}
					rom[pc] = code.concat("$").concat(splitters[2]);
					pc++;
				}// Doubt here
				
			}//DJNZ instruction ends here

			// code for XCHD starts here....
			else if (subsplit[0].equals("XCHD") && subsplit[1].equals("A") && splitters[2].startsWith("@")){
				splitters[2] = splitters[2].substring(1);
				if (splitters[2].equals("R0")) {
					code = "D6";
				} 
				else if (splitters[2].equals("R1")) {
					code = "D7";
				} 
				rom[pc] = code;
			}// code for XCHD ends here...

			//MOVX instruction starts here...
			else if(subsplit[0].equals("MOVX")){
				if(subsplit[1].equals("A")){
					if(splitters[2].equals("@DPTR")){
						code ="E0";
					}
					else if(splitters[2].equals("@R0")){
						code ="E2";
					}
					else if(splitters[2].equals("@R1")){
						code ="E3";
					}
					rom[pc]= code;
				}
				else if(splitters[2].equals("A")){
					
					if(subsplit[1].equals("@R0")){
						code ="F2";
					}
					else if(subsplit[1].equals("@R1")){
						code ="F3";
					}
					else if(subsplit[1].equals("@DPTR")){
						code ="F0";
					}
					rom[pc] = code;
				}
				
			}// End of MOVX instructions
			
			else{
				err_msg+="Syntax Error in line number:" +linenumber+"\n";
			}
			
			if(err_msg.equals("") && rom[temporary_pc].equals("0") && x==0){
				err_msg+="Syntax Error in line number"+linenumber +"\n";
			}
		// End of all instructions...
		pc++;
	}

		
	public static String addressOfBitValue(String string) {
		String bit_add="";
		if(string.endsWith("H")){
			bit_add = string.substring(0, string.length()-1);
		}
		else if(string.equals("CY")){
			bit_add = "D7";
		}
		else if(string.equals("AC")){
			bit_add = "D6";
		}
		else if(string.equals("F0")){
			bit_add = "D5";
		}
		else if(string.equals("RS1")){
			bit_add = "D4";
		}
		else if(string.equals("RS0")){
			bit_add = "D3";
		}
		else if(string.equals("OV")){
			bit_add = "D2";
		}
		else if(string.equals("UD")){
			bit_add = "D1";
		}
		else if(string.equals("P")){
			bit_add = "D0";
		}
		
		else if(string.contains(".")){
			String[] dotSplit = string.split("[.]");
			if(dotSplit[0].equals("P0")){
				bit_add = Integer.toHexString(128 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("TCON")){
				bit_add = Integer.toHexString(136 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("P1")){
				bit_add = Integer.toHexString(144 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("SCON")){
				bit_add = Integer.toHexString(152 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("P2")){
				bit_add = Integer.toHexString(160 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("IE")){
				bit_add = Integer.toHexString(168 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("P3")){
				bit_add = Integer.toHexString(176 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("IP")){
				bit_add = Integer.toHexString(184 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("PSW")){
				bit_add = Integer.toHexString(208 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("ACC")){
				bit_add = Integer.toHexString(224 + Integer.parseInt(dotSplit[1], 16));
			}
			else if(dotSplit[0].equals("B")){
				bit_add = Integer.toHexString(240 + Integer.parseInt(dotSplit[1], 16));
			}
			
		}
		return bit_add;
	}
	
}
