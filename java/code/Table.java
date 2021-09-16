import java.lang.reflect.Array;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    //TODO: ERROR HANDLING
    private ArrayList<ArrayList<String>> matrix;
    private String header;
    private String outFile;

    public Table(String tableFileString, String outFile,  boolean header){
        this.outFile = outFile;
        this.matrix = new ArrayList<>();
        try{
            BufferedReader tableReader = new BufferedReader(new FileReader(Paths.get(tableFileString).toFile()));
            if(header != false){
                this.header = tableReader.readLine();
            }
            String line = tableReader.readLine();
            int j = 0;
            while(line != null){
                String[] tokens = line.split("\\s");
                matrix.add(new ArrayList<String>());
                for(int i = 0; i<tokens.length; i++){
                    matrix.get(j).add(tokens[i]);
                }
                line = tableReader.readLine();
                j++;
            }
            tableReader.close();
        } catch(IOException i){
            //TODO:: handle exception
            System.exit(-1);
        }
        
    }
    public static void main(String[] args) {
        launch_args(args);
    }

    private static void launch_args(String[] args){

        if(args.length == 0){
            //TODO: figure out the actual error
            System.out.println("please provide arguments");
            System.exit(-1);
        }
        boolean header = false;
        if(args[0].equals("-header")){
            header = true;
        }

        int cmdIndex = header ? 1 : 0;
        String cmd = args[cmdIndex];
        switch(cmd){
            case "-print":{
                //TODO: ERROR HANDLING
                String[] colsStrings = args[cmdIndex+1].split(",");
                String tableFileString = args[cmdIndex+2];
                String outFileString = args[cmdIndex+3];

                //read columns
                int[] cols = new int[colsStrings.length];
                for(int i = 0; i<colsStrings.length; i++){
                    try {
                        cols[i] = Integer.parseInt(colsStrings[i]);
                    } catch (Exception e) {
                        System.out.println("COL INDEX ERROR");
                    }
                }
                Table table = new Table(tableFileString, outFileString, header);
                table.printCols(cols);
                break;
            }
            case "-sum":{
                //TODO: ERROR HANDLING
                String colString = args[cmdIndex+1];
                String tableFileString = args[cmdIndex+2];
                String outFileString = args[cmdIndex+3];
                int col = 0;
                try{
                    col = Integer.parseInt(colString);
                }catch(NumberFormatException n){
                    System.out.println("COL INDEX ERROR");
                }
                
                Table table = new Table(tableFileString, outFileString, header);
                table.sumCol(col);
                    
                break;
            }
            case "-action":{
                //TODO: action
                break;
            }
            case "-when":{
                //TODO: when
                break;
            }
            case "-update":{
                //TODO: ERROR HANDLING
                String infoString = args[cmdIndex + 1];
                String tableFileString = args[cmdIndex+2];
                String outFileString = args[cmdIndex + 3];
                
                int col = 0;
                int row = 0;
                String val = "";
                String[] infoArr = infoString.split(",");
                try{
                    col = Integer.parseInt(infoArr[0]);
                    row = Integer.parseInt(infoArr[1]);
                    val = infoArr[2];
                }catch(Exception e){
                    System.out.println("INDEX ERROR");
                    System.exit(-1);
                }

                Table table = new Table(tableFileString, outFileString, header);
                table.update(col, row, val);

                int[] cols = new int[table.matrix.get(0).size()];
                for(int i = 0; i<cols.length; i++){
                    cols[i] = i;
                }
                table.printCols(cols);

                break;
            }
            default:{
                try {
                    FileInputStream fileStream = new FileInputStream(Paths.get(cmd).toFile());
                    String cmd_string = (String) fileStream.toString();
                    String cmd_strings[] = cmd_string.split("\\s+");
                    for(int i = 0; i<cmd_strings.length; i++){
                        launch_args(cmd_strings[i].split("\\s+"));
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println("FIGURE OUT WHAT TO PRINT");
                }
                break;
            }
        }

    }

    public void printCols(int[] cols){
        try{
            PrintWriter printer = new PrintWriter(outFile);
            if(header != null){
                printer.println(header);
            }
            for(int i = 0; i<matrix.get(0).size(); i++){
                for(int j = 0; j<cols.length; j++){
                    printer.print(matrix.get(i).get(cols[j]) + " "); 
                }
                printer.print("\n");
            }
            printer.flush();
            printer.close();
        } catch(FileNotFoundException f){
            //TODO: handle exception
            System.out.println("oopside");
            System.exit(-1);
        } catch(IndexOutOfBoundsException i){
            System.out.println("COL INDEX ERROR");
        }
        
    }

    public void sumCol(int col){
        float sum = 0;
        try{
            for(int i = 0; i<matrix.get(0).size(); i++){
                sum += Float.parseFloat(matrix.get(i).get(col));
            }
            PrintWriter printer = new PrintWriter(outFile);
            if(sum %1 == 0){
                printer.print((int) sum);
                printer.flush();
                printer.close();
                return;
            }
            printer.print(sum);
            printer.flush();
            printer.close();
        } catch(IndexOutOfBoundsException i){
            System.out.println("COL INDEX ERROR");
            System.exit(-1);
        } catch(NumberFormatException n){
            System.out.println("TYPE ERROR");
            System.exit(-1);
        }
        catch(FileNotFoundException f){
            //TODO: handle exception properly
            System.exit(-1);
        }
        return;
    }

    public void update(int col, int row, String val){
        try{
            matrix.get(row).set(col, val);
        }catch(IndexOutOfBoundsException i){
            System.out.println("INDEX ERROR");
            System.exit(-1);
        }
    }
}