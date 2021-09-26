
typedef enum entry_type{
    NUM,
    STR
}_entry_type;

typedef enum op_type{
    COL,
    CONSTNUM,
    CONSTSTR
}_op_type;

typedef union _value{
    double num;
    char* str;
}Value;

typedef enum when_cond{
    LT,
    GT,
    EQ,
    NEQ
}_when_cond;

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
