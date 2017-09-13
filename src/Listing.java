import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import utils.Status;
public class Listing {
	public static ArrayList<String> headers = new ArrayList<>(Arrays.asList("Name", "Subtitle", "Number of Reviews",
			"Intro", "Release date", "Pricing", "Categories", "Rating", "Producer", "Details", "Company Website",
			"Email", "Sales Phone", "Salesforce Editions", "First Release", "Latest Release", "Version", "Language(s)",
			"Industries", "Supported Features", "Employees", "About Provider", "Year Founded", "Headquarters",
			"_5Stars", "_4Stars", "_3Stars", "_2Stars", "_1Stars", "URL"));
	private Map<String, String> detail;
	private ArrayList<Review> reviews;
	private String URL;
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
		addDetail("URL", uRL);
	}

	private Status processed = Status.OPEN;

	public void setStatus(Status stat) {
		this.processed = stat;
	}

	public Status getStatus() {
		return processed;
	}

	public Map<String, String> getAll() {
		return detail;
	}

	public Listing() {
		detail = new TreeMap<String, String>();
		reviews = new ArrayList<>();
	}

	public void addDetail(String k, String v) {
		detail.put(k, v);
	}

	public void addReview(Review rev) {
		reviews.add(rev);
	}

	public int countReviews() {
		return reviews.size();
	}

	public String getDetail(String k) {
		return detail.get(k);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "{\n";
		for (String header : headers) {
			s += "\t" + header + ": '" + detail.get(header) + "',\n";
		}
		// for(Entry<String, String>t :detail.entrySet()) {
		// s+= "\t"+t.getKey()+": '"+t.getValue()+"',\n";
		// }
		s += "\treviews: (" + reviews.size() + ") [";
//		int i = 0;
		for (Review rev : reviews) {
//			i++;
			s += "\n\t\t'" + rev + "',\n";
//			if (i == 10) {
//				s+= "and others...";
//				break;
//			}
				
		}
		s += "\t]\n}\n";
		return s;
	}

	/**
	 * Returns the CSV representation of the row.
	 * 
	 * @return
	 */
	public String toCSVLine() {
		String res = "";
		for (String header : headers) {
			res += prepCSVCell(detail.get(header)) + ",";
		}
		// for (Entry<String, String> t : detail.entrySet()) {
		// res += prepCSVCell(t.getValue()) + ",";
		// }
		for (Review rev : reviews) {
			res += prepCSVCell(rev.toString()) + ",";
		}
		return res + "\n";
	}

	/**
	 * Gets a string, and prepares it for insertion in a CSV file. The CSV is
	 * Comma-separated, so in case of finding commas, the string is surrounded by
	 * double quotes, and any pre-existing double quote is replaced by two double
	 * quotes. http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm Returns an empty
	 * string in case of null.
	 * 
	 * @param s
	 *            the string of the cell.
	 * @return the string of the cell, after probable changes, or an emptry string,
	 *         if null is given.
	 */
	public static String prepCSVCell(String s) {
		if (s == null) {
			return "";
		}
		if (s.contains(",") || s.contains("\n")) {
			s = "\"" + s.replaceAll("\"", "\"\"") + "\"";
		}
		return s;
	}
	public static String retrieveCSVCell(String s) {
		if(s.contains("\"")) {
			// remove the leading and trailing double quotes, and replace any internal
			// double quote-pairs (\"\") with only one. 
			s = s.substring(1).substring(0, s.length()-2).replaceAll("\"\"", "\""); 
		}
		return s;
	}
}