import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Scout is able to traverse through the pagination and look for the URLs of the
 * listings in the pages. Correct XPaths have to be provided in advance.
 * 
 * @author Makan
 *
 */
public class Scout implements Runnable {

	private String initialURL = "https://appexchange.salesforce.com";
	private String appsXPath = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[2]/DIV/DIV/SPAN[2]/SPAN/UL/LI/SPAN/DIV[1]/DIV[1]/DIV[1]/DIV[3]/DIV[1]/A";
	private String nextPageXPath = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV[3]/DIV/DIV/DIV/A"; // It points to more than one item.
	public boolean finito = false;
	@Override
	public void run() {
		listAppURLs();
		System.out.println("Scout done.");
		finito = true;
	}

	private void listAppURLs() {
		WebClient webClient = new WebClient();
		HtmlPage page = null;
		try {
			int i = 0;//
			String pageURL = initialURL; // "https://appexchange.salesforce.com/results?pageNo=2";
			while (pageURL != null) {
				page = webClient.getPage(pageURL);
				List<String> appsInPage = getItemLinksInPage(page, appsXPath);
				for (String url : appsInPage) {
					App app = new App();
					app.setURL(url);
					Main.allApps.add(app);
				}
				System.out.println("URL:" + pageURL);
				System.out.println(Main.allApps.size() + " Apps indexed by Scout so far.");
				pageURL = getNextPageURL(page, nextPageXPath);
				// System.out.println("allApps Size:" + allApps.size());
				synchronized (Main.bell) {
					Main.bell.notify();
				}
				i++;//TODO: remove this.
				if (i>1)//
					break;//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the page that contains a list of the entities, and extracts the URLs of
	 * those entities.
	 * 
	 * @param page
	 *            the HtmlPage that contains the list.
	 * @param XPath
	 *            the address of the anchor element that contains the URL of the
	 *            page of the entities. the XPath is not unique. It can 'select' all
	 *            the elements.
	 * @return A list of the URLs of the entities that were listed on the page.
	 */
	public List<String> getItemLinksInPage(HtmlPage page, String XPath) {
		ArrayList<String> urls = new ArrayList<>();
		try {
			List<HtmlAnchor> appLinks = page.getByXPath(XPath);
			for (HtmlAnchor anchor : appLinks) {
				urls.add("https://appexchange.salesforce.com" + anchor.getAttribute("href"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	/**
	 * Gets the Object of the page, and extracts the URL for the next page.
	 * 
	 * @param page
	 *            the HtmlPage Object, of one page in the pagination.
	 * @param XPath
	 *            the XPath referring to the next-page button on the page.
	 * @return the URL of the next page.
	 */
	public String getNextPageURL(HtmlPage page, String XPath) {
		String url = null;
		try {
			List<Object> links = page.getByXPath(XPath);
			HtmlElement nextPageLink = (HtmlElement) links.get(links.size() - 1); // Get the last link. (That is
																					// probably the next page.)
			url = "https://appexchange.salesforce.com" + nextPageLink.getAttribute("href");
		} catch (Exception e) { // no next page button found.
			e.printStackTrace();
		}
		return url;
	}

}
