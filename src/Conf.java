
public class Conf {
	public static int readerBufferSize = 1024 * 1024;
	public static String initialURL = "https://appexchange.salesforce.com/";
	public static String outputPath = "C:\\Users\\Neu\\Desktop";
	public static String uRLsCachePath = Conf.outputPath + "\\urls.csv";

	public static String file = "result - 3000..3410.csv";
	public static int maxScrapingThreads = 4;
	public static boolean appURLsCached = true;
	public static int START_INDEX = 3000;
	public static int END_INDEX = 3410;
	/**
	 * Make sure this folder exists in advance.
	 */
	public static String reviewsFolderPath = "\\revs\\";
	public static String XPATH_APPS = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[2]/DIV/DIV/SPAN[2]/SPAN/UL/LI/SPAN/DIV[1]/DIV[1]/DIV[1]/DIV[3]/DIV[1]/A";
	// Caution: XPATH_NEXT_PAGE_BUTTON points to more than one ANCHOR:
	public static String XPATH_NEXT_PAGE_BUTTON = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[3]/DIV/DIV/DIV/A";
	public static String XPATH_APP_NAME = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/H1";
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

	public static String[] INITIAL_URLS = {
//			Categories:
			"https://appexchange.salesforce.com/category/sales", // 1218 
			"https://appexchange.salesforce.com/category/compensation", //24 // sub category
			"https://appexchange.salesforce.com/category/contracts", // 42 // sub category
			"https://appexchange.salesforce.com/category/dashboards", // 87  // sub category
			"https://appexchange.salesforce.com/category/doc-generation", // 40  // sub category
			"https://appexchange.salesforce.com/category/ecommerce", // 32  // sub category
			"https://appexchange.salesforce.com/category/email-calendar", // 66  // sub category
			"https://appexchange.salesforce.com/category/forecasting", // 37  // sub category
			"https://appexchange.salesforce.com/category/geolocation", // 50 // sub category
			"https://appexchange.salesforce.com/category/partners", // 26  // sub category
			"https://appexchange.salesforce.com/category/quotes-orders", // 108 // sub category
			"https://appexchange.salesforce.com/category/intelligence", // 165 // sub category
			"https://appexchange.salesforce.com/results?filter=OTH_a0L3000000OvSOEEA3", // 153  // sub category

			"https://appexchange.salesforce.com/category/service", // 338
			"https://appexchange.salesforce.com/category/marketing", // 286
			"https://appexchange.salesforce.com/category/it-admin", // 616
			"https://appexchange.salesforce.com/category/hr", // 96
			"https://appexchange.salesforce.com/category/finance", // 207
			"https://appexchange.salesforce.com/category/collaboration", // 386
			"https://appexchange.salesforce.com/category/erp", // 82
			"https://appexchange.salesforce.com/category/analytics", // 61
//			Editions:
			"https://appexchange.salesforce.com/results?filter=GE", // Group
			"https://appexchange.salesforce.com/results?filter=PE", // Professional
			"https://appexchange.salesforce.com/results?filter=EE", // Enterprise
			"https://appexchange.salesforce.com/results?filter=UE", // Unlimited
			"https://appexchange.salesforce.com/results?filter=PP", // Performance
			"https://appexchange.salesforce.com/results?filter=FE", // Force.com
			"https://appexchange.salesforce.com/results?filter=DE", // Developer
//			Filters:
			"https://appexchange.salesforce.com/results?sort=2&filter=9", // Managed, sort by provider
			"https://appexchange.salesforce.com/results?sort=3&filter=9", // Managed, sort by rating
			"https://appexchange.salesforce.com/results?filter=9,GE", // Managed, Group Edition
			"https://appexchange.salesforce.com/results?filter=9,2,PE", // Managed, Professional Edition, Paid
			"https://appexchange.salesforce.com/results?filter=9,1,PE", // Managed, Professional Edition, Free
			"https://appexchange.salesforce.com/results?filter=9,8,PE", // Managed, Professional Edition, Discounted for non profits
			"https://appexchange.salesforce.com/results?filter=9,1,EE", // Managed, Enterprise, Free 
			"https://appexchange.salesforce.com/results?filter=9,1,lg=en", // Managed, Free, English
			"https://appexchange.salesforce.com/results?filter=9,2,lg=en", // managed, paid, English
			"https://appexchange.salesforce.com/results?filter=9,2,rt1,lg=en", // Managed, Paid, 1stars&up
			"https://appexchange.salesforce.com/results?filter=9,2,EE", // Managed, Enterprise, Paid
			"https://appexchange.salesforce.com/results?filter=9,2,EE,rt1", // Managed, Enterprise, 1starsand up, paid
			
			
			"https://appexchange.salesforce.com/results?sort=3&filter=6", // Native
			"https://appexchange.salesforce.com/results?sort=3&filter=10", // Mobile
			"https://appexchange.salesforce.com/results?sort=3&filter=11,12", // iPad, iPhone
			"https://appexchange.salesforce.com/results?sort=3&filter=13", // Android
			"https://appexchange.salesforce.com/results?sort=3&filter=16", // Marketing cloud
			"https://appexchange.salesforce.com/results?sort=3&filter=14", // Salesforce1
			"https://appexchange.salesforce.com/results?sort=3&filter=15", // Lightning Ready
//			Languages:
			"https://appexchange.salesforce.com/results?filter=lg=nl", // Dutch
			"https://appexchange.salesforce.com/results?filter=lg=fi", // Finnish
			"https://appexchange.salesforce.com/results?filter=lg=fr", // French
			"https://appexchange.salesforce.com/results?filter=lg=de", // German
			"https://appexchange.salesforce.com/results?filter=lg=da", // Danish
			"https://appexchange.salesforce.com/results?filter=lg=it", // Italian
			"https://appexchange.salesforce.com/results?filter=lg=ja", // Japanese
			"https://appexchange.salesforce.com/results?filter=lg=ko", // Korean
			"https://appexchange.salesforce.com/results?filter=lg=pt", // Portuguese
			"https://appexchange.salesforce.com/results?filter=lg=ru", // Russian
			"https://appexchange.salesforce.com/results?filter=lg=zh_CN", // Simplified Chinese
			"https://appexchange.salesforce.com/results?filter=lg=es", // Spanish
			"https://appexchange.salesforce.com/results?filter=lg=sv", // Swedish
			"https://appexchange.salesforce.com/results?filter=lg=th", // Thai
			"https://appexchange.salesforce.com/results?filter=lg=zh_TW", // Traditional Chinese
			}; 
}
