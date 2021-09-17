import java.util.ArrayList;
import java.util.Map.Entry;

public class ActionGraph {
    private ArrayList<Action> actions;

    public ActionGraph(Action firstAction){
        buildGraph(firstAction);
    }

    private void buildGraph(Action action){
        actions.add(action);
        ArrayList<Pair> pairs = action.getCells();
        Table table = action.getTable();
        for(int i = 0; i<pairs.size(); i++){
            if(table.isAction(pairs.get(i)) == 1 && table.isAction(action.getIndex()) == 1){
                Action newAction = table.getAction(pairs.get(i));
                action.addDependency(newAction);
                buildGraph(newAction);
            }
            else if(table.isAction(pairs.get(i)) == 0 && table.isAction(action.getIndex()) == 0){
                Action newAction = table.getAction(pairs.get(i));
                action.addDependency(newAction);
                buildGraph(newAction);
            }
            else if(table.isAction(pairs.get(i)) == -1){
                continue;
            }
            else{
                System.out.println("TYPE ERROR");
                System.exit(-1);
            }
        }
    }
}

abstract class Action{
    private Table table;
    private Pair index;
    private ArrayList<Pair> cells;
    private ArrayList<Action> dependencies;

    public Action(Table table, Pair index){
        this.index = index;
        this.table = table;
        this.cells = new ArrayList<Pair>();
        this.dependencies = new ArrayList<Action>();
    }

    public Pair getIndex(){
        return this.index;
    }
    public void addDependency(Action action){
        this.dependencies.add(action);
    }
    public void addCell(Pair cell){
        this.cells.add(cell);
    }

    public Table getTable(){
        return this.table;
    }

    public ArrayList<Pair> getCells(){
        return this.cells;
    }

    abstract void execute();
}

class sum extends Action{
    public sum(Table table, Pair index, String args){
        super(table, index);
        String[] pairStrings = args.split("\\[");
        for(int i = 0; i<pairStrings.length; i++){
            String rowString = pairStrings[i].split(",")[0];
            String colString = pairStrings[i].split(",")[1].replace("\\]", "");
            int row = 0;
            int col = 0;
            try{
                row = Integer.parseInt(rowString);
                col = Integer.parseInt(colString);
                addCell(new Pair(row, col));
            } catch(NumberFormatException n){
                System.out.println("COL INDEX ERROR");
                System.exit(-1);
            }
        }
    }
    @Override
    void execute(){
        ArrayList<Pair> cells = getCells();
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
        if(sum % 1 == 0){
            table.update(this.getIndex().getCol(), this.getIndex().getCol(), "" +((int) sum));
        }
        else{
            table.update(this.getIndex().getCol(), this.getIndex().getCol(), "" + sum);
        } 
    }
}

class avg extends Action{
    public avg(Table table, Pair index, String args){
        super(table, index);
        String[] pairStrings = args.split("\\[");
        for(int i = 0; i<pairStrings.length; i++){
            String rowString = pairStrings[i].split(",")[0];
            String colString = pairStrings[i].split(",")[1].replace("\\]", "");
            int row = 0;
            int col = 0;
            try{
                row = Integer.parseInt(rowString);
                col = Integer.parseInt(colString);
                addCell(new Pair(row, col));
            } catch(NumberFormatException n){
                System.out.println("COL INDEX ERROR");
                System.exit(-1);
            }
        }
    }
    @Override
    void execute(){
        ArrayList<Pair> cells = getCells();
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

class to_lower extends Action{
    public to_lower(Table table, Pair index, String args){
        super(table, index);
        String coorString =  args.replaceAll("[\\(\\[\\]\\)]", "");
        int row = 0;
        int col = 0;
        String[] pair = coorString.split(",");
        try{
            row = Integer.parseInt(pair[0]);
            col = Integer.parseInt(pair[1]);
        } catch(NumberFormatException n){
            System.out.println("INDEX ERROR");
            System.exit(-1);
        }
        index = new Pair(row, col);
        
    }
    @Override
    void execute(){
        Pair cell = getCells().get(0);
        Table table = getTable();
        try{
            table.update(cell.getCol(), cell.getRow(), table.getVal(cell).toLowerCase());
        }catch(IndexOutOfBoundsException e){
            System.out.println("COL INDEX ERROR");
            System.exit(-1);
        }
    }
}

class to_upper extends Action{
    public to_upper(Table table, Pair index, String args){
        super(table, index);
        String coorString =  args.replaceAll("[\\(\\[\\]\\)]", "");
        int row = 0;
        int col = 0;
        String[] pair = coorString.split(",");
        try{
            row = Integer.parseInt(pair[0]);
            col = Integer.parseInt(pair[1]);
        } catch(NumberFormatException n){
            System.out.println("INDEX ERROR");
            System.exit(-1);
        }
        index = new Pair(row, col);
    }
    @Override
    void execute(){
        Pair cell = getCells().get(0);
        Table table = getTable();
        try{
            table.update(cell.getCol(), cell.getRow(), table.getVal(cell).toUpperCase());
        }catch(IndexOutOfBoundsException e){
            System.out.println("COL INDEX ERROR");
            System.exit(-1);
        } 
    } 
}

class Pair{
    private int row;
    private int col;

    public Pair(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
}