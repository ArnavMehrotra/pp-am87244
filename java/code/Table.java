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
    public String getVal(IndexPair pair){
        int row = pair.getRow();
        int col = pair.getCol();
       return this.matrix.get(row).get(col); 
    }

    public TableAction getAction(IndexPair pair){
        String[] values = getVal(pair).split("__");
        switch(values[0]){
            case "sum":
                return (TableAction) new Sum(this, pair, values[1]);
            case "avg":
                return (TableAction) new Average(this, pair, values[1]);
            case "to_lower":
                return (TableAction) new ToLower(this, pair, values[1]);
            case "to_upper":
                return (TableAction) new ToUpper(this, pair, values[1]);
        }
        return null;
    }

    public int isAction(IndexPair pair){
        String action = this.matrix.get(pair.getRow()).get(pair.getCol()).split("__")[0];
        switch(action){
            case "sum":
            case "avg":
                return 0;
            case "to_upper":
            case "to_lower":
                return 1;
            default:
                return -1;
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
                if (args.length - cmdIndex != 4){
                    System.out.println("OTHER ERROR");
                    System.exit(-1);
                } 
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
                if (args.length - cmdIndex != 4){
                    System.out.println("OTHER ERROR");
                    System.exit(-1);
                }
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
                if (args.length - cmdIndex != 4){
                    System.out.println("OTHER ERROR");
                    System.exit(-1);
                } 
                String[] colsString = args[cmdIndex+1].split(",");
                int[] cols = new int[colsString.length];
                boolean isCols = true;
                int offset = cmdIndex;
                if(colsString.length == 1){
                    offset += 1;
                    isCols = false;
                }
                else{
                    for(int i = 0; i<colsString.length; i++){
                        try {
                            cols[i] = Integer.parseInt(colsString[i]);
                        } catch (Exception e) {
                            System.out.println("COL INDEX ERROR");
                        }
                    }
                }
                String tableFileString = args[offset+1];
                String outFileString = args[offset+2];
                Table table = new Table(tableFileString, outFileString, header);
                
                if(!isCols){
                    for(int i = 0; i<table.matrix.get(0).size(); i++){
                        cols[i] = i;
                    }
                }

                for(int i = 0; i<table.matrix.get(0).size(); i++){
                    for(int j = 0; j<cols.length; j++){
                        IndexPair pair = new IndexPair(i, cols[j]);
                        if(table.isAction(pair) > -1){
                            TableAction action = table.getAction(pair);
                            ActionGraph graph = new ActionGraph(action);
                            graph.eval();
                            table.update(action.getIndex().getRow(), action.getIndex().getCol(), graph.getResult());
                        }
                    }
                }
                table.printCols(cols);
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
                //TODO: ERROR HANDLING
                
                String opFileString = args[0];
                String tableFileString = args[1];
                String outFileString = args[2];
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(Paths.get(opFileString).toFile()));
                    String line = reader.readLine();

                    //you could super easily overflow the stack with my program by reusing the OPFILE command in the OPFILE
                    //that being said, I couldn't think of a better way to do this.
                    while (line != null){
                        String full_cmd = line + " " + tableFileString + " " + outFileString;
                        launch_args(full_cmd.split("\\s+"));
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