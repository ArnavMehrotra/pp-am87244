import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;


public class Table {
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
            System.out.println("OTHER ERROR");
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
        switch(values[1]){
            case "sum":
                return (TableAction) new Sum(this, pair, values[2]);
            case "avg":
                return (TableAction) new Average(this, pair, values[2]);
            case "to_lower":
                return (TableAction) new ToLower(this, pair, values[2]);
            case "to_upper":
                return (TableAction) new ToUpper(this, pair, values[2]);
        }
        return null;
    }

    public int isAction(IndexPair pair){
        String action = "";
        try{
            action = this.matrix.get(pair.getRow()).get(pair.getCol()).split("__")[1];
        }
        catch(Exception e){
            return -1;
        }
        
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
            System.out.println("OTHER ERROR");
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
                if (args.length - cmdIndex < 3){
                    System.out.println("OTHER ERROR");
                    System.exit(-1);
                } 

                boolean hasCols = false;
                if(header && args.length == 5){
                   hasCols = true; 
                }
                else if(!header && args.length == 4){
                    hasCols = true;
                }
                 
                int offset = (hasCols ? 1:0) + cmdIndex;
                String tableFileString = args[offset+1];
                String outFileString = args[offset+2];
                Table table = new Table(tableFileString, outFileString, header);
                
                int[] cols;
                if(hasCols){
                    String[] colsString = args[cmdIndex+1].split(",");
                    cols = new int[colsString.length];
                    for(int i = 0; i<colsString.length; i++){
                        try {
                            cols[i] = Integer.parseInt(colsString[i]);
                        } catch (Exception e) {
                            System.out.println("COL INDEX ERROR");
                            System.exit(-1);
                        }
                    }
                }
                else{
                    cols = new int[table.matrix.get(0).size()];
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
                            table.update(action.getIndex().getCol(), action.getIndex().getRow(), graph.getResult());
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
                    System.out.println("OTHER ERROR");
                    System.exit(-1);
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
            for(int i = 0; i<matrix.size(); i++){
                for(int j = 0; j<cols.length; j++){
                    printer.print(matrix.get(i).get(cols[j]) + " "); 
                }
                printer.print("\n");
            }
            printer.flush();
            printer.close();
        } catch(FileNotFoundException f){
            System.out.println("OTHER ERROR");
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
            if(header != null){
                printer.println(header);
            }
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
            System.out.println("OTHER ERROR");
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