package com.sinosoft.ops.cimp.service.sheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressTrans {
	//R1C1:R1C10
	public String [][] expressTransM(String [][] exp){
		
		String[] spl;
		String[] r1;
		String[] c1;
		String[] r2;
		String[] c2;
		int countR = 0;
		int countC = 0;
		int countS = 0;
		int countB = 0;
		StringBuilder sbd = new StringBuilder();
		
		for(String []  row : exp){
			for(String s : row){
            	spl = s.split(":");
            	r1 = spl[0].split("R");
            	c1 = r1[1].split("C");
            	r2 = spl[1].split("R");
            	c2 = r2[1].split("C");
           			
            	if(c1[0].equals(c2[0])) {
            		
            		countR = Integer.parseInt(c1[0]);
            		int cx = Integer.parseInt(c1[1]);
            		int cy = Integer.parseInt(c2[1]);
            			if(cx>cy) {
            				countB = cx;
            				countS = cy;
            			}else
            				countB = cy;
            				countS = cx;
              	}else {
              		
            		countC = Integer.parseInt(c1[1]);
            		int cx = Integer.parseInt(c1[0]);
            		int cy = Integer.parseInt(c2[0]);
            			if(cx>cy) {
            				countB = cx;
            				countS = cy;
            			}else
            				countB = cy;
            				countS = cx;
            	}
            }
        }
		for (int i = countS; i <= countB; i++) {
			sbd.append("R"+String.valueOf(countR)+"C"+String.valueOf(i)+"+");
		}
		
		String sub = sbd.substring(0, sbd.length()-1);
		String [][] sta = {{sub}};
		return sta;
	}
	
	//R1C[2,4,6,8]   R[2,4,6,8]C1
	public String [][] expressTransDK(String [][] exp){
		
		String[] r;
		String[] c;
		StringBuilder sbd = new StringBuilder();
		
		for(String []  row : exp){
			for(String s : row){
				r = s.split("R");
				c = r[1].split("C");
				
				Pattern p = Pattern.compile("[^0-9]");  
				Matcher mr = p.matcher(c[0]);  
				Matcher mc = p.matcher(c[1]);  				
				int mrt = Integer.parseInt(mr.replaceAll("").trim());
				int mct = Integer.parseInt(mc.replaceAll("").trim());

				if(mrt > mct) {
					String srb = String.valueOf(mrt);
					String scb = String.valueOf(mct);
					char[] ch = srb.toCharArray();
					for (int i = 0; i < ch.length; i++) {
					sbd.append("R"+ch[i]+"C"+scb+"+");
					}
				}else {
					String scb = String.valueOf(mct);
					String srb = String.valueOf(mrt);
					char[] ch = scb.toCharArray();
					for (int i = 0; i < ch.length; i++) {
					sbd.append("R"+srb+"C"+ch[i]+"+");
					}
				}
			}
		}
		String sub = sbd.substring(0, sbd.length()-1);
		String [][] sta = {{sub}};
		return sta;
	}
	
	//R[2,4,6,8]C[1,2]
	private String[][] expressTransSK(String[][] exp) {
		
		String[] r;
		String[] c;
		StringBuilder sbd = new StringBuilder();
		
		for(String []  row : exp){
			for(String s : row){
				r = s.split("R");
				c = r[1].split("C");
				
				Pattern p = Pattern.compile("[^0-9]");  
				Matcher mr = p.matcher(c[0]);  
				Matcher mc = p.matcher(c[1]);  				
				int mrt = Integer.parseInt(mr.replaceAll("").trim());
				int mct = Integer.parseInt(mc.replaceAll("").trim());

					String srb = String.valueOf(mrt);
					String scb = String.valueOf(mct);
					char[] chr = srb.toCharArray();
					char[] chc = scb.toCharArray();
					for (int i = 0; i < chr.length; i++) {
						for (int j = 0; j < chc.length; j++) {
							sbd.append("R"+chr[i]+"C"+chc[j]+"+");
						}
					}
			}
		}
		String sub = sbd.substring(0, sbd.length()-1);
		String [][] sta = {{sub}};
		return sta;
	}	
	//测试
	public static void main(String[] args) {
		String[][] arrExcel = new String[10][10];
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				arrExcel[i][j]=Double.toString(Math.random());
			}
		}
		String [][] expression1 = {/*{"R1C1:R1C10"},*//*{"R1C[2,4,6,8]"},*//*{"R[2,4,6,8]C1"}*/{"R[2,4,6,8]C[1,2]"}}; 
		ExpressTrans expts = new ExpressTrans();
//		String[][] expressTrans = expts.expressTransM(expression1);
//		String[][] expressTrans = expts.expressTransDK(expression1);
		String[][] expressTrans = expts.expressTransSK(expression1);

		for(String []  row : expressTrans ){
            for(String s : row){
                System.out.print(s);
            }
        }
	}


}
