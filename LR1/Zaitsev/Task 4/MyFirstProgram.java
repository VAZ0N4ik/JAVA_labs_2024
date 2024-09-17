import myfirstpackage.*;

class MyFirstClass {
 	public static void main(String[] s) {
		myfirstpackage.MyFirstPackage o = new myfirstpackage.MyFirstPackage(0, 0);
     	for (int i = 1; i <= 8; ++i) {
			for (int j = 1; j <= 8; ++j) {
				o.setNum1(i);
				o.setNum2(j);
				System.out.print(o.operate());
				System.out.print(" ");
			}
			System.out.println();
		}
 	}
}