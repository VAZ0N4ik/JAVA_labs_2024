package myfirstpackage;

public class MyFirstPackage {
	private int num1;
	private int num2;
	
	// constructor
	public MyFirstPackage(int num1, int num2) {
		this.num1 = num1;
		this.num2 = num2;
	}
	
	// getter for num1
	public int getNum1() {
		return num1;
	}
	
	// setter for num1
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	
	// getter for num2
	public int getNum2() {
		return num2;
	}
	
	// setter for num2
	public void setNum2(int num2) {
		this.num2 = num2;
	}
	
	// "bitwise and" operation (variant 8)
	public int operate () {
		return num1 & num2;
	}
}