//Victor Delgado
//P. 216

public class DivideEvenly {
	public static void main(String[] args) {
		final int limit = 100;
		int var;

		System.out.print(limit + " is evenly divisible by ");
		for (var = 1; var <= limit; ++var) {
			if (limit % var == 0) {
				System.out.print(var + " ");
			}
		}
		System.out.println();
	}
}
