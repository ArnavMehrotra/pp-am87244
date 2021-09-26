
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

Table* build_table(int header, char* table_file_string, char* out_file_string);
void free_table(Table* table);
void sum_col(int col, Table* table);
void print_cols(Table* table, int cols[], int n_cols);
void print_when(Table* table, char* cond, int operation);