import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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

	public boolean finito = false;

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		scoutListingURLs();
		System.out.println("Scout done. Fetched " + Main.allListings.size() + " Listings.");
		finito = true;
		System.out.println(
				"Scouting time: " + ((System.currentTimeMillis() - startTime) / 1000.0 / 60) + " Minutes");
	}

	/**
	 * Gets the URLs from the cache file. Path of the cachefile is in a class
	 * attribute. Assumes that URLs are seperated with a '\r\n' character. Loads the
	 * parsed caches into the allListings list of the Main class. It does NOT empty the
	 * list in advance.
	 */
	public void fetchCachedListings() {
		try {
			// FileWriter f = new FileWriter(filePath);
			File f = new java.io.File(Conf.uRLsCachePath);
			FileReader fr = new java.io.FileReader(f);

			char[] buffer = new char[Conf.readerBufferSize];
			String finalText = "";
			int index;
			do {
				index = fr.read(buffer);
				if (index <= 0)
					break;
				finalText += String.valueOf(buffer, 0, index);
			} while (true);
			fr.close();
			String[] URLs = finalText.split("\r\n");
			for (String url : URLs) {
				Listing a = new Listing();
				a.setURL(url);
				Main.allListings.put(url, a);
			}
			System.out.println(URLs.length + " Cached Listing URLs loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finito = true;
	}

	/**
	 * saves the allListings array to the given path, with a '\r\n' as the separator.
	 * 
	 * @param filePath
	 *            the path to the file where the URLs will be saved.
	 * 
	 */
	private void saveURLsToDisc(String filePath) {
		try {
			FileWriter fw = new FileWriter(filePath);
			for (Entry<String, Listing> item : Main.allListings.entrySet()) {
				fw.append(item.getKey() + "\r\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void scoutListingURLs() {
		WebClient webClient = new WebClient();
		HtmlPage page = null;
		try {
			// String pageURL = Conf.initialURL;
			for (String pageURL : Conf.INITIAL_URLS) {
				System.out.println("Scouting on: " + pageURL);
				while (pageURL != null) {
					page = webClient.getPage(pageURL);
					List<String> listingsInPage = getItemLinksInPage(page, Conf.XPATH_LISTINGS);
					for (String url : listingsInPage) {
						if (!Main.allListings.containsKey(url)) {
							Listing listing = new Listing();
							listing.setURL(url);
							Main.allListings.put(url, listing);
						}
					}
					System.out.println("URL:" + pageURL);
					System.out.println(Main.allListings.size() + " Listings indexed by Scout so far.");
					pageURL = getNextPageURL(page, Conf.XPATH_NEXT_PAGE_BUTTON);
					synchronized (Main.bell) {
						Main.bell.notify();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveURLsToDisc(Conf.uRLsCachePath);
		webClient.close();
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
	 * @return the URL of the next page. Null if the button is not found or the name
	 *         of the found link is not lowercase equal to "next page".
	 */
	public String getNextPageURL(HtmlPage page, String XPath) {
		String url = null;
		try {
			List<Object> links = page.getByXPath(XPath);
			HtmlElement nextPageLink = (HtmlElement) links.get(links.size() - 1); // Get the last link. (That is
			if (nextPageLink.asText().toLowerCase().trim().compareTo("next page") != 0) {
				// the link is not to the next page.
				return url;
			}
			url = "https://appexchange.salesforce.com" + nextPageLink.getAttribute("href");
		} catch (Exception e) { // no next page button found.
			e.printStackTrace();
		}
		return url;
	}

}
