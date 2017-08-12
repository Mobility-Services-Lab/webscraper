
public class Review {
	private String writerName;
	private String rating;
	private String headline;
	private String content;
	private String date;
	private String likes;
	public static String headers = "Writer's Name, Rating out of 50, Headline, Content, Date and Time, Number of Likes";
	private Review() {
		// TODO Auto-generated constructor stub
		this("");
	}

	public Review(String comment) {
		try {
			/*
			 * Pattern is: <name> - <headline>\r\n<comment>\r\n<date> · Comment · Like ·[
			 * <number of Likes> ·] Report Abuse\r\nWrite a comment...
			 */
			String[] lines = comment.split("(\n|\r\n)");
			String[] firstLine = lines[0].split(" - ");
			writerName = firstLine[0];
			headline = firstLine[1];
			content = "";
			for (int i = 1; i < lines.length - 2; i++) { // everything except first and last two lines.
				content += lines[i];
			}
			String[] lastLine = lines[lines.length - 2].split(" · ");
			date = lastLine[0];
			if (lastLine.length == 5) {
				likes = lastLine[3];
			} else {
				likes = "0";
			}
		} catch (Exception e) {
			writerName = "Parse Error";
			rating = "Parse Error";
			headline = "Parse Error";
			content = "Parse Error";
			date = "Parse Error";
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	@Override
	public String toString() {
		return "{"
	+ "\n\t\tWriter's Name:" + writerName 
	+ "\n\t\tRating, out of 50: " + rating 
	+ "\n\t\tHeadline: " + headline
	+ "\n\t\tContent: " + content
	+ "\n\t\tDate and Time: " + date 
	+ "\n\t\tNumber of Likes: " + likes
	+ "\n}\n";
	}
	public String toCSVLine() {
		return App.prepCSVCell(writerName)
		+ "," + App.prepCSVCell(rating)
		+ "," + App.prepCSVCell(headline)
		+ "," + App.prepCSVCell(content)
		+ "," + App.prepCSVCell(date)
		+ "," + App.prepCSVCell(likes)
		+ "\n";
	}
}