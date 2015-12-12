
 import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ExtractTest {
 
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "";
        String driver="com.mysql.jdbc.Driver";
        String password = "";
        String dbname="pbl";
 
    
 
        try {
        	Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+dbname, user, password);
 
            String sql = "INSERT INTO train (phraseID, phrase,sent_val) values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            BufferedReader br = new BufferedReader(new FileReader("train.tsv"));
            
               // StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                	
                while (line != null) {
                   
                	System.out.println(line);
                    String arr[]=line.split("\t");
                    System.out.println(arr[2]);
                    statement.setInt(1, Integer.parseInt(arr[0]));
                    statement.setString(2, arr[2]);
                    statement.setInt(3, Integer.parseInt(arr[3]));
                	line = br.readLine();
                	statement.execute();
                }
                //String everything = sb.toString();
   
        
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        catch(Exception e){}
    }
}