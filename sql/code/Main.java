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

    static final float REALLY_SMALL = -1*Float.MAX_VALUE;

    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("OTHER ERROR");
            System.exit(-1);
        }
        Connection conn = null;
        try{
            Class.forName ("org.h2.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement s = conn.createStatement();
            s.executeUpdate("DROP TABLE MATRIX IF EXISTS");
            String ct = "CREATE TABLE MATRIX " +
                "(c INTEGER NOT NULL, " +
                "r INTEGER, " +
                "t INTEGER NOT NULL, " +
                "intval INTEGER, " +
                "fltval DOUBLE PRECISION, " +
                "strval VARCHAR(255))";
            s.executeUpdate(ct);
            s.close();
            outTable(conn, args[1], readTable(conn, args[0]));
            System.out.println("success");
            conn.close();
        }
        catch(Exception e){
            //TODO:: remove debug code
            e.printStackTrace();
            System.out.println("OTHER ERROR");
        }
    }

    private static int readTable(Connection c, String tableFileString){
        try{
            //get cursor for db
            Statement stmt  = c.createStatement();
            //get reader for file
            BufferedReader tableReader = new BufferedReader(new FileReader(Paths.get(tableFileString).toFile()));
            //read header and insert in DB
            String line = tableReader.readLine();
            String[] header = line.split("\\s");
            int n_cols = header.length;
            for(int i = 0; i<n_cols; i++){
                String q = "INSERT INTO MATRIX VALUES(" + i + ", NULL, " + "0,  NULL, NULL, '" + header[i] + "');"; 
                stmt.executeUpdate(q);
            }

            //read the rest of the table and insert in DB
            line = tableReader.readLine();
            int row = 0; 
            while(line != null){
                String[] tokens = line.split("\\s");
                if(tokens.length != n_cols){
                    System.out.println("NUM COLS ERROR");
                    System.exit(-1);
                }
                for(int i = 0; i<tokens.length; i++){
                    String q = "";
                    try{
                        float val = Float.parseFloat(tokens[i]);
                        if(val % 1 == 0){
                            q = "INSERT INTO MATRIX VALUES(" + i + ", " + row + ", " + "1, " + (int) val + ", NULL, NULL);";
                        }
                        else{
                            q = "INSERT INTO MATRIX VALUES(" + i + ", " + row + ", " + "2, NULL, " + val + " , NULL);";
                        }
                    }
                    catch(NumberFormatException n){
                        q = "INSERT INTO MATRIX VALUES(" + i + ", " + row + ", " + "3, NULL, NULL, '"+tokens[i]+ "');";
                    }
                    catch(Exception e){
                        System.out.println("OTHER ERROR");
                    }
                    //System.out.println(q);
                    stmt.executeUpdate(q);
                }
                line = tableReader.readLine();
                row++;
            }
            stmt.close();
            return n_cols;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OTHER ERROR");
        }
        return -1;
    }

    private static void outTable(Connection c, String outFileString, int n_cols){
        try{ 
            Statement s = c.createStatement();
            float maxSum = REALLY_SMALL;
            int maxCol = -1;
            for(int i =0; i<n_cols; i++){
                float sum = 0;
                boolean op = false;
                ResultSet res = s.executeQuery("SELECT t, intval, fltval FROM MATRIX WHERE (t=2 OR t=1) AND c=" + i + ";");
                while(res.next()){
                    if(res.getInt("t") == 1){
                        op = true;
                        sum += res.getInt("intval");
                    }
                    else{
                        op = true;
                        sum += res.getDouble("fltval");
                    }
                }
                if(op && sum > maxSum){
                    maxSum = sum;
                    maxCol = i;
                }
                res.close();
            }            
            if(maxCol == -1){
                //empty file
                s.close();
                return;
            }
            ResultSet res = s.executeQuery("SELECT strval FROM MATRIX WHERE (t=0) AND c=" + maxCol + ";");
            res.next();
            System.out.println(res.getString("strval"));
            res.close();
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OTHER ERROR");
        }
    }
}
