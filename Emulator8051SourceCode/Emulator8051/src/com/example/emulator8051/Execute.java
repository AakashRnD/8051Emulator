/*
 * Description: This class executes the instructions based on ram values
 * Author: Prayasee Pradhan    	   Email id: prayasee91@gmail.com
 * Author: Priyanka Padmanabhan	   Email id: priyanka.91092@gmail.com
 * 			
 * 
 */


package com.example.emulator8051;

public class Execute  {
	static String[] ram = new String[256];
	static int sp = 7; // Stack pointer
	static String[][] timeline_array = new String[100][2];
	static int CY=0, AC=0, P=0;
	static int timeline = 0, osc_period=0;
	static int ptr = 0; // For checking which register bank to be selected
	static String[] extram = new String[256];

	public static int nextPC(String string, int k) {     //static
		String bin_val =""; // Binary string of any hex value
		int data = 0; //Variable for arithmetic operations
		int add=0, len=0; // Address value and length of any binary string
		String dptr = ram[131].concat(ram[130]);


		if(string.equals("0")){
			k= Assemble.ending_address;
			k--;
		}


		else if(string.startsWith("0")){
			if(string.endsWith("0")){
				osc_period = 12;
			}
			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();

				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				k++;
				String hb = Assemble.rom[k];
				k++;
				String lb = Assemble.rom[k];
				k=Integer.parseInt(hb.concat(lb),16);
				k--;
				osc_period = 24;
				}

			else if(string.endsWith("3")){
				bin_val=Integer.toBinaryString(Integer.parseInt(ram[224], 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				bin_val = Character.toString(bin_val.charAt(7)).concat(bin_val.substring(0, 7));
				ram[224]= Integer.toHexString(Integer.parseInt(bin_val,2));
				osc_period = 12;

			}

			else if(string.endsWith("4")){
				data = Integer.parseInt(ram[224],16) + 1;
				if(data==256){
					data = 0;
				}
				ram[224] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k],16)],16)+1;
				if(data==256){
					data = 0;
				}
				ram[Integer.parseInt(Assemble.rom[k],16)] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("6")){
				add = ptr;
				int iaddint = Integer.parseInt(ram[add],16);
				data = Integer.parseInt(ram[iaddint],16) + 1;
				if(data==256){
					data = 0;
				}
				ram[iaddint] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add = ptr+1;
				int iaddint = Integer.parseInt(ram[add],16);
				data = Integer.parseInt(ram[iaddint],16) + 1;
				if(data==256){
					data = 0;
				}
				ram[iaddint] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=8 && Integer.parseInt(string,16)<=15 ){
				add = ptr + (Integer.parseInt(string,16) - 8);
				data = Integer.parseInt(ram[add],16) + 1;
				if(data==256){
					data = 0;
				}
				ram[add] = Integer.toHexString(data);
				osc_period = 12;
			}
		}


		else if (string.startsWith("1")){
			if(string.endsWith("0")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				if(bit==1){
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					writeBit(temp , '0');
					k--;
				}

				else{
					k = k + 2;
				}
			osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = "0".concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();

				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();

				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();

				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Integer.toHexString(k+3);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = "0".concat(temp);
					}
				}

				String lb = temp.substring(2,4);
				String hb = temp.substring(0,2);
				sp++;
				ram[sp] = lb;
				sp++;
				ram[sp] = hb;
				k++;
				hb = Assemble.rom[k];
				k++;
				lb = Assemble.rom[k];
				k=Integer.parseInt(hb.concat(lb),16);
				k--;
				osc_period = 24;

			}

			else if(string.endsWith("3")){

				bin_val=Integer.toBinaryString(Integer.parseInt(ram[224], 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				int c = readBit("D7");
				bin_val = Integer.toString(c).concat(bin_val.substring(0, 7));
				ram[224] = Integer.toHexString(Integer.parseInt(bin_val,2));
				CY = Integer.parseInt(Character.toString(bin_val.charAt(7)) , 2);
				if(CY==1){
					writeBit("D7",'1');
				}
				else{
					writeBit("D7",'0');
				}
			}

			else if(string.endsWith("4")){
				data = Integer.parseInt(ram[224],16) - 1;
				if(data==-1){
					data = 255;
				}
				ram[224] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k],16)],16)-1;
				if(data==-1){
					data = 255;
				}
				ram[Integer.parseInt(Assemble.rom[k],16)] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("6")){
				add = ptr;
				int iaddint = Integer.parseInt(ram[add],16);
				data = Integer.parseInt(ram[iaddint],16) - 1;
				if(data==-1){
					data = 255;
				}
				ram[iaddint] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add = ptr+1;
				int iaddint = Integer.parseInt(ram[add],16);
				data = Integer.parseInt(ram[iaddint],16) - 1;
				if(data==-1){
					data = 255;
				}
				ram[iaddint] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=24 && Integer.parseInt(string,16)<=31 ){
				add = ptr + (Integer.parseInt(string,16) - 24);
				data = Integer.parseInt(ram[add],16) - 1;
				if(data==-1){
					data = 255;
				}
				ram[add] = Integer.toHexString(data);
				osc_period = 12;
			}
		}


		else if (string.startsWith("2")){
			if(string.endsWith("0")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				if(bit==1){
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
				k--;
				}
				else{
					k = k + 2;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();

				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp1,temp2;
				temp1 = ram[sp];
				sp--;
				temp2 = ram[sp];
				sp--;
				k = Integer.parseInt(temp1.concat(temp2), 16);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				bin_val=Integer.toBinaryString(Integer.parseInt(ram[224], 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
					bin_val = bin_val.substring(1).concat(Character.toString(bin_val.charAt(0)));
					ram[224] = Integer.toHexString(Integer.parseInt(bin_val,2));
				}
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=36 && Integer.parseInt(string,16)<=47 ){

				if(string.endsWith("4")){
					k++;
					data = Integer.parseInt(Assemble.rom[k],16);
				}
				else if(string.endsWith("5")){
					k++;
					data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k],16)],16);
				}
				else if(string.endsWith("6")){
					add = ptr;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if(string.endsWith("7")){
					add = ptr+1;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if (Integer.parseInt(string,16)>=40 && Integer.parseInt(string,16)<=47 ){
					add = ptr + (Integer.parseInt(string,16) - 40);
					data = Integer.parseInt(ram[add],16);
				}

				int A = Integer.parseInt(ram[224],16);
				int ac1 = A & 15;
				int ac2 = data & 15;
				int ov1 = A & 127;
				int ov2 = data & 127;
				int ov3 = 0;
				if((ov1 + ov2)>127){
					ov3 = 1;
				}
				A = A + data;
				if(A>255){
					A = A - 256;
					writeBit("D7", '1');
				}else {
					writeBit("D7", '0');
				}

				if((ac1+ac2) > 15){
					writeBit("D6", '1');
				}
				else {
					writeBit("D6", '1');
				}

				if ( (CY==0 && ov3==1) || (CY==1 && ov3==0)){
					writeBit("D2", '1');
				}
				else {
					writeBit("D2", '0');
				}
				ram[224] = Integer.toHexString(A);
				osc_period = 12;
			}
		}


		else if (string.startsWith("3")){
			if(string.endsWith("0")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				if(bit==0){
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					k--;
				}
				else{
					k = k + 2;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp1,temp2;
				temp1 = ram[sp];
				sp--;
				temp2 = ram[sp];
				sp--;
				k = Integer.parseInt(temp1.concat(temp2), 16);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				bin_val=Integer.toBinaryString(Integer.parseInt(ram[224], 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				int c = readBit("D7");
				bin_val = bin_val.substring(1).concat(Integer.toString(c));
				ram[224] = Integer.toHexString(Integer.parseInt(bin_val,2));
				CY = Integer.parseInt(Character.toString(bin_val.charAt(0)),16);
				if(CY==1){
					writeBit("D7",'1');
				}
				else{
					writeBit("D7",'0');
				}
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=52 && Integer.parseInt(string,16)<=63 ){
				if(string.endsWith("4")){
					k++;
					data = Integer.parseInt(Assemble.rom[k],16);
				}
				else if(string.endsWith("5")){
					k++;
					data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k],16)],16);
				}
				else if(string.endsWith("6")){
					add = ptr;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if(string.endsWith("7")){
					add = ptr+1;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if (Integer.parseInt(string,16)>=56 && Integer.parseInt(string,16)<=63 ){
					add = ptr + (Integer.parseInt(string,16) - 56);
					data = Integer.parseInt(ram[add],16);
				}
				int A = Integer.parseInt(ram[224],16);
				int ac1 = A & 15;
				int ac2 = data & 15;
				int ov1 = A & 127;
				int ov2 = data & 127;
				int ov3 = 0;
				if((ov1 + ov2 + CY)>127){
					ov3 = 1;
				}
				if((ac1+ac2+CY) > 15){
					writeBit("D6", '1');
				}
				else {
					writeBit("D6", '0');
				}
				A = A + data + CY;
				if(A>255){
					A = A - 256;
					writeBit("D7", '1');
				}else {
					writeBit("D7", '0');
				}
				if ( (CY==0 && ov3==1) || (CY==1 && ov3==0)){
					writeBit("D2", '1');
				}
				else {
					writeBit("D2", '0');
				}
				ram[224] = Integer.toHexString(A);
				osc_period = 12;
			}
		}


		else if(string.startsWith("4")){
			if(string.endsWith("0")){
				if(CY == 0){
					k++;
				}
				else{
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 2 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 2 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();

				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)],16);
				int A= Integer.parseInt(ram[224], 16);
				data = data | A;
				ram[Integer.parseInt(Assemble.rom[k],16)] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				k++;
				String add_1 =Assemble.rom[k];
				k++;
				String add_2 = Assemble.rom[k];
				data  = Integer.parseInt(ram[Integer.parseInt(add_1,16)], 16) | Integer.parseInt(add_2, 16);
				ram[Integer.parseInt(add_1,16)] = Integer.toHexString(data);
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				k++;
				data = Integer.parseInt(Assemble.rom[k], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16) | data);
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16)| data);
				osc_period = 12;
			}

			else if(string.endsWith("6")){
				add = ptr;
				int iaddint =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddint],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16)| data);
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add = ptr+1;
				int iaddint =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddint],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16)| data);
				osc_period = 12;
			}

			else if(Integer.parseInt(string,16)>=72 && Integer.parseInt(string, 16)<=79){
				add = ptr + (Integer.parseInt(string, 16)-72);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) | Integer.parseInt(ram[add], 16));
				osc_period = 12;
			}

		}


		else if(string.startsWith("5")){
			if(string.endsWith("0")){
				if(CY == 1){
					k++;
				}
				else{
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 2 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 2 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)],16);
				int A= Integer.parseInt(ram[224], 16);
				data = data & A;
				ram[Integer.parseInt(Assemble.rom[k],16)] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				k++;
				String add_1 =Assemble.rom[k];
				k++;
				String add_2 = Assemble.rom[k];
				data  = Integer.parseInt(ram[Integer.parseInt(add_1,16)], 16) & Integer.parseInt(add_2, 16);
				ram[Integer.parseInt(add_1,16)] = Integer.toHexString(data);
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				k++;
				data = Integer.parseInt(Assemble.rom[k], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16) & data);
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16)& data);
				osc_period = 12;
			}

			else if(string.endsWith("6")){
				add = ptr;
				int iaddt =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddt],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) & data);
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add = ptr+1;
				int iaddt =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddt],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16)& data);
				osc_period = 12;
			}

			else if(Integer.parseInt(string,16)>=88 && Integer.parseInt(string, 16)<=95){
				add = ptr + (Integer.parseInt(string, 16)-88);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) & Integer.parseInt(ram[add], 16));
				osc_period = 12;
			}

		}


		else if(string.startsWith("6")){

			if(string.endsWith("0")){
				if(Integer.parseInt(ram[224],16)== 0){
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}

					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 2 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 2 - rel;
					}
					k--;
				}
				else{
					k++;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)],16);
				int A= Integer.parseInt(ram[224], 16);
				data = data ^ A;
				ram[Integer.parseInt(Assemble.rom[k],16)] = Integer.toHexString(data);
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				k++;
				String add_1 =Assemble.rom[k];
				k++;
				String add_2 = Assemble.rom[k];
				data  = Integer.parseInt(ram[Integer.parseInt(add_1,16)], 16) ^ Integer.parseInt(add_2, 16);
				ram[Integer.parseInt(add_1,16)] = Integer.toHexString(data);
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				k++;
				data = Integer.parseInt(Assemble.rom[k], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16) ^ data);
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				k++;
				data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k], 16)], 16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224], 16) ^ data);
				osc_period = 12;
			}

			else if(string.endsWith("6")){
				add = ptr;
				int iaddt =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddt],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) ^ data);
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add = ptr+1;
				int iaddt =Integer.parseInt(ram[add], 16);
				data = Integer.parseInt(ram[iaddt],16);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) ^ data);
				osc_period = 12;
			}

			else if(Integer.parseInt(string,16)>=104 && Integer.parseInt(string, 16)<=111){
				add = ptr + (Integer.parseInt(string, 16)-104);
				ram[224] = Integer.toHexString(Integer.parseInt(ram[224],16) ^ Integer.parseInt(ram[add], 16));
				osc_period = 12;
			}

		}


		else if(string.startsWith("7")){

			if(string.endsWith("0")){
				if(Integer.parseInt(ram[224],16)== 0){
					k++;
				}
				else{
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 2 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 2 - rel;
					}
				k--;
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				CY = CY | bit;
				if(CY==1){
					writeBit("D7",'1');
				}
				else{
					writeBit("D7",'0');
				}
				k++;
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				int A = Integer.parseInt(ram[224],16);
				int DPTR = Integer.parseInt(dptr,16);
				k = A + DPTR;
				if(k>65535){k = k - 65535;}
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				k++;
				ram[224]= Assemble.rom[k];
				osc_period = 12;
			}

			else if(string.endsWith("5")){
				int number = Integer.parseInt(Assemble.rom[k+1],16);
				ram[number]= Assemble.rom[k+2];
				k+=2;
				osc_period = 24;
			}

			else if(string.endsWith("6")){
				add= ptr;
				k++;
				ram[Integer.parseInt(ram[add], 16)]=Assemble.rom[k];
				osc_period = 12;
			}

			else if(string.endsWith("7")){
				add= ptr +1;
				k++;
				ram[Integer.parseInt(ram[add], 16)]= Assemble.rom[k];
				osc_period = 12;
			}

			else if(Integer.parseInt(string, 16)>=120 && Integer.parseInt(string, 16)<=127){
				add= ptr + (Integer.parseInt(string, 16)- 120);
				k++;
				ram[add]= Assemble.rom[k];
				osc_period = 12;
			}
		}


		else if(string.startsWith("8")){

			if(string.endsWith("0")){
				bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
				if(bin_val.length()<8){
					for(int p=0;p<8-bin_val.length();p++){
						bin_val= "0".concat(bin_val);
					}
				}

				if (bin_val.startsWith("0")){
					int rel = Integer.parseInt(bin_val, 2);
					k= k + 2 + rel;
				}

				else if (bin_val.startsWith("1")){
					int rel = 256 - Integer.parseInt(bin_val, 2);
					if(rel==2){
						k=Assemble.ending_address;
					}
					else{
						k = k + 2 - rel;
					}
				}
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				CY = CY & bit;
				k++;
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				int A = Integer.parseInt(ram[224],16);
				add = A + k + 1;
				ram[224] = Assemble.rom[add];
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				int A = Integer.parseInt(ram[224],16);
				int B = Integer.parseInt(ram[240],16);
				int answer=0,remainder=0;
				if(B==0){
					writeBit("D2",'1');
				}
				else{
					answer =  (int) A/B;
					remainder = (int) A%B;
					writeBit("D2",'0');
				}

				writeBit("D7",'0');
				ram[224] = Integer.toHexString(answer);
				ram[240] = Integer.toHexString(remainder);
				osc_period = 48;
			}

			else if(string.endsWith("5")){
				ram[Integer.parseInt(Assemble.rom[k+2],16)]= ram[Integer.parseInt(Assemble.rom[k+1],16)];
				k +=2;
				osc_period = 24;
			}

			else if(string.endsWith("6")){
				add = ptr;
				k++;
				ram[Integer.parseInt(Assemble.rom[k],16)]= ram[Integer.parseInt(ram[add], 16)];
				osc_period = 24;
			}

			else if(string.endsWith("7")){
				add = ptr +1;
				k++;
				ram[Integer.parseInt(Assemble.rom[k],16)]= ram[Integer.parseInt(ram[add], 16)];
				osc_period = 24;
			}

			else if(Integer.parseInt(string, 16)>=136 && Integer.parseInt(string, 16)<=143){
				add = ptr +(Integer.parseInt(string, 16)-136);
				k++;
				ram[Integer.parseInt(Assemble.rom[k], 16)] = ram[add];
				osc_period = 24;
			}

		}


		else if(string.startsWith("9")){

			if(string.endsWith("0")){
				k++;
				String data1 = Assemble.rom[k];
				k++;
				String data2 = Assemble.rom[k];
				ram[131] = data1;
				ram[130] = data2;
				if(ram[130].length()<2){
					ram[130] = "0".concat(ram[130]);
				}

				if(ram[131].length()<2){
					ram[131] = "0".concat(ram[131]);
				}
				dptr = ram[131].concat(ram[130]);
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
					bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
					bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				CY = readBit("D7");
				if(CY== 1){
					writeBit(temp , '1');
				}
				else{
					writeBit(temp , '0');
				}
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				int temp = Integer.parseInt(dptr,16);
				int A = Integer.parseInt(ram[224],16);
				add = temp + A;
				ram[224] = Assemble.rom[add];
				osc_period = 24;
			}

			else if (Integer.parseInt(string,16)>=148 && Integer.parseInt(string,16)<=159 ){

				if(string.endsWith("4")){
					k++;
					data = Integer.parseInt(Assemble.rom[k],16);
				}
				else if(string.endsWith("5")){
					k++;
					data = Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k],16)],16);
				}
				else if(string.endsWith("6")){
					add = ptr;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if(string.endsWith("7")){
					add = ptr+1;
					int iaddint = Integer.parseInt(ram[add],16);
					data = Integer.parseInt(ram[iaddint],16);
				}
				else if (Integer.parseInt(string,16)>=152 && Integer.parseInt(string,16)<=159 ){
					add = ptr + (Integer.parseInt(string,16) - 152);
					data = Integer.parseInt(ram[add],16);
				}
				int A = Integer.parseInt(ram[224],16);
				int ac1 = A & 15;
				int ac2 = data & 15;
				int ov1 = A & 127;
				int ov2 = data & 127;
				int ov3 = 0;
				if((ov2+CY) > ov1){ov3 = 1;}
				if ((ac2+CY) > ac1){
					writeBit("D6",'1');
				}
				else{
					writeBit("D6",'0');
				}
				A = A - CY - data;
				if (A<0){
					A = 255 + A;
					writeBit("D7",'1');
				} 
				else {
					writeBit("D7",'0');
				}
				if ( (CY==0 && ov3==1) || (CY==1 && ov3==0)){
					writeBit("D2",'1');
				}
				else{
					writeBit("D2",'0');
				}
				ram[224] = Integer.toHexString(A);
				osc_period = 12;
			}
		}


		else if(string.startsWith("A")){
			if(string.endsWith("0")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				k++;
				if(bit==1){
					bit =0;
				}
				else{
					bit =1;
				}
				CY = CY | bit;
				if(CY == 0){
					writeBit("D7",'0');
				}
				else{
					writeBit("D7",'1');
				}
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
					bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				if(bit==1){
					writeBit("D7",'1');
				}
				else{
					writeBit("D7",'0');
				}
				k++;
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				dptr = Integer.toHexString(Integer.parseInt(dptr,16)+1);
				if(dptr.length()<4){
					int p = 4 - dptr.length();
					for(int i=0;i<p;i++){
						dptr = "0".concat(dptr);
					}
				}
				ram[131] = dptr.substring(0,2);
				ram[130] = dptr.substring(2,4);
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				int A = Integer.parseInt(ram[224],16);
				int B = Integer.parseInt(ram[240],16);
				int answer = A*B;
				writeBit("D7",'0');
				if (answer > 255){
					writeBit("D2",'1');
				}
				else {
					writeBit("D2",'0');
				}
				String temp = Integer.toHexString(answer);
				if(temp.length()<4){
					int c = 4 -temp.length();
					for(int i=0;i<c;i++){
						temp = ("0").concat(temp);
					}
				}
				ram[240] = temp.substring(0, 2);
				ram[224] = temp.substring(2, 4);
				osc_period = 48;
			}

			else if(string.endsWith("5")){

			// reserved

			}

			else if(string.endsWith("6")){
				add = ptr;
				k++;
				ram[Integer.parseInt(ram[add],16)]= ram[Integer.parseInt(Assemble.rom[k],16)];
				osc_period = 24;
			}

			else if(string.endsWith("7")){
				add = ptr + 1;
				k++;
				ram[Integer.parseInt(ram[add],16)]= ram[Integer.parseInt(Assemble.rom[k],16)];
				osc_period = 24;
			}

			else if(Integer.parseInt(string, 16)>=168 && Integer.parseInt(string, 16)<= 175){
				add = ptr + Integer.parseInt(string, 16)-168;
				k++;
				ram[add] = ram[Integer.parseInt(Assemble.rom[k], 16)];
				osc_period = 24;
			}
		}


		else if(string.startsWith("B")){

			if(string.endsWith("0")){
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				k++;
				if(bit==1){
					bit =0;
				}
				else{
					bit =1;
				}
				CY = CY & bit;
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				char ch;
				String temp = Assemble.rom[k+1];
				int bit = readBit(temp);
				if(bit==1){
					ch = '0';
				}
				else{
					ch = '1';
				}
				writeBit(temp, ch);
				k++;
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				if(CY==1){
					writeBit("D7",'0');
				}
				else{
					CY=1;
					writeBit("D7",'1');
				}
				osc_period = 12;
			}

			else if(string.endsWith("4")){
				if(ram[224].equals(Assemble.rom[k+1])){
					k=k+2;
				}
				else {
					if(Integer.parseInt(ram[224],16)<Integer.parseInt(Assemble.rom[k+1],16)){
						writeBit("D7", '1');
					}
					else {
						writeBit("D7", '0');
					}
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

			else if(string.endsWith("5")){
				if(ram[224].equals(ram[Integer.parseInt(Assemble.rom[k+1],16)])){
					k=k+2;
				}
				else {
					if(Integer.parseInt(ram[224],16)<Integer.parseInt(ram[Integer.parseInt(Assemble.rom[k+1],16)],16)){
						writeBit("D7", '1');
					}
					else {
						writeBit("D7", '0');
					}
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

			else if(string.endsWith("6") || string.endsWith("7")){
				if(string.endsWith("6")){
					add = ptr;
				}
				else {
					add = ptr + 1;
				}
				if(ram[Integer.parseInt(ram[add],16)].equals(Assemble.rom[k+1])){
					k=k+2;
				}
				else{

					if(Integer.parseInt(ram[Integer.parseInt(ram[add],16)],16)<Integer.parseInt(Assemble.rom[k+1],16)){
						writeBit("D7", '1');
					}

					else {
						writeBit("D7", '0');
					}

					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}

					if (bin_val.startsWith("0")){
					int rel = Integer.parseInt(bin_val, 2);
					k = k + 3 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}

					k--;
				}
				osc_period = 24;
			}

			else if(Integer.parseInt(string, 16)>=184 && Integer.parseInt(string, 16)<= 191){
				add = ptr + Integer.parseInt(string, 16)-184;
				if(ram[add].equals(Assemble.rom[k+1])){
					k=k+2;
				}
				else {
					if(Integer.parseInt(ram[add],16)<Integer.parseInt(Assemble.rom[k+1],16)){
						writeBit("D7", '1');
					}
					else {
						writeBit("D7", '0');
					}

					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}

					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

		}


		else if(string.startsWith("C")){
			if(string.endsWith("0")){
				k++;
				sp++;
				ram[sp] = ram[Integer.parseInt(Assemble.rom[k],16)];
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				writeBit(temp,'0');
				k++;
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				writeBit("D7",'0');
				osc_period = 12;
			}

			else if(string.endsWith("4")){
				if(ram[224].length()==1){
					ram[224] = ram[224].concat("0");
				}
				else if (ram[224].length()==2){
					ram[224] = Character.toString(ram[224].charAt(1)).concat(Character.toString((ram[224].charAt(0))));
				}
				osc_period = 24;
			}

			else if (Integer.parseInt(string,16)>=197 && Integer.parseInt(string,16)<=207 ){

				String value = "";
				if(string.endsWith("5")){
					k++;
					add = Integer.parseInt(Assemble.rom[k],16);
					value = ram[add];
				}

				else if(string.endsWith("6")){
					add = ptr;
					value = ram[Integer.parseInt(ram[add],16)];
				}

				else if(string.endsWith("7")){
					add = ptr+1;
					value = ram[Integer.parseInt(ram[add],16)];
				}

				else if (Integer.parseInt(string,16)>=200 && Integer.parseInt(string,16)<=207 ){
					add = ptr + (Integer.parseInt(string,16) - 200);
					value = ram[add];
				}

				String temp = ram[224];
				ram[224] = value;
				ram[add] = temp;
				osc_period = 12;
			}

		}


		else if(string.startsWith("D")){
			if(string.endsWith("0")){
				k++;
				ram[Integer.parseInt(Assemble.rom[k],16)] = ram[sp];
				sp--;
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
					bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				String temp = Assemble.rom[k+1];
				writeBit(temp,'1');
				k++;
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				writeBit("D7", '1');
				osc_period = 12;
			}

			else if(string.endsWith("4")){
				int A = Integer.parseInt(ram[224],16);
				String lb1 = ram[224].substring(0,3);
				int lb2 = Integer.parseInt(lb1,16);
				if(AC==1 || lb2>9){
					A = A + 6;
					ram[224] = Integer.toHexString(A);
				}
				if(A>255){
					CY =1;
				}
				lb1 = ram[224].substring(0,3);
				String hb1 = ram[224].substring(4,7);
				lb2 = Integer.parseInt(lb1,16);
				int hb2 = Integer.parseInt(hb1,16);
				if(CY==1 || hb2>9){
					hb2 = hb2 + 6;
				}
				if(hb2>9){
					CY=1;
				}
				lb1 = Integer.toHexString(lb2);
				hb1 = Integer.toHexString(hb2);
				ram[224] = hb1 + lb1;
				osc_period = 12;
			}

			else if (string.endsWith("5")){
				add = Integer.parseInt(Assemble.rom[k+1],16);
				if(Integer.parseInt(ram[add],16)==0){
					ram[add]= "FF";
				}
				else{
					ram[add] = Integer.toHexString(Integer.parseInt(ram[add],16)-1);
				}
				if(Integer.parseInt(ram[add],16)==0){
					k+=2;
				}
				else{
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+2], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}

					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 3 + rel;
					}

					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 3 - rel;
					}
					k--;
				}
				osc_period = 24;
			}

			else if (string.endsWith("6") | string.endsWith("7")){

				if (string.endsWith("6")){
					add = ptr;
				}
				else if (string.endsWith("7")){
					add = ptr + 1;
				}
				String value = ram[Integer.parseInt(ram[add],16)];
				String temp1 = ram[224].substring(0,3);
				String temp2 = ram[224].substring(4,7);
				String temp3 = value.substring(0,3);
				String temp4 = value.substring(4,7);
				ram[224] =  temp2.concat(temp3);
				ram[add] = temp4.concat(temp1);
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=216 && Integer.parseInt(string,16)<=223 ){
				add = ptr + (Integer.parseInt(string,16) - 216);
				if(Integer.parseInt(ram[add], 16)==0){
					ram[add]="FF";
				}
				else{
					ram[add]=Integer.toHexString(Integer.parseInt(ram[add],16)-1);
				}
				if(Integer.parseInt(ram[add],16)==0){
					k++;
				}
				else{
					bin_val= Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1], 16));
					if(bin_val.length()<8){
						for(int p=0;p<8-bin_val.length();p++){
							bin_val= "0".concat(bin_val);
						}
					}
					if (bin_val.startsWith("0")){
						int rel = Integer.parseInt(bin_val, 2);
						k = k + 2 + rel;
					}
					else if (bin_val.startsWith("1")){
						int rel = 256 - Integer.parseInt(bin_val, 2);
						k = k + 2 - rel;
					}
					k--;
				}
				osc_period = 24;
			}
		}


		else if(string.startsWith("E")){

			if(string.endsWith("0")){
				ram[224] = extram[Integer.parseInt(dptr, 16)];
				osc_period = 24;
			}

			else if(string.endsWith("1")){
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;
			}

			else if(string.endsWith("2")){
				add= ptr;
				ram[224]= extram[Integer.parseInt(ram[add], 16)];
				osc_period = 24;
			}

			else if(string.endsWith("3")){
				add= ptr + 1;
				ram[224] = extram[Integer.parseInt(ram[add], 16)];
				osc_period = 24;
			}

			else if(string.endsWith("4")){
				ram[224] = "00";
				osc_period = 12;
			}

			else if (string.endsWith("5")){
				k++;
				ram[224] = ram[Integer.parseInt(Assemble.rom[k],16)];
				osc_period = 12;
			}

			else if (string.endsWith("6") | string.endsWith("7")){
				if (string.endsWith("6")){
					add = ptr;
				}
				else if (string.endsWith("7")){
					add = ptr + 1;
				}
				ram[224] = ram[Integer.parseInt(ram[add],16)];
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=232 && Integer.parseInt(string,16)<=239 ){
				add = ptr + (Integer.parseInt(string,16) - 232);
				ram[224] = ram[add];
				osc_period = 12;	
			}

		}


		else if(string.startsWith("F")){

			if(string.endsWith("0")){
				extram[Integer.parseInt(dptr, 16)] = ram[224];
				osc_period = 24;
			}
			
			else if(string.endsWith("1")){
				String temp = Integer.toHexString(k+2);
				if(temp.length()<4){
					int p = 4 - temp.length();
					for(int i=0;i<p;i++){
						temp = ("0").concat(temp);
					}
				}
				String lb2 = temp.substring(2,4);
				String hb2 = temp.substring(0,2);
				sp++;
				ram[sp] = lb2;
				sp++;
				ram[sp] = hb2;
				bin_val=Integer.toBinaryString(Integer.parseInt(string, 16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String opcode = bin_val.substring(0,3);
				bin_val = Integer.toBinaryString(Integer.parseInt(Assemble.rom[k+1],16));
				len= bin_val.length();
				if(len<8){
					int p= 8-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}
				String lb = bin_val;
				bin_val=Integer.toBinaryString(k+2);
				len= bin_val.length();
				if(len<16){
					int p= 16-len;
					for(int i=0;i<p;i++){
						bin_val= "0".concat(bin_val);
					}
				}

				String hb = bin_val.substring(0,5);
				opcode = hb.concat(opcode).concat(lb);
				k = Integer.parseInt(opcode,2);
				k--;
				osc_period = 24;

			}

			else if(string.endsWith("2")){
				add= ptr;
				extram[Integer.parseInt(ram[add], 16)] = ram[224];
				osc_period = 12;
			}

			else if(string.endsWith("3")){
				add= ptr + 1;
				extram[Integer.parseInt(ram[add], 16)]= ram[224];
				osc_period = 12;
			}

			else if(string.endsWith("4")){
				int val= Integer.parseInt(ram[224], 16);
				bin_val = Integer.toBinaryString(val);
				int p=bin_val.length();
				if(p<8){
					for(int i=0;i<8-p;i++){
						bin_val = "0".concat(bin_val);
					}
				}
				StringBuilder sb = new StringBuilder(bin_val);
				bin_val = sb.toString();

				for(int i=0;i<8;i++){
					if(bin_val.charAt(i)=='1'){
						sb.setCharAt(i, '0');
					}
					else{
						sb.setCharAt(i, '1');
					}
				}
				bin_val = sb.toString();
				val = Integer.parseInt(bin_val,2);
				ram[224] = Integer.toHexString(val);
				osc_period = 12;
			}

			else if (string.endsWith("5")){
				k++;
				ram[Integer.parseInt(Assemble.rom[k], 16)]= ram[224];
				osc_period = 12;
			}

			else if (string.endsWith("6") | string.endsWith("7")){

				if (string.endsWith("6")){
					add = ptr;
				}
				else if (string.endsWith("7")){
					add = ptr + 1;
				}
				ram[Integer.parseInt(ram[add],16)] = ram[224];
				osc_period = 12;
			}

			else if (Integer.parseInt(string,16)>=248 && Integer.parseInt(string,16)<=255 ){
				add = ptr + (Integer.parseInt(string,16) - 248);
				ram[add] = ram[224];
				osc_period = 12;
			}

		}


		bin_val=Integer.toBinaryString(Integer.parseInt(ram[224], 16));
		int counter=0;
		for(int i=0;i<bin_val.length();i++){
			if(bin_val.charAt(i)=='1'){
				counter++;
			}
		}

		if((counter%2)==1){
			P=1;
		}else{P=0;}
		ptr = updatePTR();
		ram[129] = Integer.toHexString(sp);
		k++;
		for(int i=0;i<4;i++){
			bin_val = Integer.toBinaryString(Integer.parseInt(ram[128+16*i],16));
			int p = bin_val.length();
			for(int j=0;j<8-p;j++){
				bin_val = "0".concat(bin_val);
			}
			Circuit.port_value[i] = bin_val;
		}

		for(int i =0;i<4;i++){
			System.out.println("PORT " + i + " :" + Circuit.port_value[i]);
		}

		timeline+=osc_period;
		System.out.println(osc_period+"osc period is");
		System.out.println(timeline+"timeline is");
		Internals8051.updateInternals();
		return k;

	}


	public static int updatePTR() {
		String bin_val = Integer.toBinaryString(Integer.parseInt(ram[208],16));
		int len= bin_val.length();
		if(len<8){
			int p= 8-len;
			for(int i=0;i<p;i++){
				bin_val= "0".concat(bin_val);
			}
		}

		if(bin_val.substring(3,5).equals("00")){
			ptr = 0;
		}
		if(bin_val.substring(3,5).equals("01")){
			ptr = 8;
		}
		if(bin_val.substring(3,5).equals("10")){
			ptr = 16;
		}
		if(bin_val.substring(3,5).equals("11")){
			ptr = 24;
		}

	return ptr;

	}



	public static void  writeBit(String temp, char j) {
		char ch =j;
		StringBuilder sb;
		if(Integer.parseInt(temp,16)>=0 && Integer.parseInt(temp,16)<=127){
			int div = (int) (Integer.parseInt(temp,16)/8);
			int rem = (int) (Integer.parseInt(temp,16)%8);
			String bin_val = Integer.toBinaryString(Integer.parseInt(ram[32+div],16));
			if(bin_val.length()<8){
				int p = 8 - bin_val.length();
				for(int i=0;i<p;i++){
					bin_val = ("0").concat(bin_val);
				}
			}
			sb = new StringBuilder(bin_val);
			sb.setCharAt(7-rem, ch);
			bin_val = sb.toString();
			ram[32+div] = Integer.toHexString(Integer.parseInt(bin_val,2));
		}

		else{
			int rem = (int) (Integer.parseInt(temp,16)%8);
			int bit = Integer.parseInt(Character.toString(j),2);
			String bin_val = Integer.toBinaryString(Integer.parseInt(ram[Integer.parseInt(temp,16)-rem],16));
			if(bin_val.length()<8){
				int p = 8 - bin_val.length();
				for(int i=0;i<p;i++){
					bin_val = ("0").concat(bin_val);
				}
			}
			sb = new StringBuilder(bin_val);
			sb.setCharAt(7-rem, ch);
			bin_val = sb.toString();
			ram[Integer.parseInt(temp,16)-rem] = Integer.toHexString(Integer.parseInt(bin_val,2));
		}
	}



	public static int readBit(String temp) {
		int bit;
		if(Integer.parseInt(temp,16)>=0 && Integer.parseInt(temp,16)<=127){
			int div = (int) (Integer.parseInt(temp,16)/8);
			int rem = (int) (Integer.parseInt(temp,16)%8);
			String bin_val = Integer.toBinaryString(Integer.parseInt(ram[32+div],16));
			if(bin_val.length()<8){
				int p = 8 - bin_val.length();
				for(int i=0;i<p;i++){
					bin_val = ("0").concat(bin_val);
				}
			}
			bit = (int) bin_val.charAt(7 - rem);
		}
		else{
			int rem = (int) (Integer.parseInt(temp,16)%8);
			String bin_val = Integer.toBinaryString(Integer.parseInt(ram[Integer.parseInt(temp,16)-rem],16));
			if(bin_val.length()<8){
				int p = 8 - bin_val.length();
				for(int i=0;i<p;i++){
					bin_val = ("0").concat(bin_val);
				}
			}
			bit = Integer.parseInt(Character.toString(bin_val.charAt(7 - rem)),16);
		}
		return bit;
	}
}
