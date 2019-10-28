package tip.tdc.com.tip.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccentRemover {
	
	
	public static char[] SPECIAL_CHARACTERS = {
			'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê',
			'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ũ', 'à', 'á', 'â', 'ã',
			'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă',
			'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ',
			'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ',
			'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ',
			'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ',
			'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ',
			'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ',
			'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ',
			'Ự', 'ự', 'Ý', 'ỷ', 'Ỷ', 'ỳ', 'Ỳ', 'ỹ', 'Ỹ', 'ỵ', 'Ỵ'};
	private static char[] REPLACEMENTS = {
			'A', 'A', 'A', 'A', 'E', 'E', 'E',
			'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'U', 'a', 'a', 'a', 'a',
			'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A',
			'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a',
			'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
			'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e',
			'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I',
			'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
			'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
			'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
			'U', 'u', 'Y' ,'y', 'Y', 'y', 'Y', 'y', 'Y', 'y', 'Y'};


	private static final String[] RootForms = new String[]{
			"aáàảãạâấầẩẫậăắằẳẵặ", "eéèẻẽẹêếềểễệ", "iíìỉĩị", "oóòỏõọôốồổỗộơớờởỡợ", "uúùủũụưứừửữự", "yýỳỷỹỵ", "dđ",
			"AÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶ", "EÉÈẺẼẸÊẾỀỂỄỆ", "IÍÌỈĨỊ", "OÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢ", "UÚÙỦŨỤƯỨỪỬỮỰ", "YÝỲỶỸỴ", "DĐ"
	};

	private static final Map<Character, Character> ROOT_FORM = new HashMap<>();
	static {
		for (String rootForm : RootForms) {
			if (rootForm.length() > 1) {
				char rootChar = rootForm.charAt(0);
				ROOT_FORM.put(rootChar, rootChar);
				for (int i=1; i<rootForm.length(); i++) {
					ROOT_FORM.put(rootForm.charAt(i), rootChar);
				}
			}
		}
	}

	private static final String ACCEPTED_CHARS = " .";
	private static final Set<Character> SPECIAL_PASS = new HashSet<>();
	static {
		for (int i=0; i<ACCEPTED_CHARS.length(); i++) {
			SPECIAL_PASS.add(ACCEPTED_CHARS.charAt(i));
		}
	}

	public static final String preProcess(String text) {
		StringBuilder rez = new StringBuilder(text.length());
		for (int i=0; i<text.length(); i++) {
			char ch = text.charAt(i);
			if (ROOT_FORM.containsKey(ch) || (ch >= 'a' && ch <= 'z')
					|| (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || SPECIAL_PASS.contains(ch)) {
				rez.append(ch);
			}
		}
		return rez.toString();
	}

	public static final String removeAccent(String text) {
		text = preProcess(text);
		StringBuilder rez = new StringBuilder(text.length());
		for (int i=0; i<text.length(); i++) {
			rez.append(ROOT_FORM.containsKey(text.charAt(i)) ? ROOT_FORM.get(text.charAt(i)) : text.charAt(i));
		}
		return rez.toString();
	}

	private static HashMap<Character, Character> mapVNese = new HashMap<>();

//	public static String toUrlFriendly(String s) {
//		int maxLength = Math.min(s.length(), 236);
//		char[] buffer = new char[maxLength];
//		int n = 0;
//		for (int i = 0; i < maxLength; i++) {
//			char ch = s.charAt(i);
//			buffer[n] = removeAccent(ch);
//			// skip not printable characters
//			if (buffer[n] > 31) {
//				n++;
//			}
//		}
//		// skip trailing slashes
//		while (n > 0 && buffer[n - 1] == '/') {
//			n--;
//		}
//		return String.valueOf(buffer, 0, n);
//	}

//	private static char removeAccent(char ch) {
//		int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
//		if (index >= 0) {
//			ch = REPLACEMENTS[index];
//		}
//		return ch;
//	}

	
//	public static String removeAccent(String s) {
//		String result = s;
//		for (int i = 0; i < SPECIAL_CHARACTERS.length; i++) {
//			result = result.replace(SPECIAL_CHARACTERS[i], REPLACEMENTS[i]);
//		}
//		return result;
//	}
	static int[] SPECIAL_CHARACTERS_INT = new int[SPECIAL_CHARACTERS.length];
	static {
		for (int i = 0; i < SPECIAL_CHARACTERS.length; i++) {
			SPECIAL_CHARACTERS_INT[i] = (int) SPECIAL_CHARACTERS[i];

			mapVNese.put(SPECIAL_CHARACTERS[i], REPLACEMENTS[i]);
		}
	}

//
//	private static char removeAccent(char ch) {
//
//
//		int index = Arrays.binarySearch(SPECIAL_CHARACTERS_INT, (int)ch);
//		if (index >= 0) {
//			ch = REPLACEMENTS[index];
//		}
//
//		if(ch == 'ừ') {
//			System.out.print("FUCK" + index);
//		}
//		return ch;
//	}

	public static String removeAccent1(String s) {
		String res = "";
		for (int i = 0; i < s.length(); i++) {
			if(mapVNese.containsKey(new Character(s.charAt(i)))) {
				res += mapVNese.get(new Character(s.charAt(i)));
			} else {
				res += s.charAt(i);
			}
		}
		return res;
	}
	
	
}
