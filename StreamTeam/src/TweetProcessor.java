
public class TweetProcessor {
	public static void add(String s){
		
	}
	public static String getAlphabeticStringParts(String token)
	 {
	  char[] ch = token.toCharArray();
	  for (int i = 0; i < token.length(); i++)
	  {
	   if (org.apache.commons.lang.CharUtils.isAsciiAlpha(ch[i]))
	   {
	   } else if (ch[i] == '#' || ch[i] == 'Ä' || ch[i] == 'Ü' || ch[i] == 'Ö' || ch[i] == 'ä' || ch[i] == 'ü' || ch[i] == 'ö' || ch[i] == 'ß' || ch[i] == 'é')
	   {
	   } else
	   {
	    ch[i] = ' ';
	   }
	  }
	  return new String(ch);
	 }

}
