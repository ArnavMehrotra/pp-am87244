
typedef enum entry_type{
    NUM,
    STR
}_entry_type;

typedef union _value{
    double num;
    char* str;
}Value;

typedef struct _table_entry{
    enum entry_type type;    
    Value val;
     
}Table_Entry;

typedef struct _table{
    int num_rows;
    int num_cols;
    char* outfile;
    char** header;
    Table_Entry** values;
}Table;
