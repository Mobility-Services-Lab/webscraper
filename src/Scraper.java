import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper implements Runnable {
	App app;

	public Scraper(App app) {
		this.app = app;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		getAppInfo(app);
	}

	/**
	 * Retrieves the intended data from the given url, puts in the App object
	 * itself. Then rings the bell to wake the main thread.
	 */
	public void getAppInfo(App listing) {
		WebClient webClient = new WebClient();
		try {
			HtmlPage appPage = webClient.getPage(listing.getURL());
			// System.out.println("name:" + );
			listing.addDetail("Name", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/H1", 10));
			listing.addDetail("Subtitle",
					getValuePersist(appPage, "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/P", 1));
			listing.addDetail("Number of Reviews",
					getValuePersist(appPage, "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/SPAN/DIV/DIV/DIV/DIV/SPAN/P",
							1).replaceAll("\\D+", "")); // TODO: remove the
			listing.addDetail("Intro", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV/P[2]/SPAN", 1));
			listing.addDetail("Release date", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN/P", 1));
			listing.addDetail("Pricing", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN[2]/STRONG/SPAN",
					1));
			listing.addDetail("Categories", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV[2]/SPAN[3]/SPAN/P",
					1));
			String producer = getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV/SPAN/DIV/DIV/DIV/P/B", 1)
							.replaceFirst("App by ", "");
			listing.addDetail("Producer", producer);

			// click on the detail button.
			appPage = ((HtmlElement) appPage
					.getByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[2]/A").get(0)).click();

			//long startTime = System.currentTimeMillis();			
			listing.addDetail("Details", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV/DIV/DIV", 10));
			///System.out.println("elapsed time by trying to get the detail page:" + (System.currentTimeMillis()-startTime) );
			
			for (Object contactInfo : appPage.getByXPath(
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV/DIV/DIV/P/A")) {
				try {
					if (((HtmlElement) contactInfo).asText().toLowerCase().contains("website")) { // todo: company website is listed in the provider page for sure. also try to fill the data over there.
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

			listing.addDetail("Sales Phone", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV/DIV/DIV/P/SPAN/SPAN",
					1));
			listing.addDetail("Editions", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/SPAN/DIV/DIV[2]/DIV/UL/LI",
					1).replaceFirst("Salesforce Editions: ", ""));
			String techDetails = getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV[2]/DIV/DIV/UL",
					1);
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

			listing.addDetail("Supported Features", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[2]/SPAN/DIV[2]/DIV/DIV[2]/SPAN/DIV", 1)
							.replaceFirst("Supported Features\r\n", ""));
			appPage = ((HtmlElement) appPage
					.getByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[4]/A").get(0)).click();
			// click on the PROVIDER tab.
			Thread.sleep(1000);
			listing.addDetail("About Provider", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV[2]/DIV/SPAN/DIV/DIV/DIV/P/SPAN",
					10));
			//TODO: fix "ABout provider". it does not extract. maybe measure how long it takes to get the data, first.
			String providerDetails = getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[4]/SPAN/DIV/DIV/DIV", 1);
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
					.getByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV/DIV/UL/LI[3]/A").get(0)).click();

			// waiting until the page loads.
			getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[2]/DIV/H5", 10);
			listing.addDetail("Rating", getValuePersist(appPage,
					"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV/DIV/DIV[1]",
					1));
			try {
			listing.addDetail("_5Stars", ((HtmlElement)appPage.getFirstByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[1]")).getAttribute("alt").replaceAll("5 Stars? : ", "").replaceAll(" reviews?", "") );
			listing.addDetail("_4Stars", ((HtmlElement)appPage.getFirstByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[2]")).getAttribute("alt").replaceAll("4 Stars? : ", "").replaceAll(" reviews?", "") );
			listing.addDetail("_3Stars", ((HtmlElement)appPage.getFirstByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[3]")).getAttribute("alt").replaceAll("3 Stars? : ", "").replaceAll(" reviews?", "") );
			listing.addDetail("_2Stars", ((HtmlElement)appPage.getFirstByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[4]")).getAttribute("alt").replaceAll("2 Stars? : ", "").replaceAll(" reviews?", "") );
			listing.addDetail("_1Stars", ((HtmlElement)appPage.getFirstByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/DIV[2]/DIV/SPAN/SPAN/DIV[2]/DIV/DIV[5]")).getAttribute("alt").replaceAll("1 Stars? : ", "").replaceAll(" reviews?", "") );
			} catch(Exception e) {}
			// try {
			// Matcher mat = Pattern.compile("[0-9]+").matcher(ratingClass);
			// if (mat.find()) {
			// listing.addDetail("Rating", mat.group(0));
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// Thread.sleep(1000);
			getReviews(listing, appPage);
			listing.setProcessed(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(listing);
		synchronized (Main.bell) {
			Main.bell.notify();
		}
	}

	private void getReviews(App a, HtmlPage reviewPage) {
		String ratingXPath = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[3]/DIV/SPAN/DIV/DIV/DIV/SPAN[2]/SPAN/P/SPAN";
		String reviewXPath = "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[3]/DIV/SPAN";
		boolean lastPage = false;
		while (!lastPage) {
			List<Object> reviews = reviewPage.getByXPath(reviewXPath);
			List<Object> ratings = reviewPage.getByXPath(ratingXPath);
			for (int i = 0; i < reviews.size(); i++) {
				HtmlElement review = ((HtmlElement) reviews.get(i));
				Review r = new Review(review.asText());
				try { // if the text is extended and hidden, let's get it.
					// System.out.println("rev: " + review.getCanonicalXPath());
					r.setContent(((HtmlElement) reviewPage
							.getFirstByXPath(review.getCanonicalXPath() + "/DIV/DIV/DIV/SPAN[3]/SPAN[2]"))
									.getTextContent().replaceAll(" …More$", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// r.writerName = ((HtmlElement) writerNames.get(i)).asText();
				r.setRating(((HtmlElement) ratings.get(i)).getAttribute("class").replaceAll("\\D+", ""));
				// r.headline = ((HtmlElement) headlines.get(i)).asText();
				// r.content = ((HtmlElement) contents.get(i)).asText();
				// r.date = ((HtmlElement) dates.get(i)).asText();
				a.addReview(r);
			}
			// if(writerNames.size() == 0) {
			// // If there is no reviews at all, getNextReviewPage can't perform it's
			// persistent waiting.
			// // Because it will hopelessly look for an element that does not exist.
			// Therefore, quit sooner.
			// System.out.println("no reviews seen. return..");
			// return;
			// }
			try {
				reviewPage = getNextReviewPage(reviewPage,
						"/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/SPAN/DIV/DIV/DIV/SPAN/A");
			} catch (Exception e) {
				lastPage = true;
				e.printStackTrace();
			}
			//TODO : remove this.
			lastPage = true;
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

		// HtmlElement nextPageLink = (HtmlElement)
		// reviewsPage.getByXPath(xPath).get(0);
		// HtmlElement nextPageLink = reviewsPage.getAnchorByText("Next Page");
		// HtmlElement nextPageLink = reviewsPage.getFirstByXPath("/A[text()='Next
		// Page']");
		// System.out.println("getNextReviewPage: '" + nextPageLink.asText()+"'");
		// if the found link does not read: "Next Page", it is probably the last page.
		if (nextPageLink.asText().toLowerCase().trim().compareTo("next page") != 0) {
			// System.out.println(nextPageLink.asText().toLowerCase().trim());
			throw new Exception();
		}

		// reviewsPage = null;
		HtmlPage newPage = nextPageLink.click();
		Thread.sleep(10000);
		// just persist to get this content on the page, to make sure it is loaded.
		// String absurd = getValuePersist(reviewsPage,
		// "/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/SPAN/DIV/DIV/DIV/SPAN/A",
		// 20); // the writer's name.

		// for(int i = 0; i < 20; i++) {
		// Thread.sleep(1000);
		// List<Object> reviews =
		// newPage.getByXPath("/HTML/BODY/DIV/DIV/DIV/DIV/DIV/DIV/FORM/DIV/DIV[2]/DIV/DIV[3]/SPAN/SPAN[2]/DIV[3]/DIV/SPAN");
		// System.out.print("." + reviews.size());
		// if(reviews.size()>0)
		// break;
		// }
		// Thread.sleep(1000);
		// System.out.println("all the anchors:");
		// for(HtmlAnchor a: newPage.getAnchors()) {
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

}
