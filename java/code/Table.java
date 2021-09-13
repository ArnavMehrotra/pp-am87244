import java.lang.reflect.Array;
import java.io.*;
import java.nio.file.*;

public class Table {
    //TODO: ERROR HANDLING
    public static void main(String[] args) {
        parse_args(args);
    }

    private static void parse_args(String[] args){
        boolean header = false;
        if(args[0].strip().equals("-header")){
            header = true;
        }
        int cmdIndex = header ? 1 : 0;
        String cmd = args[cmdIndex];
        switch(cmd){
            case "-print":{
                //TODO: print
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
                    String cmd_strings[] = (String[]) fileStream.toString().lines().toArray();
                    for(int i = 0; i<cmd_strings.length; i++){
                        parse_args(cmd_strings[i].split("\\s+"));
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println("FIGURE OUT WHAT TO PRINT");
                }
                break;
            }
        }
    }

    private static int[][] read_table(){
        return null;
    }
}