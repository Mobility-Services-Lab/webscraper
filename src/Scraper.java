import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import utils.Status;

public class Scraper implements Runnable {
	Listing app;

	public Scraper(Listing app) {
		this.app = app;
	}

	public Scraper() {
		this.app = null;
	}

	@Override
	public void run() {
		System.out.println("Scrapper started.");
		if (app != null) {
			getListingInfo(app);
		} else {
			System.out.println("APP is null");
		}
		System.out.println("Scrapper done");
	}

	/**
	 * Retrieves the intended data from the given url, puts in the App object
	 * itself. Then rings the bell to wake the main thread.
	 */
	public void getListingInfo(Listing listing) {
		WebClient webClient = new WebClient();
		try {
			HtmlPage appPage = webClient.getPage(listing.getURL());
			listing.addDetail("Name", getValuePersist(appPage, Conf.XPATH_LISTING_NAME, 10));
			listing.addDetail("Subtitle", getValuePersist(appPage, Conf.XPATH_SUBTITLE, 1));
			listing.addDetail("Number of Reviews",
					getValuePersist(appPage, Conf.XPATH_REVIEW_NUMBER, 1).replaceAll("\\D+", ""));
			listing.addDetail("Intro", getValuePersist(appPage, Conf.XPATH_INTRO, 1));
			listing.addDetail("Release date", getValuePersist(appPage, Conf.XPATH_RELEASE_DATE, 1));
			listing.addDetail("Pricing", getValuePersist(appPage, Conf.XPATH_PRICING, 1));
			listing.addDetail("Categories", getValuePersist(appPage, Conf.XPATH_CATEGORIES, 1));
			String producer = getValuePersist(appPage, Conf.XPATH_PRODUCER, 1).replaceFirst("App by ", "");
			listing.addDetail("Producer", producer);

			// click on the detail button.
			appPage = ((HtmlElement) appPage.getByXPath(Conf.XPATH_DETAIL_TAB).get(0)).click();

			listing.addDetail("Details", getValuePersist(appPage, Conf.XPATH_DETAILS, 10));

			for (Object contactInfo : appPage.getByXPath(Conf.XPATH_WEBSITE_OR_EMAIL)) {
				try {
					if (((HtmlElement) contactInfo).asText().toLowerCase().contains("website")) {
						listing.addDetail("Company Website", ((HtmlElement) contactInfo).getAttribute("href"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (((HtmlElement) contactInfo).asText().toLowerCase().contains("email")) {
						listing.addDetail("Email",
								((HtmlElement) contactInfo).getAttribute("href").replaceFirst("mailto:", ""));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			listing.addDetail("Sales Phone", getValuePersist(appPage, Conf.XPATH_SALES_PHONE, 1));
			listing.addDetail("Salesforce Editions", getValuePersist(appPage, Conf.XPATH_SALESFORCE_EDITIONS, 1)
					.replaceFirst("Salesforce Editions: ", ""));
			String techDetails = getValuePersist(appPage, Conf.XPATH_TECH_DETAILS, 1);
			// System.out.println("tech details:" + techDetails);
			try {
				Pattern pat = Pattern.compile("First Release: (.+?)(\r\n|\n|$)");// after industries, and before the end
																					// of line or end of string.
				Matcher mat = pat.matcher(techDetails);
				if (mat.find())
					listing.addDetail("First Release", mat.group(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Latest Release: (.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(techDetails);
				if (mat.find())
					listing.addDetail("Latest Release", mat.group(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Version: (.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(techDetails);
				if (mat.find())
					listing.addDetail("Version", mat.group(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Languages: (.+?)(\r\n|\n|$)");//
				Matcher mat = pat.matcher(techDetails);
				if (mat.find())
					listing.addDetail("Language(s)", mat.group(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Industries: (.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(techDetails);
				if (mat.find())
					listing.addDetail("Industries", mat.group(1));
			} catch (Exception e) {
				e.printStackTrace();
			}

			listing.addDetail("Supported Features", getValuePersist(appPage, Conf.XPATH_SUPPORTED_FEATURES, 1)
					.replaceFirst("Supported Features\r\n", ""));
			appPage = ((HtmlElement) appPage.getByXPath(Conf.XPATH_PROVIDER_TAB).get(0)).click();
			// click on the PROVIDER tab.
			Thread.sleep(3000);

			getValuePersist(appPage, Conf.XPATH_SOMEWHERE_IN_PROVIDER_TAB, 10); // just to make sure that the page is
																				// loaded.
			try {
				listing.addDetail("About Provider",
						getValueFromXPathAndDescendants(appPage, Conf.XPATH_ABOUT_PROVIDER));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				new URL(listing.getDetail("Company Website"));
			} catch (Exception e) {
				// the item is not a url. so try to get the company website, from another source
				for (Anchor a : getDescendantAnchors(appPage, Conf.XPATH_PROVIDER_DETAILS)) {
					// System.out.println("a: " + a.href + " " + a.text);
					if (a.text.trim().toLowerCase().contains("website")) {
						// System.out.println("found the anchor:" + a.text);
						listing.addDetail("Company Website", a.href);
					}
				}
			}

			String providerDetails = getValuePersist(appPage, Conf.XPATH_PROVIDER_DETAILS_INNER, 1);

			try {
				Pattern pat = Pattern.compile("Employees(\r\n|\n)(.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(providerDetails);
				if (mat.find())
					listing.addDetail("Employees", mat.group(2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Year Founded(\r\n|\n)(.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(providerDetails);
				if (mat.find())
					listing.addDetail("Year Founded", mat.group(2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Pattern pat = Pattern.compile("Headquarters(\r\n|\n)(.+?)(\r\n|\n|$)");
				Matcher mat = pat.matcher(providerDetails);
				if (mat.find())
					listing.addDetail("Headquarters", mat.group(2));
			} catch (Exception e) {
				e.printStackTrace();
			}
			appPage = ((HtmlElement) appPage // click on the REVIEWS tab.
					.getByXPath(Conf.XPATH_REVIEWS_TAB).get(0)).click();

			// waiting until the page loads.
			getValuePersist(appPage, Conf.XPATH_SOMEWHERE_IN_REVIEWS_TAB, 10);
			listing.addDetail("Rating", getValuePersist(appPage, Conf.XPATH_RATING_AVERAGE, 1));
			try {
				listing.addDetail("_5Stars", ((HtmlElement) appPage.getFirstByXPath(Conf.XPATH_5STARS))
						.getAttribute("alt").replaceAll("5 Stars? : ", "").replaceAll(" reviews?", ""));
				listing.addDetail("_4Stars", ((HtmlElement) appPage.getFirstByXPath(Conf.XPATH_4STARS))
						.getAttribute("alt").replaceAll("4 Stars? : ", "").replaceAll(" reviews?", ""));
				listing.addDetail("_3Stars", ((HtmlElement) appPage.getFirstByXPath(Conf.XPATH_3STARS))
						.getAttribute("alt").replaceAll("3 Stars? : ", "").replaceAll(" reviews?", ""));
				listing.addDetail("_2Stars", ((HtmlElement) appPage.getFirstByXPath(Conf.XPATH_2STARS))
						.getAttribute("alt").replaceAll("2 Stars? : ", "").replaceAll(" reviews?", ""));
				listing.addDetail("_1Stars", ((HtmlElement) appPage.getFirstByXPath(Conf.XPATH_1STARS))
						.getAttribute("alt").replaceAll("1 Stars? : ", "").replaceAll(" reviews?", ""));
			} catch (Exception e) {
			}
			getReviews(listing, appPage);
			listing.setStatus(Status.PROCESSED);
			webClient.close();
		} catch (Exception e) {
			// listing.setStatus(Status.OPEN); // only if you want the scraping to happen again.
			e.printStackTrace();
		}
		synchronized (Main.bell) {
			Main.bell.notify();
		}
	}

	private void getReviews(Listing a, HtmlPage reviewPage) {
		String ratingXPath = Conf.XPATH_RATING;
		String reviewXPath = Conf.XPATH_REVIEW;
		boolean lastPage = false;
		while (!lastPage) {
			List<Object> reviews = reviewPage.getByXPath(reviewXPath);
			List<Object> ratings = reviewPage.getByXPath(ratingXPath);
			for (int i = 0; i < reviews.size(); i++) {
				HtmlElement review = ((HtmlElement) reviews.get(i));
				Review r = new Review(review.asText());
				try { // if the text is extended and hidden, let's get it.
					r.setContent(((HtmlElement) reviewPage
							.getFirstByXPath(review.getCanonicalXPath() + Conf.RELATIVE_XPATH_REVIEW_EXTENDED_CONTENT))
									.getTextContent().replaceAll(" …More$", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				r.setRating(((HtmlElement) ratings.get(i)).getAttribute("class").replaceAll("\\D+", ""));
				a.addReview(r);
			}
			try {
				reviewPage = getNextReviewPage(reviewPage, Conf.XPATH_NEXT_REVIEW_PAGE_BUTTON);
			} catch (Exception e) {
				lastPage = true;
				e.printStackTrace();
			}
		}
	}

	/**
	 * In the reviews Page, this function looks for the 'Next Page' button,
	 * following the given xPath. If the button with such text is not found, throws
	 * an exception.
	 * 
	 * @param reviewsPage
	 *            the HtmlPage Object containing the current page.
	 * @param xPath
	 *            xPath address of the 'next page' button on the page.
	 * @return the HtmlPage object containing the next page.
	 * @throws Exception
	 *             a normal exception, showing that the given 'next page' button
	 *             does not exist.
	 */
	private HtmlPage getNextReviewPage(HtmlPage reviewsPage, String xPath) throws Exception {
		// Get the last link that matches the xPath. That's probably the next page link.
		List<Object> links = reviewsPage.getByXPath(xPath);
		HtmlElement nextPageLink = (HtmlElement) links.get(links.size() - 1);

		// if the found link does not read: "Next Page", it is probably the last page.
		if (nextPageLink.asText().toLowerCase().trim().compareTo("next page") != 0) {
			throw new Exception();
		}
		HtmlPage newPage = nextPageLink.click();
		Thread.sleep(10000);
		// for (HtmlAnchor a : newPage.getAnchors()) {
		// System.out.print(a.asText() + " - ");
		// }
		return newPage;
	}

	/**
	 * This function catches the first element on the page, that matches the XPath,
	 * and returns the String content of it. If the first rep times run into an
	 * exception, it will retry, with an interval.
	 * 
	 * @param appPage
	 *            the HtmlPage Object, containing the DOM.
	 * @param xPath
	 *            XPath to the element or element set that matches the pattern.
	 * @param rep
	 *            Number of retries until the function gives up.
	 * @return The String of first element from the set of elements that matches the
	 *         pattern.
	 */
	private String getValuePersist(HtmlPage appPage, String xPath, int rep) {
		String res = "";
		while (rep > 0) {
			try {
				Thread.sleep(2000);
				res = getValueFromXPath(appPage, xPath);
				Thread.sleep(2000);
				return res;
			} catch (Exception e) {
				res += ".";
				rep--;
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * This function catches the first element on the page, that matches the XPath,
	 * and returns the String content of it.
	 * 
	 * @param appPage
	 *            the HtmlPage Object, containing the DOM.
	 * @param xPath
	 *            XPath to the element or element set that matches the pattern.
	 * @return The String of first element from the set of elements that matches the
	 *         pattern.
	 */
	private String getValueFromXPath(HtmlPage appPage, String xPath) throws Exception {
		return ((HtmlElement) appPage.getByXPath(xPath).get(0)).asText();
	}

	/**
	 * Getting the text that is shown in the current element, and the text of it's
	 * descendants all together.
	 * 
	 * @param appPage
	 * @param xPath
	 * @return
	 * @throws Exception
	 */
	private String getValueFromXPathAndDescendants(HtmlPage appPage, String xPath) throws Exception {
		return ((HtmlElement) appPage.getByXPath(xPath).get(0)).getTextContent();
	}

	/**
	 * Takes an xPath in a Page, and returns any Anchor elements that are its
	 * descendants.
	 * 
	 * @param appPage
	 *            The page object.
	 * @param xPath
	 *            The path, pointing to the parent element.
	 * @return An ArrayList of the found Anchors. Anchor is a class local to the
	 *         Scraper file.
	 */
	private ArrayList<Anchor> getDescendantAnchors(HtmlPage appPage, String xPath) {
		ArrayList<Anchor> anchors = new ArrayList<>();
		for (DomNode element : ((HtmlElement) appPage.getByXPath(xPath).get(0)).getDescendants()) {

			NamedNodeMap namedNodes = element.getAttributes();
			for (int i = 0; i < namedNodes.getLength(); i++) {

				if (namedNodes.item(i).getNodeName().equals("href")) {

					Anchor a = new Anchor();
					a.href = namedNodes.item(i).getNodeValue();
					a.text = element.getTextContent();
					anchors.add(a);
					break;
				}
			}
		}
		return anchors;
	}
}

class Anchor {
	String text;
	String href;
}
