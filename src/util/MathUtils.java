package util;

public final class MathUtils{
	
	/**
	 * Find the greatest common divisor of two integers
	 * @param a The first number
	 * @param b The second number
	 */
	public static int gcd(int a, int b){
		if(b == 0) return a;
		return gcd(b, a % b);
	}
	
	/** Cannot make instances of {@link MathUtils} */
	private MathUtils(){}
	
}
