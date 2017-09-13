
//import org.ini4j.Ini;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Conf {
	private static String configFile = "./httpsappexchange.salesforce.com[slash]components.json";
	public static int readerBufferSize = 1024 * 1024;
	public static String uRLsCachePath;
	public static String outputFile;
	public static int scrapingThreads = 4; // default 4. OPTIONAL PARAM.
	public static boolean URLsCached = false;
	public static int START_INDEX; // starting from zero
	public static int END_INDEX = Integer.MAX_VALUE; // default is the max value. OPTIONAL PARAM.
	public static String[] INITIAL_URLS;
	public static String reviewsFolderPath;
	
	public static String XPATH_LISTINGS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[2]/DIV/DIV/SPAN[2]/SPAN/UL/LI/SPAN/DIV[1]/DIV[1]/DIV[1]/DIV[3]/DIV[1]/A";
	// Caution: XPATH_NEXT_PAGE_BUTTON points to more than one ANCHOR:
	public static String XPATH_NEXT_PAGE_BUTTON = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[3]/DIV/DIV/DIV/A";
	public static String XPATH_LISTING_NAME = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/H1";
	public static String XPATH_SUBTITLE = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/P";
	public static String XPATH_REVIEW_NUMBER = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/SPAN/P";
	public static String XPATH_INTRO = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV/P[2]/SPAN";
	public static String XPATH_RELEASE_DATE = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN/P";
	public static String XPATH_PRICING = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN[2]/STRONG/SPAN";
	public static String XPATH_CATEGORIES = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN[3]/SPAN/P";
	public static String XPATH_PRODUCER = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV/P/B";
	public static String XPATH_DETAIL_TAB = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[2]/A";
	public static String XPATH_DETAILS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV/DIV/DIV";
	public static String XPATH_WEBSITE_OR_EMAIL = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV/DIV/DIV/P/A";
	public static String XPATH_SALES_PHONE = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV/DIV/DIV/P/SPAN/SPAN";
	public static String XPATH_SALESFORCE_EDITIONS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/SPAN/DIV/DIV[2]/DIV/UL/LI";
	public static String XPATH_TECH_DETAILS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV[2]/DIV/DIV/UL";
	public static String XPATH_SUPPORTED_FEATURES = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV[2]/SPAN/DIV";
	public static String XPATH_PROVIDER_TAB = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[4]/A";
	public static String XPATH_SOMEWHERE_IN_PROVIDER_TAB = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV[2]/DIV/SPAN/DIV/DIV/DIV/P/SPAN";
	public static String XPATH_ABOUT_PROVIDER = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV[2]/DIV/SPAN/DIV/DIV/DIV/P";
	public static String XPATH_PROVIDER_DETAILS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV/DIV";
	public static String XPATH_PROVIDER_DETAILS_INNER = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV/DIV/DIV";
	public static String XPATH_REVIEWS_TAB = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[3]/A";
	public static String XPATH_SOMEWHERE_IN_REVIEWS_TAB = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[2]/DIV/H5";
	public static String XPATH_RATING_AVERAGE = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV/DIV/DIV[1]";
	public static String XPATH_5STARS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[1]";
	public static String XPATH_4STARS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[2]";
	public static String XPATH_3STARS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[3]";
	public static String XPATH_2STARS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[4]";
	public static String XPATH_1STARS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[5]";
	public static String XPATH_RATING = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[3]/DIV/SPAN/DIV/DIV/DIV/SPAN[2]/SPAN/P/SPAN";
	public static String XPATH_REVIEW = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[3]/DIV/SPAN";
	public static String RELATIVE_XPATH_REVIEW_EXTENDED_CONTENT = "/DIV/DIV/DIV/SPAN[3]/SPAN[2]";
	public static String XPATH_NEXT_REVIEW_PAGE_BUTTON = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/SPAN/DIV/DIV/DIV/SPAN/A";

	private static String[] fetchInitialURLArray(JSONObject json) {
		JSONArray array = (JSONArray) json.get("initial urls");
		String[] initialURLS = new String[array.size()];
		System.out.println(array.size() + " initial URLs found");
		for (int i = 0; i < array.size(); i++) {
			initialURLS[i] = (String) array.get(i);
		}
		return initialURLS;
	}

	public static void readConfFile() {
		FileReader reader = null;
		try {
			reader = new FileReader(configFile);
		} catch (IOException e) {
			System.err.print("IO Exception, Config could not be opened.");
			e.printStackTrace();
			System.exit(1);
		}
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		System.out.println("reading conf file: " + configFile);

		try {
			json = (JSONObject) parser.parse(reader);
		} catch (IOException | ParseException e1) {
			System.err.print("IO Exception, Config could not be parsed.");
			e1.printStackTrace();
			System.exit(1);
		}

		try {
			INITIAL_URLS = fetchInitialURLArray(json);
		} catch (Exception e) {
			System.err.println("Failed to load the initial array of page URLs");
			System.exit(1);
		}
		try {
			outputFile = (String) json.get("output file");
		} catch (Exception e) {
			System.err.println("Failed to load the name of the output file");
			System.exit(1);
		}
		try {
			uRLsCachePath = (String) json.get("cached listing urls file");
		} catch (Exception e) {
			System.err.println("Failed to load the path of the cache file.");
			System.exit(1);
		}
		try {
			reviewsFolderPath = (String) json.get("reviews' folder");
		} catch (Exception e) {
			System.err.println("Failed to load the folder of the reviews files.");
			System.exit(1);
		}
		try {
			URLsCached = fetchAreURLsCached(json);
		} catch (Exception e) {
			System.err.println("Failed to check if cached.");
			System.exit(1);
		}
		try {

			START_INDEX = Integer.parseInt((String) json.get("start index"));
		} catch (Exception e) {
			System.err.println("Failed to laod start index.");
			System.exit(1);
		}
		try {
			END_INDEX = Integer.parseInt((String) json.get("end index"));
		} catch (Exception e) {
			System.err.println("End index not set, Setting the END_INDEX to integer max.");
		}
		try {
			scrapingThreads = Integer.parseInt((String) json.get("number of threads"));
		} catch (Exception e) {
			System.out.println("Number of threads not set, Setting the scrapingThreads default " + scrapingThreads + ".");
		}
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("FileReader failed to close() the " + configFile);
			e.printStackTrace();
		}

	}

	private static boolean fetchAreURLsCached(JSONObject json) {

		String bool = (String) json.get("are Listing URLs cached");
		if (bool.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

}
