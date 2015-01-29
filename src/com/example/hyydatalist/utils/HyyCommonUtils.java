package com.example.hyydatalist.utils;

import java.util.Random;

public class HyyCommonUtils {
	static final Random rd = new Random();
	
	static final String[] POKER_FACES = { ">_<", "(︶^︶)", "~@^_^@~", "(￣（工）￣)",
			"(￣▽￣)", "~@^_^@~", "⊙﹏⊙", "(>﹏<)", "└(^o^)┘", "=^_^=", "(⊙o⊙)",
			"o(∩_∩)o...", "(=^_^=)", "(-_^)", "(^_-)", "::>_<::", "(*ω*)",
			"(0^◇^0)", "( $ _ $ )", "(☆_☆)", "^(00)^", "(・(ｪ)・)", "→_→",
			"*^◎^*", "╯﹏╰", "(×_×)", ">3<", "（づ￣ 3￣)づ", "(-ิo-ิ)", "(￣﹁￣)",
			"（#￣▽￣#）", "O-(///￣皿￣)☞ ─═≡☆゜★█▇▆▅▄▃▂_", "○(￣^￣) ○─═★° " };

	public static int getHandSetSDKVer() {

		int handSetInfo = android.os.Build.VERSION.SDK_INT;

		return handSetInfo;

	}

	public static String getPokerFaceRandom() {
		int i = rd.nextInt(POKER_FACES.length);
		return POKER_FACES[i];
	}
}
