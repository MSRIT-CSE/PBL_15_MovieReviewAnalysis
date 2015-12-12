import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class finalpbl {

	public static void main(String[] args) throws IOException {
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String driver = "com.mysql.jdbc.Driver";
		String password = "";
		String dbname = "pbl";
		float positive_measure = 0;
		float total_measure = 0;
		FileWriter fileWriter = new FileWriter("pbl.csv");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		

		long startTime = System.currentTimeMillis();

		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbname, user,
					password);

			String sql = "select * from train where phrase=?";
			PreparedStatement statement = conn.prepareStatement(sql);

			BufferedReader br = new BufferedReader(new FileReader("pblfile"));
			String line = br.readLine();
			line = br.readLine();
			double sum = 0;
			int n = 0;
			int emotion_counter[] = new int[5];
			int c = 0;

			while (line != null) {
				int emotion;
				line = br.readLine();
				String arr[] = line.split("\t");
				statement.setString(1, arr[0].trim());
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					emotion = rs.getInt(3);
					sum += emotion;
					emotion_counter[emotion]++;
					n++;
				}
			}

			sum /= n;
			System.out.println("The emotion value is : " + sum);
			if (sum <= 2.01 && sum >= 1.99)
				System.out.println("The review is almost neutral");
			else if (sum > 2.01 && sum <= 2.05)
				System.out.println("The review is moderately positive");
			else if (sum > 2.05 && sum <= 2.15)
				System.out.println("The review is positive");
			else if (sum > 2.15)
				System.out.println("The review is extremely positive");
			else if (sum < 1.99 && sum >= 1.95)
				System.out.println("The review is moderately negative");
			else if (sum < 1.95 && sum >= 1.85)
				System.out.println("The review is negative");
			else if (sum < 1.85)
				System.out.println("The review is extremely negative");

			System.out.println();
			System.out
					.println("The number of very negative phrases (emotion value 0) are : "
							+ emotion_counter[0]);
			System.out
					.println("The number of moderately negative phrases (emotion value 1) are : "
							+ emotion_counter[1]);
			System.out
					.println("The number of neutral phrases (emotion value 2) are : "
							+ emotion_counter[2]);
			System.out
					.println("The number of positive phrases (emotion value 3) are : "
							+ emotion_counter[3]);
			System.out
					.println("The number of very positive phrases (emotion value 4) are : "
							+ emotion_counter[4]);
			
			bufferedWriter.write("Emotion\tValue\n");
			bufferedWriter.write("Very_Negative\t" + emotion_counter[0] + "\n");
			bufferedWriter.write("Negative\t" + emotion_counter[1] + "\n");
			bufferedWriter.write("Neutral\t" + emotion_counter[2] + "\n");
			bufferedWriter.write("Positive\t" + emotion_counter[3] + "\n");
			bufferedWriter.write("Very_Positive\t" + emotion_counter[4] + "\n");
			
			positive_measure = emotion_counter[3] * 1 + emotion_counter[4] * 2;
			total_measure = emotion_counter[1] * 1 + emotion_counter[0] * 2
					+ positive_measure;
			float positive_percent = (positive_measure * 100) / total_measure;
			System.out.println("\nIt is " + positive_percent
					+ "% positive and " + (100 - positive_percent)
					+ "% negative.");

			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		bufferedWriter.close();
		long endTime = System.currentTimeMillis();
		System.out.println("\nTime in seconds "
				+ (double) (endTime - startTime) / 1000.0);
	}
}