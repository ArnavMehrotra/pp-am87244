class ToUpper extends TableAction{
    public ToUpper(Table table, IndexPair index, String args){
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
        index = new IndexPair(row, col);
        addCell(index);
    }

    @Override
    void execute(){
        IndexPair cell = getCells().get(0);
        Table table = getTable();
        try{
            table.update(getIndex().getCol(), getIndex().getRow(), table.getVal(cell).toUpperCase());
        }catch(IndexOutOfBoundsException e){
            System.out.println("COL INDEX ERROR");
            System.exit(-1);
        }
    }
}