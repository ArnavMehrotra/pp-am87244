import java.util.ArrayList;

//abstract class for all actions
//Each action has it's own execution function and special constructor.
public abstract class TableAction{
    private Table table;
    private IndexPair index;
    private ArrayList<IndexPair> cells;
    private ArrayList<TableAction> dependencies;

    public TableAction(Table table, IndexPair index){
        this.index = index;
        this.table = table;
        this.cells = new ArrayList<IndexPair>();
        this.dependencies = new ArrayList<TableAction>();
    }

    public void setTable(Table table){
        this.table = table;
    }

    public IndexPair getIndex(){
        return this.index;
    }
    public void addDependency(TableAction action){
        this.dependencies.add(action);
    }
    public void addCell(IndexPair cell){
        this.cells.add(cell);
    }

    public Table getTable(){
        return this.table;
    }

    public ArrayList<IndexPair> getCells(){
        return this.cells;
    }

    abstract void execute();
}