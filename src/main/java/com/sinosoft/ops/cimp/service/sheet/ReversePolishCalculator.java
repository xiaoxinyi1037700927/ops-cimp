package com.sinosoft.ops.cimp.service.sheet;

import java.util.HashMap;
import java.util.Stack;

/**
 * @ClassName:  ReversePolishCalculator
 * @description: 逆波兰计算器
 * @author:        lixianfu
 * @date:            2018年5月31日 下午2:28:21
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class ReversePolishCalculator {
	//运算符优先级  
    private static HashMap<String,Integer> opLs;  
      
    private String src;  
  
    public ReversePolishCalculator(String src) {  
        this.src = src;  
        if(opLs==null) {  
            opLs = new HashMap<String,Integer>(6);  
            opLs.put("+",0);  
            opLs.put("-",0);  
            opLs.put("*",1);  
            opLs.put("/",1);  
            opLs.put("%",1);  
            opLs.put(")",2);  
        }  
    }  
  
    //将中缀表达式转化为后缀表达式  
    public String toRpn()  {  
        String[] tmp = split(src);  
        // 后缀栈  
        Stack<String> rpn = new Stack<String>();  
        // 临时栈  
        Stack<String> tmpSta = new Stack<String>();  
  
        for (String str : tmp) {  
            if (isNum(str)) {  
                //是操作数,直接压入结果栈  
                rpn.push('('+str+')');  
            }else{  
                //是操作符号  
                if(tmpSta.isEmpty()) {
                	//还没有符号  
                    tmpSta.push(str);  
                }else{  
                 //判读当前符号和临时栈栈顶符号的优先级  
                    if(isHigh(tmpSta.peek(),str)) {  
                        if(!str.equals(")")) {  
                            do{  
                                //1在临时栈中找出小于当前优先级的操作符  
                                //2压入当前读入操作符  
                                rpn.push(tmpSta.peek());  
                                tmpSta.pop();  
                            }while(!tmpSta.isEmpty()&&(isHigh(tmpSta.peek(),str))); 
                            
                            tmpSta.push(str);  
                        }else{  
                            //是)依次弹出临时栈的数据，直到(为止  
                            while(!tmpSta.isEmpty()&&!tmpSta.peek().equals("(")){  
                            	
                                rpn.push(tmpSta.pop());  
                            }  
                            if((!tmpSta.empty())&&(tmpSta.peek().equals("("))){
                            	//弹出(  
                                tmpSta.pop();  
                            }  
                        }  
                    }else if(!isHigh(tmpSta.peek(),str)){  
                        tmpSta.push(str);  
                    }  
                }  
            }  
  
        }  
        while(!tmpSta.empty()) {//把栈中剩余的操作符依次弹出  
            rpn.push(tmpSta.pop());  
        }  
        StringBuilder st = new StringBuilder();  
        for (String str : rpn) {  
                st.append(str);  
        }  
        rpn.clear();  
        return st.toString();  
    }  
  
    //分割(56+4)3*6+2=>(,56,+,4,  
    private String[] split(String src) {  
        StringBuilder sb = new StringBuilder(src.length());  
        for(char ch:src.toCharArray()) {  
            if(ch=='+'||ch=='-'||ch=='*'||ch=='*'||ch=='/'||ch=='('||ch==')'||ch=='%')  {  
                sb.append(",");  
                sb.append(ch);  
                sb.append(",");  
            }else{  
                sb.append(ch);  
            }  
        }  
        String string = sb.toString().replaceAll(",{2,}", ","); 
        return string.split(",");  
    }  
  
    //比较操作符的优先级  
    private boolean isHigh(String pop, String str) {  
        if(str.equals(")"))  
            return true;  
        if(opLs.get(pop)==null||opLs.get(str)==null)  
          return false;  
        return opLs.get(pop)>=opLs.get(str);  
              
    }  
  
    //是否是数字  
    public boolean isNum(String str) {  
        for (char ch : str.toCharArray()) {  
            if(ch=='+'||ch=='-'||ch=='*'||ch=='*'||ch=='/'||ch=='('||ch==')'||ch=='%')  
                return false;  
        }  
        return true;  
    }  
  
    //得到结果  
    public double getRes() {  
        String rpn = toRpn();  
        Stack<Double> res = new Stack<Double>();  
        StringBuilder sb = new StringBuilder();  
        for(char ch:rpn.toCharArray()) { 
            if(ch=='(')  {          	
                continue;    
            }else if(ch>='0'&&ch<='9'||ch=='.'){  
                sb.append(ch);  
            }else if(ch==')')  {   
            		if (sb != null) {
            			res.push(Double.valueOf(sb.toString()));  
                        sb = new StringBuilder();	
					}
            				
            }else{  
                 if(!res.empty())  {  
                     Double x = res.pop();  
                     Double y = res.pop();  
                     switch (ch) {  
                    case '+':  
                         res.push(y+x);   
                        break;  
                    case '-':  
                        res.push(y-x);   
                        break;  
                    case '*':  
                        res.push(y*x);   
                        break;    
                    case '%':  
                    case '/':     
                        if(x!=0)  {  
                            double rsd = ch=='%'?y%x:y/x;  
                            res.push(rsd);   
                        }else{  
                             System.out.println("分母为零");  
                             res.clear();  
                             return -1;  
                        }  
                        break;  
                    }  
                 }  
            }  
        }  
        Double result = res.pop();  
        res.clear();  
        return result;  
    }  
  
    public static void main(String[] args) {  
    	//测试用例
        //String str = "1+2*3-4*5-6+7*8-9"; //123*+45*-6-78*+9-
        //String str = "a*(b-c*d)+e-f/g*(h+i*j-k)"; // abcd*-*e+fg/hij*+k-*-
        //String str = "6*(5+(2+3)*8+3)"; //6523+8*+3+*
        //String str = "a+b*c+(d*e+f)*g"; //abc*+de*f+g*f
    	String str1 = "(15.3+2)+2+3";
    	if ( str1.charAt(0) == '(') {
    		if (str1.charAt(1) == '(') {
    			String str = str1.substring(2);
    			ReversePolishCalculator analyer = new ReversePolishCalculator(str);
    			System.out.println(str1+"="+analyer.getRes());  
			}
    		String str = str1.substring(1, str1.length());
            ReversePolishCalculator analyer = new ReversePolishCalculator(str);  
            System.out.println(str1+"="+analyer.getRes());  
    	}else {
    		ReversePolishCalculator analyer = new ReversePolishCalculator(str1);  
            System.out.println(str1+"="+analyer.getRes()); 
		}    	  
        
    }  
}
