import java.io.FileWriter;
import java.io.IOException;
import utils.Status;

public class Main {
	public static Object bell = new Object();
	/**
	 * allListings array includes all the Listing objects that are listed to be processed or
	 * the ones that are already processed. Scrapers get instances of Listings in this
	 * list to fill the details in. It is assumed that each existing listing in this
	 * list at least has a valid value for the attribute URL.
	 */
	public static volatile MyLinkedMap<String, Listing> allListings = new MyLinkedMap();

	public static void main(String args[]) {
		Conf.readConfFile();
		 processMultipleListings();
	}

	/*
	 * This function fetches the next listing in the specified range in the configuration,
	 * that is yet not processed.
	 */
	private static Listing getFirstUnprocessedListing() {
		for (int i = Conf.START_INDEX; i < Conf.END_INDEX && i < allListings.size(); i++) {
			if (allListings.getValue(i).getStatus() == Status.OPEN) {
				System.out.println("getFirstUnprocessedListing: " + i + "th Listing: " + allListings.getValue(i).getURL());
				return allListings.getValue(i);

			}
		}
		System.out.println("getFirstUnprocessedListing:  No unprocessed Listings");
		return null;
	}

	private static int getFirstFreeWorker(Thread[] workers) {
		for (int i = 0; i < workers.length; i++) {
			if (workers[i] == null || !workers[i].isAlive()) // idle worker
			{
				System.out.println("getFirstFreeWorker: " + " worker # " + i);
				return i;
			}
		}
		System.out.println("getFirstFreeWorker: " + " No workers Available");
		return -1;
	}

	private static boolean allWorkersDone(Thread[] workers) {
		for (int i = 0; i < workers.length; i++) {
			if (workers[i].isAlive()) // busy worker
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * This function tries to get All the information of the listings in Salesforce. But
	 * it is highly configurable via the Conf file and variables from there. If the
	 * list of the Listings has already been cached in a file in a previous run, set the
	 * Conf.URLsCached to true, so that this function fills the list of the listings
	 * from the file and saves time. The variable maxScrapingThreads shows how many
	 * parallel threads should be used to grab the information. Depending on the
	 * properties of the computer that this function runs on, threads could be
	 * higher or lower.
	 */
	public static void processMultipleListings() {
		long startTime = System.currentTimeMillis();
		// fetch URLs.
		Scout scout = new Scout();
		if (Conf.URLsCached) {
			scout.fetchCachedListings(); // use the cache file to load the URLs.
		} else {
			new Thread(scout).start(); // get the listing URLs from scratch
		}
		Thread[] workers = new Thread[Conf.scrapingThreads];
		Listing nextListing = getFirstUnprocessedListing();
		try {
			while ((!scout.finito || nextListing != null || !allWorkersDone(workers))) {
				int index = getFirstFreeWorker(workers);
				if (index > -1 && nextListing != null) {
					System.out.println("Assigning nextListing");
					nextListing.setStatus(Status.ONGOING);
					workers[index] = new Thread(new Scraper(nextListing));
					workers[index].start();
					Thread.yield();
				} else { // no available workers
					synchronized (bell) {
						try {
							bell.wait(60000); // wait for maximum 60 seconds
							System.out.println("Just woke up");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				nextListing = getFirstUnprocessedListing();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Scraping is done");
		System.out.println("All Done! Writing output..");
		saveListings(Conf.outputFile, Conf.START_INDEX, Conf.END_INDEX);
		System.out.println("..saved.");
		System.out.println(
				"Total running time: " + ((System.currentTimeMillis() - startTime) / 1000.0 / 60) + " Minutes");
	}

	/**
	 * Save the content of allListings array in the given file. Also save all the
	 * reviews of each Listing in a different file.
	 * 
	 * @param path
	 * @param file
	 */
	private static void saveListings(String file, int start, int end) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, true);
			String headerLine = "";
			for (String header : Listing.headers) {
				headerLine += header + ",";
			}
			headerLine += "\n";
			writer.append(headerLine);
			writer.flush();
			writer.close();
			for (int i = start; i < end && i < allListings.size(); i++) {
				saveListing(file, allListings.getValue(i), i);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveListing(String file, Listing a, int number) {
		System.out.println("saving listing #" + number + ": " + a.getDetail("Name"));
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, true);
			writer.append(a.toCSVLine());
			writer.flush();
			writer.close();
			saveReviews(Conf.reviewsFolderPath, a, number);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("could not write " + number + "th listing (or its reviews). - " + a.getDetail("Name")
					+ "' to the file.");
		}
	}

	private static void saveReviews(String path, Listing a, int number) throws IOException {
		String name = a.getDetail("Name");
		if (name != null)
			name = name.replaceAll("[^A-Za-z0-9 ]", " ") + ".csv";
		// i+1, because file names start from One, not Zero.
		FileWriter revWrite = new FileWriter(path + (number + 1) + " - " + name);
		for (Review r : a.getReviews()) {
			revWrite.append(r.toCSVLine());
		}
		revWrite.flush();
		revWrite.close();
	}

	private static void processSingleListing(String path, String file, int number) {
		long startTime = System.currentTimeMillis();
		Scout scout = new Scout();
		scout.fetchCachedListings();
		Listing a = allListings.getValue(number);
		Scraper s = new Scraper();
		s.getListingInfo(a);
		saveListing(file, a, number);
		System.out.println(
				"Total running time: " + ((System.currentTimeMillis() - startTime) / 1000.0 / 60) + " Minutes");
	}
}
