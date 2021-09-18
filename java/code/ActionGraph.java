import java.util.ArrayList;

public class ActionGraph {
    private ArrayList<TableAction> actions;
    private String result;

    public ActionGraph(TableAction firstAction){
        this.actions = new ArrayList<TableAction>();
        buildGraph(firstAction);
    }
    private void buildGraph(TableAction action){
        actions.add(action);
        ArrayList<IndexPair> pairs = action.getCells();
        Table table = action.getTable();
        for(int i = 0; i<pairs.size(); i++){
            if(table.isAction(pairs.get(i)) == 1 && table.isAction(action.getIndex()) == 1){
                TableAction newAction = table.getAction(pairs.get(i));
                //absurdly inneficient, but easy to debug
                for(int j = 0; j<actions.size(); j++){
                    int row_one = actions.get(j).getIndex().getRow();
                    int col_one = actions.get(j).getIndex().getCol();
                    int new_row =  newAction.getIndex().getRow();
                    int new_col = newAction.getIndex().getCol();
                    if(row_one == new_row && col_one == new_col){
                        System.out.println("DAG ERROR");
                        System.exit(-1);
                    }
                }
                action.addDependency(newAction);
                buildGraph(newAction);
            }
            else if(table.isAction(pairs.get(i)) == 0 && table.isAction(action.getIndex()) == 0){
                TableAction newAction = table.getAction(pairs.get(i));
               //absurdly inneficient, but easy to debug
               for(int j = 0; j<actions.size(); j++){
                    int row_one = actions.get(j).getIndex().getRow();
                    int col_one = actions.get(j).getIndex().getCol();
                    int new_row =  newAction.getIndex().getRow();
                    int new_col = newAction.getIndex().getCol();
                    if(row_one == new_row && col_one == new_col){
                        System.out.println("DAG ERROR");
                        System.exit(-1);
                    }
                } 
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
    public void eval(){
        for(int i = actions.size(); i > 1; i--){
            actions.get(i).execute();
            actions.get(i-1).setTable(actions.get(i).getTable());
        }
        actions.get(0).execute();
        result = actions.get(0).getTable().getVal(actions.get(0).getIndex());
    }
    public String getResult(){
        return this.result;
    }
}
