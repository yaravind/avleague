package com.aravind.avl.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class StringUtil
{
	public static String capitalizeFirstLetter(String str)
	{
		if (StringUtils.isNotBlank(str))
		{
			return WordUtils.capitalize(str.trim());
		}
		return str;
	}
}