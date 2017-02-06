package utils.secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**将传入的是string**/
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


	/**hongyang的方法**/

	//1.hongyang
	/**将字符串转化为字节数组**/
	public static byte[] strToByteMD5(String str){
		byte[] digset=null;
		/**对应可以获取SHA1的加密方式**/
		try {
			MessageDigest messageDigest=MessageDigest.getInstance("MD5");
			digset=messageDigest.digest(str.getBytes());
			return digset;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**将字符串以SHA1方式转码**/
	public	static byte[] strToByteSHA1(String str){

		byte[] digest=null;
		try {
			MessageDigest messageDigest=MessageDigest.getInstance("SHA1");
			digest=messageDigest.digest(str.getBytes());
			return digest;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**将字节数组转化为16进制字符串**/
	public static String byteToStr(byte[] by){
		//创建匹配规则
		final String HEX="0123456789abcdef";

		StringBuilder sb=new StringBuilder(by.length*2);
		//每个字节数组进行遍历取高位和地位进行按位与
		for(byte b:by){
			// 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			// 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt(b & 0x0f));
		}

		/**高四位和第四位匹配按位与在结合**/
		return sb.toString();
	}
}
