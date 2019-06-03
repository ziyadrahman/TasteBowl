package com.syntax.tastebowl.Common;

public class Mybmr {
	String gender;
	float wkg;
	float hinc;
	float age;
	public Mybmr(float wkg, float hinc, float age, String gender){
		this.age=age;
		this.wkg=wkg;
		this.hinc=hinc;
		this.gender=gender;
	}
	
	public String calculateBmr(){
		
		if(gender.equals("Male")){
			float result= (float) (((66)+(13.7*wkg))+((5*hinc)-(6.8*age)));
			return ""+result;
			
		}
		if(gender.equals("Female")){
			float result= (float) (((655)+(9.6*wkg))+((1.8*hinc)-(4.7*age)));
			return ""+result;
			
		}
		
		
		
		
		
		return "sorry";
	}

}
