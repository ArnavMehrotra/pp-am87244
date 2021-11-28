import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.*;

public class Main {
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/test";  

    static final String USER = "sa"; 
    static final String PASS = ""; 

    public static void main(String[] args){
        System.out.println("args: " + Arrays.toString(args));
        try{
            Class.forName ("org.h2.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            readTable(conn, args[0]);
            System.out.println("success");
            conn.close();
        }
        catch(Exception e){
            System.out.println("OTHER ERROR");
        }
    }

    public static void readTable(Connection c, String s){
        try{
            BufferedReader tableReader = new BufferedReader(new FileReader(Paths.get(s).toFile()));
            String line = tableReader.readLine();
            while(line != null){
                String[] tokens = line.split("\\s");
            }
        }
        catch(Exception e){
            System.out.println("OTHER ERROR");
        }
    }
}
