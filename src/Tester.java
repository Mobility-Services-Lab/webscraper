import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tester {
	public static void main(String[] args) {
		regtester();
		System.out.println("finito");
	}
	public static void arrayListTester() {
		ArrayList<String> ls = new ArrayList<>();
		ls.add("B");
		ls.add("A");
		for(String s: ls) {
			System.out.println(s);
		}
	}
	public static void regtester(){
		String s="Name: ViewAllActivity\r\n" + 
				"Version: V1 / 1.1.0\r\n" + 
				"First Release: 8/3/2017\r\n" + 
				"Latest Release: 7/20/2017\r\n" + 
				"Languages: English\r\n" + 
				"Industries: Information not provided";
		s= "Nintex\r\n" + 
				"(425) 533-2827, opt. 1 | Website | Email\r\n" + 
				"Headquarters\r\n" + 
				"Bellevue, WA, USA\r\n" + 
				"Employees\r\n" + 
				"400";
		s = "hey salam";
		System.out.println(s.replaceAll("heyy?", "Hi"));
//		Pattern pat = Pattern.compile("(.*)([0-9]+)(.*)");
//		Pattern pat = Pattern.compile("(.*)([0-9]+)(.*)");
//		Pattern pat = Pattern.compile("Headquarters(\r\n|\n|$)(.+?)(\r\n|\n|$)");//
//		Matcher matcher = pat.matcher(s);
//		
//		if(matcher.find())
//		{
//			System.out.println(matcher.group(1));
//		}
	}
}