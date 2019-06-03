package com.syntax.tastebowl.Common;

public class MyBmi {
float weight;
float height;
public MyBmi(float weight, float height){
	this.weight=weight;
	this.height=height;
}

public float calculateBmi(){
	
	float result=(weight)/(height*height);
	return result;
}


}
