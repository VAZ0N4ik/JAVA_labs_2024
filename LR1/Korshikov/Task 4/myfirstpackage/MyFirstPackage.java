package myfirstpackage;
public class MyFirstPackage {
	private int first;
	private int second;
	public MyFirstPackage(int f, int s){
		first = f;
		second = s;
	}
	public void setFirst(int f){
		first = f;
	}
	public void setSecond(int s){
		second = s;
	}
	public int getFirst(){
		return first;
	}
	public int getSecond(){
		return second;
	}
	public int sum(int x, int y){
		return x + y;
	}
}
