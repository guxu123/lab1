import java.awt.List;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class multinomial {
	
	public static void main(String[] args){
		StringBuilder complete=new StringBuilder();
		multinomial mul=new multinomial();
		
		InputStream is=null;
		StringBuilder sb=null;
		StringBuilder mntmp = null,mymn=null;
		int ch,flag,res,finalres;
		char c='\0',prec='\0';
		String str,mn="\0",mnstr,cmd1,cmd2;
		String []list,tmp;
		String []partList,finalList;
		StringBuilder strtmp=null,result=null,finalresult;
		try{
			is=System.in;
			sb=new StringBuilder();
			while(true){
				c='\0';
				prec='\0';
				if((ch=is.read())!='\n'){
					c=(char)ch;					
					sb.append(c);
				}else{
					str=sb.toString();
					sb.delete(0, sb.length());
					if(str.startsWith("!simplify")){        //simplify
						if(mn=="\0"){
							System.out.println("Please input the multinomial first!");
							continue;
						}
						
						//fix here
						if(str.equals("!simplify\r")|| str.equals("!simplify \r")){
							mul.merge(mn,complete);
							System.out.println(complete);
							complete.delete(0, complete.length());
							continue;
						}
						
						cmd1=str.substring(10,str.length()-1);
						list=cmd1.split(" ");
												
						flag=0;
						StringBuilder st=new StringBuilder();
					    for(int i=0;i<list.length;i++){
					    	if(!list[i].matches("^[a-zA-Z]+={1}[0-9]+")){
					    		System.out.println("Invalid Simplify");
					    		return;
					    	}
					        Pattern pattern = Pattern.compile("(.*?)=");
					        Matcher matcher = pattern.matcher(list[i]);					         
					        while (matcher.find()) {
					        	st.append(matcher.group());
					        }
					        st.delete(st.length()-1,st.length());
					        if(mn.contains(st)){
					        	flag++;
					        }
					        st.delete(0, st.length());
					    }
					    if(flag!=list.length){
					    	System.out.println("There is no such par in the multinomial");
					    	continue;
					    }
							
						for(int i=0;i<list.length;i++){
							if(!(list[i].matches("^[a-zA-Z]+={1}-?[0-9]+\r?\n?$"))){
								System.out.println("Invalid simplify forms");
								continue;
							}
						}
						
						System.out.println("Valid Simplify!");
						//cal
						mntmp=new StringBuilder();
						mnstr=mn;
						int loc=0;
						for(String a : list){
							loc=a.indexOf('=');
							mnstr=mnstr.replace(a.substring(0, loc), a.substring(loc+1));
													
						}
						mntmp.append(mnstr);
						strtmp=new StringBuilder();
						result=new StringBuilder();
						partList=mntmp.toString().split("\\+");
						for(String a:partList){
							tmp=a.split("\\*");
							res=1;
							for(String b:tmp){
								if(b.matches("^[a-zA-Z]+$")){
									strtmp.append("*"+b);
								}else{
									res*=Integer.parseInt(b);
								}
							}
							result.append(res);
							result.append(strtmp+"+");
							strtmp.delete(0, strtmp.length());
						}
						finalList=result.toString().split("\\+");
						finalres=0;
						finalresult=new StringBuilder();
						for(String a:finalList){
							if(a.matches("^[0-9]+$")){
								finalres+=Integer.parseInt(a);
							}else{
								strtmp.append(a+"+");
							}
						}
						if(!strtmp.toString().isEmpty())
							finalresult.append(strtmp);
						if(finalres!=0)
							finalresult.append(finalres+"+");
						finalresult.delete(finalresult.length()-1,finalresult.length());
						mul.merge(finalresult.toString(),complete);
						System.out.println("The simplify result is: "+complete);
						complete.delete(0, complete.length());
						
					}else if(str.startsWith("!d/d")){        //d/d*
						cmd2=str.substring(5,6);						
						
						if(mn=="\0"){
							System.out.println("Please input the multinomial first!");
							continue;
						}
						
						if(cmd2.length()>2){
							System.out.println("Too many pars");
							continue;
						}
						System.out.println("Valid d/d*!");
						
						flag=0;
						for(int i=0;i<mn.length();i++){
							if(cmd2.equals(String.valueOf(mn.charAt(i)))){
								flag=1;
							}
						}
						if(0==flag){
							System.out.println("There is no such par in the multinomial");
							continue;
						}			
					}else{
						mn=str.substring(0,str.length()-1);
						/*
						flag=0;
						for(int i=0;i<mn.length();i++){
							c=mn.charAt(i);
							if(!Character.isDigit(c) && !Character.isLetter(c) && (c!='*' && c !='+'&&c!='\r'&&c!='\n')){
								System.out.println("Invalid Multinomial");
								flag++;
								break;
							}else if(Character.isDigit(c)||((prec=='\0'||prec=='*'||prec=='+') && Character.isLetter(c))||(c=='\r'||c=='\n')
									||((Character.isLetter(prec) || Character.isDigit(prec)) && (c=='*'||c=='+'||c=='\r'))){
								;
							}else{
								System.out.println("Invalid Multinomial");		
								flag++;
								break;
							}
							prec=c;
						}
						if(0==flag){
							System.out.println("Valid Multinomial!\nyour mm is: "+mn);						
						}*/
					}
					
				}
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
		finally{
			try{if(is!=null)
				is.close();
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}
	}
	
	
	
	
	
	///HERE
	
	
	
	
	public void merge(String mn, StringBuilder complete){ 
		String[] list,part,tmp;
		String cpl;
		list=mn.split("\\+");
		Map<String,Integer> maptotal=new HashMap<String,Integer>();
		Map<String,Integer> mappart=new HashMap<String,Integer>();
		for(String a:list){
			part=a.split("\\*");
			for(String b:part){
				if(b.matches("^[a-zA-Z]+$")){
					if(mappart.containsKey(b)){
						mappart.put(b, mappart.get(b)+1);
					}else{
						mappart.put(b,1);
					}	
				}
			}
			
			for(String b:part){
				if(b.matches("^-?[0-9]+$")){	
					if(maptotal.containsKey(mappart.toString())){							
						maptotal.put(mappart.toString(), maptotal.get(mappart.toString())+Integer.parseInt(b));
					}else{
						maptotal.put(mappart.toString(), Integer.parseInt(b));
					}				
					break;
				}
			}
			mappart.clear();

		}
		
		for(Entry<String,Integer> entry : maptotal.entrySet()){
			complete.append(entry.getValue()+"*");
			if(entry.getKey()=="{}"){
				complete.delete(complete.length()-1, complete.length());
				complete.append('+');
				continue;
			}
			part=entry.getKey().substring(1,entry.getKey().length()-1).split(", ");
			
			for(String a:part){
				tmp=a.split("=");
				//System.out.println(tmp);
				for(int i=0;i<Integer.parseInt(tmp[1]);i++){
					complete.append(tmp[0]+"*");
				}				
			}
			complete.delete(complete.length()-1, complete.length());
			complete.append("+");
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		
		complete.delete(complete.length()-1, complete.length());
		cpl=complete.toString();
		cpl=cpl.replace("+-", "-");
		complete.delete(0, complete.length());
		complete.append(cpl);
		System.out.println(maptotal);
	}
	/*
	public void simplify(String str,String mn,StringBuilder complete){
		String cmd1,mnstr;
		String[] list,partList,tmp,finalList;
		int flag,res,finalres;
		StringBuilder mntmp,strtmp,result,finalresult;
		multinomial mul;
		if(str.startsWith("!simplify")){        //simplify
						mul=new multinomial();
						if(mn=="\0"){
							System.out.println("Please input the multinomial first!");
							continue;
						}
						//fix here
						if(str.equals("!simplify\r")|| str.equals("!simplify \r")){
							mul.merge(mn,complete);
							complete.delete(0, complete.length());
							continue;
						}
						cmd1=str.substring(10,str.length()-1);
						list=cmd1.split(" ");
												
						flag=0;
						StringBuilder st=new StringBuilder();
					    for(int i=0;i<list.length;i++){
					        Pattern pattern = Pattern.compile("(.*?)=");
					        Matcher matcher = pattern.matcher(list[i]);					         
					        while (matcher.find()) {
					        	st.append(matcher.group());
					        }
					        st.delete(st.length()-1,st.length());
					        if(mn.contains(st)){
					        	flag++;
					        }
					        st.delete(0, st.length());
					    }
					    if(flag!=list.length){
					    	System.out.println("There is no such par in the multinomial");
					    	continue;
					    }
							
						for(int i=0;i<list.length;i++){
							if(!(list[i].matches("^[a-zA-Z]+={1}-?[0-9]+\r?\n?$"))){
								System.out.println("Invalid simplify forms");
								continue;
							}
						}
						
						System.out.println("Valid Simplify!");
						//cal
						mntmp=new StringBuilder();
						mnstr=mn;
						int loc=0;
						for(String a : list){
							loc=a.indexOf('=');
							mnstr=mnstr.replace(a.substring(0, loc), a.substring(loc+1));
													
						}
						mntmp.append(mnstr);
						System.out.println(mntmp);
						strtmp=new StringBuilder();
						result=new StringBuilder();
						partList=mntmp.toString().split("\\+");
						for(String a:partList){
							tmp=a.split("\\*");
							res=1;
							for(String b:tmp){
								if(b.matches("^[a-zA-Z]+$")){
									strtmp.append("*"+b);
								}else{
									res*=Integer.parseInt(b);
								}
							}
							result.append(res);
							result.append(strtmp+"+");
							strtmp.delete(0, strtmp.length());
						}
						finalList=result.toString().split("\\+");
						finalres=0;
						finalresult=new StringBuilder();
						for(String a:finalList){
							if(a.matches("^[0-9]+$")){
								finalres+=Integer.parseInt(a);
							}else{
								strtmp.append(a+"+");
							}
						}
						if(!strtmp.toString().isEmpty())
							finalresult.append(strtmp);
						if(finalres!=0)
							finalresult.append(finalres+"+");
						finalresult.delete(finalresult.length()-1,finalresult.length());
						mul.merge(finalresult.toString(),complete);
						System.out.println("The simplify result is: "+complete);
						complete.delete(0, complete.length());
		}
	}*/
	
	public void cal(String mn,StringBuilder complete){
		String mnstr;
		String[] partList,tmp,finalList;
		int res,finalres;
		StringBuilder mntmp,strtmp,result,finalresult;
		multinomial mul;
		mntmp=new StringBuilder();
		mnstr=mn;
		
		mntmp.append(mnstr);
		strtmp=new StringBuilder();
		result=new StringBuilder();
		partList=mntmp.toString().split("\\+");
		for(String a:partList){
			tmp=a.split("\\*");
			res=1;
			for(String b:tmp){
				if(b.matches("^[a-zA-Z]+$")){
					strtmp.append("*"+b);
				}else{
					res*=Integer.parseInt(b);
				}
			}
			result.append(res);
			result.append(strtmp+"+");
			strtmp.delete(0, strtmp.length());
		}
		finalList=result.toString().split("\\+");
		finalres=0;
		finalresult=new StringBuilder();
		for(String a:finalList){
			if(a.matches("^[0-9]+$")){
				finalres+=Integer.parseInt(a);
			}else{
				strtmp.append(a+"+");
			}
		}
		if(!strtmp.toString().isEmpty())
			finalresult.append(strtmp);
		if(finalres!=0)
			finalresult.append(finalres+"+");
		finalresult.delete(finalresult.length()-1,finalresult.length());
		mul=new multinomial();
		mul.merge(finalresult.toString(),complete);
		System.out.println("The simplify result is: "+complete);
	}
}