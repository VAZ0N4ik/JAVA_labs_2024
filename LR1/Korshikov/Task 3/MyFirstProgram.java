class MyFirstClass {
	public static void main(String[] s) {
		MySecondClass o = new MySecondClass(1,2);
		System.out.println(o.sum());
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				o.setFirst(i);
				o.setSecond(j);
				System.out.print(o.sum());
				System.out.print(" ");
			}
		System.out.println();

		}

	}
}
class MySecondClass {
	private int first;
	private int second;
	public MySecondClass(int f, int s){
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
	public int sum(){
		return first + second;
	}
}
	
