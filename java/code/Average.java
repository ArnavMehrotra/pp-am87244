import java.util.ArrayList;

//subclass for average action
public class Average extends TableAction{
    public Average(Table table, IndexPair index, String args){
        super(table, index);
        String fmtArgs = args.replaceAll("\\(", "").replaceAll("\\)", "");
        String[] pairStrings = fmtArgs.split("\\[");
        for(int i = 0; i<pairStrings.length; i++){
            String rowString = "";
            String colString = "";
            try{
                rowString = pairStrings[i].split(",")[0];
                colString = pairStrings[i].split(",")[1].replace("]", "");
            }catch(Exception e){
                continue;
            }
            int row = 0;
            int col = 0;
            try{
                row = Integer.parseInt(rowString);
                col = Integer.parseInt(colString);
                addCell(new IndexPair(row, col));
            } catch(NumberFormatException n){
                System.out.println("COL INDEX ERROR");
                System.exit(-1);
            }
        }
    }
    @Override
    void execute(){
        ArrayList<IndexPair> cells = getCells();
        Table table = getTable();
        float sum = 0;
        for(int i = 0; i<cells.size(); i++){
            float buff = 0;
            try{
                buff = Float.parseFloat(table.getVal(cells.get(i))); 
            } catch(NumberFormatException n){
                System.out.println("TYPE ERROR");
                System.exit(-1);
            } catch(IndexOutOfBoundsException o){
                System.out.println("COL INDEX ERROR");
                System.exit(-1);
            }
            sum += buff;
        }
        float avg = sum / cells.size();
        if(avg % 1 == 0){
            table.update(this.getIndex().getCol(), this.getIndex().getCol(), "" +((int) avg));
        }
        else{
            table.update(this.getIndex().getCol(), this.getIndex().getCol(), "" + avg);
        } 
    }
}
