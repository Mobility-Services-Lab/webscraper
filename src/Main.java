import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

public class Main {
	// Table apps;
	// static WebClient webClient;
	public static Object bell;
	public static String initialURL = "https://appexchange.salesforce.com/";
	public static String outputPath = "C:\\Users\\Neu\\Desktop";
	public static String file = "hi.csv";

	public static void main(String args[]) {
		bell = new Object();
		runMultiThread();
		// App r = new App();
		// r.setURL("https://appexchange.salesforce.com/listingDetail?listingId=a0N3000000B5cXCEAZ");
		// new Scraper(r).getAppInfo(r);
	}

	public static void runMultiThread() {
		Scout scout = new Scout();
		new Thread(scout).start();
		int maxThreads = 3;
		ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
		int processedApps = 0;
		while (true) {
			if (processedApps < allApps.size()) {
				Runnable scraper = new Scraper(allApps.get(processedApps));
				pool.execute(scraper);
				processedApps++;
			}
			if (processedApps == 4) { // temporary. TODO: remove this.
				// System.out.println("12 apps processed. BREAK!");
				break;
			}
			if (scout.finito && processedApps >= allApps.size()) {
				// System.out.println("all apps processed. and Scout done. BREAK!");
				break;
			}
			synchronized (bell) {
				try {
					bell.wait(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
		}
		System.out.println("All Done! Writing output..");
		saveRows(outputPath, file);
		System.out.println("..saved.");
	}

	public static volatile ArrayList<App> allApps = new ArrayList<>();

	private static void saveRows(String path, String file) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(path + "\\" + file);
			String headerLine = "";
			for (String header : App.headers) {
				headerLine += header + ",";
			}
			headerLine += "\n";
			writer.append(headerLine);
			int i = 0; // to act as ID.
			for (App a : allApps) {
				i++;
				System.out.println("saving app #" + i + ": " + a.getDetail("Name"));
				if (a.isProcessed())
					// System.out.println("writing app: " + a.getDetail("Name"));
					writer.append(a.toCSVLine());
				// write the reviews in a different file:
				try {
					FileWriter revWrite = new FileWriter(path + "\\revs\\" + i + " - " + a.getDetail("Name") + ".csv");
					for (Review r : a.getReviews()) {
						revWrite.append(r.toCSVLine());
					}
					revWrite.flush();
					revWrite.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("could not write the reviews of the app: '" + i + " - " + a.getDetail("Name")
							+ "' to the file");
				}

			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
