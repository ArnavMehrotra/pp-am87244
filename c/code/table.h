
typedef enum entry_type{
    NUM,
    STR
}_entry_type;

typedef struct _table_entry{
    enum entry_type type;    
    
}Table_Entry;

typedef struct _table{
    FILE* outfile;
    Table_Entry*** values;
}Table;
