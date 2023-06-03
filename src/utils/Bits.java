package utils;

import com.google.common.io.BaseEncoding;

/**
 * This class contains several utility methods for manipulating bit sequences
 * and producing bit representations of primitive data types.
 * 
 * @author Matteo Loporchio
 */
public final class Bits {
	
	/**
	 * Constructs an array of bytes parsed from a hexadecimal string.
	 * @param str a hexadecimal string
	 * @return an array of bytes parsed from a hexadecimal string
	 */
	public static byte[] fromHex(String str) {
		return BaseEncoding.base16().decode(str.toUpperCase());
	}
	
	/**
	 * Counts the number of bits equal to 1 in the given array of bytes.
	 * @param data the array of bytes
	 * @return number of ones in the array
	 */
	public static int countOnes(byte[] data) {
		int result = 0;
		for (int i = 0; i < data.length; i++)
			result += Integer.bitCount(data[i] & 0xff);
		return result;
	}
}
