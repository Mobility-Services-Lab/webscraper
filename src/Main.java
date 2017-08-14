import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static Object bell;

	public static void main(String args[]) {
		bell = new Object();
		runMultiThread();
	}

	/**
	 * This function tries to get All the information of the apps in 
	 * Salesforce. But it is highly configurable via the Conf file and 
	 * variables from there. If the list of the Apps has already been
	 * cached in a file in a previous run, set the Conf.appURLsCached
	 * to true, so that this function fills the list of the apps from
	 * the file and saves time.  The variable maxScrapingThreads shows
	 * how many parallel threads should be used to grab the information.
	 * Depending on the properties of the computer that this function 
	 * runs on, threads could be higher or lower. 
	 * @param appURLsCached
	 */
	public static void runMultiThread() {
		long startTime = System.currentTimeMillis();
		Scout scout = new Scout();
		if (Conf.appURLsCached) {
			scout.getURLsCached(); // use the cache file to load the URLs.
		} else {
			new Thread(scout).start(); // get the app URLs from scratch
		}
		ExecutorService pool = Executors.newFixedThreadPool(Conf.maxScrapingThreads);
		int processedApps = 0;
		while (true) {
			while (processedApps < allApps.size()) {
				Runnable scraper = new Scraper(allApps.get(processedApps));
				pool.execute(scraper);
				processedApps++;
				if(processedApps %100 == 0) {
					System.out.println(processedApps+ "th App was assigned.");
				}
				System.out.println("Apps left for process: " + processedApps);
			}
			if (scout.finito && processedApps >= allApps.size()) {
				break;
			}
			synchronized (bell) {
				try {
					bell.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
		}
		System.out.println("All Done! Writing output..");
		saveRows(Conf.outputPath, Conf.file);
		System.out.println("..saved.");
		System.out.println("Total running time: " + ((System.currentTimeMillis()- startTime) / 1000.0 / 60) + " Minutes");
	}

	public static volatile ArrayList<App> allApps = new ArrayList<>();
/**
 * Save the content of allApps array in the given file. Also save all the 
 * reviews of each app in a different file.
 * @param path
 * @param file
 */
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
				if (!a.isProcessed())
					continue;
				System.out.println("saving app #" + i + ": " + a.getDetail("Name"));
				if (a.isProcessed())
					// System.out.println("writing app: " + a.getDetail("Name"));
					writer.append(a.toCSVLine());
				// write the reviews in a different file:
				try {
					FileWriter revWrite = new FileWriter(path + Conf.cachedFolderPath + i + " - " + a.getDetail("Name").replaceAll("[^A-Za-z0-9 ]", " ") + ".csv");
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
