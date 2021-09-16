import java.lang.reflect.Array;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    //TODO: ERROR HANDLING
    private ArrayList<ArrayList<String>> matrix;
    private String outFile;
    private boolean header;

    public Table(String tableFileString, String outFile, boolean header){
        this.header = header;
        this.matrix = new ArrayList<>();
        this.outFile = outFile;
        try{
            BufferedReader tableReader = new BufferedReader(new FileReader(Paths.get(tableFileString).toFile()));
            if(header){
                tableReader.readLine();
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
        }

    }
    public static void main(String[] args) {
        if(args.length == 0){
            //TODO: figure out the actual error
            System.out.println("please provide arguments");
            System.exit(-1);
        }
        launch_args(args);
    }

    private static void launch_args(String[] args){
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
                        Integer.parseInt(colsStrings[i]);
                    } catch (Exception e) {
                        System.out.println("COL INDEX ERROR");
                    }
                }
                
                Table table = new Table(tableFileString, outFileString, header);
                table.printCols(cols);
                break;
            }
            case "-sum":{
                //TODO: sum
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
                //TODO: update
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

    private void printCols(int[] cols){
        try{
            FileOutputStream outFileStream = new FileOutputStream(Paths.get(outFile).toFile());
            System.out.println(matrix);
            for(int i = 0; i<matrix.get(0).size(); i++){
                for(int j = 0; j<cols.length; j++){
                    try {
                        outFileStream.write(matrix.get(i).get(j).getBytes()); 
                    } catch (IndexOutOfBoundsException a) {
                        System.out.println("COL INDEX ERROR");
                    }
                    outFileStream.write("\n".getBytes());
                }
            }
            outFileStream.flush();
        } catch(Exception e){
            //TODO: handle exception
            System.exit(-1);
        }
        
    }
}