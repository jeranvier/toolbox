package jeranvier.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grep2List {
	
	public static void main(String[] arg){
		List<String> list = new ArrayList<String>();
		list.add("13 46095.097864315517654 7054.386046320822970 46095.097864315517654 7054.386046320822970");
		list.add("this is another test 3 with 2 times this 34");
		list.add("it's a final test for control");
		 for(String result :Grep2List.find("([0-9]+\\.[0-9]+)", list.get(0))){
			 System.out.println(result);
		 }
	}

	public static List<String> find(String stringPattern, String string) {
		if(! stringPattern.matches("\\\\(.*\\\\)")){
			stringPattern="("+stringPattern+")";
		}
		Pattern pattern = Pattern.compile(stringPattern);
		Matcher matcher = pattern.matcher(string);
		List<String> results = new ArrayList<String>();
		while (matcher.find()){
		    results.add(matcher.group(1));
		}
		return results;
	}

}
