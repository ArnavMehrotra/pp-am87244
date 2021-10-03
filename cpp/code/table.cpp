#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>

using namespace std;

typedef enum TYPE{
    STR,
    NUM
}_type;

typedef enum OPTYPE{
    EQ,
    GT,
    LT,
    NEQ
}_optype;

typedef union _value{
    string* str;
    double num;
}Value;

class TableEntry{
    public:
        TYPE type;
        Value val;
        TableEntry(const char* valString){
            if((atoi(valString) == 0) && (valString[0] != '0')){
                        type = STR;
                        val.str = new string(valString);
                    }
                    else{
                        type = NUM;
                        val.num = atof(valString);
                    }
        }
};



class Table{
    private:
        vector<vector<TableEntry> > values;
        string outFile;
        int rows;
        int cols;
        vector<string> header;
    public:
        Table(string tableFile, string out, bool head){
            outFile = out;
            ifstream t(tableFile.c_str());
            if(!t){
                cout << "OTHER ERROR\n";
                exit(-1);
            }
            string fileString;
            getline(t, fileString);
            int r = 0;
            int c = 0;

            if(head){
                char* tok = strtok((char*) fileString.c_str(), " \t");
                while(tok){
                    header.push_back(tok);
                    tok = strtok(NULL, " \t");
                    c++;
                }
                getline(t, fileString);
            }

            //read file into new table struct
            while(!fileString.empty()){
                vector<TableEntry> val_buff;
                stringstream ss(fileString);
                string val;
                int last_c = c;
                c = 0;
                while(ss >> val){
                    val_buff.push_back(TableEntry(val.c_str())); 
                    c++; //oops I did it again 
                }
                if((last_c != c) && (last_c != 0)){
                    cout << "NUM COLS ERROR\n";
                    exit(-1);
                }
                values.push_back(val_buff);
                getline(t, fileString);
                r++;
            }

            rows = r;
            cols = c;
            t.close();
        }

        void printCols(vector<int> printedCols){
            //prints columns
            FILE* out = fopen(outFile.c_str(), "w");

            //I decided to use fprintf for formatting reasons.
            if(!out){
                cout << "OTHER ERROR\n";
                exit(-1);
            }

            for(int i = 0; i<printedCols.size(); i++){
                if((printedCols[i] >= cols) || printedCols[i] < 0){
                    cout << "COL INDEX ERROR\n";
                    exit(-1);
                }
            }

            if(!header.empty()){
                for(int i = 0; i<printedCols.size(); i++){
                    fprintf(out, "%s ",header[printedCols[i]].c_str());
                }
                fprintf(out, "\n");
            }

            for(int i = 0; i<rows; i++){
                for(int j = 0; j<printedCols.size(); j++){
                    if(values[i][printedCols[j]].type == NUM){
                        fprintf(out, "%.5g ",values[i][printedCols[j]].val.num);
                    }
                    else{
                        fprintf(out, "%s ", values[i][printedCols[j]].val.str->c_str());
                    }
                }
                fprintf(out, "\n");
            }
            fclose(out);
        }

        void printRows(vector<int> printedRows){
            FILE* out = fopen(outFile.c_str(), "w");

            if(!out){
                cout << "OTHER ERROR\n";
                exit(-1);
            }

            if(!header.empty()){
                for(int i = 0; i<this->cols; i++){
                    fprintf(out, "%s ", header[i].c_str());
                }
                fprintf(out, "\n");
            }

            for(int i = 0; i<printedRows.size(); i++){
                for(int j = 0; j<this->rows; j++){
                    if(this->values[printedRows[i]][j].type == NUM){
                        fprintf(out, "%.5g ", this->values[printedRows[i]][j].val.num);
                    }
                    else{
                        fprintf(out, "%s ", this->values[printedRows[i]][j].val.str->c_str());
                    }
                }
                fprintf(out, "\n");
            }
            fclose(out);
        }

        int getNumRows(){
            return this->rows;
        }

        int getNumCols(){
            return this->cols;
        }

        vector<string> getHeader(){
            return this->header;
        }

        vector<vector<TableEntry> > getVals(){
            return this->values;
        }

        void sumAndPrint(int col){
            if(col >= this->cols){
                cout << "COL INDEX ERROR\n";
                exit(-1);
            }

            double sum = 0; 
            for(int i = 0; i<rows; i++){
                if(values[i][col].type == STR){
                    cout << "TYPE ERROR\n";
                    exit(-1);
                }
                sum += values[i][col].val.num; 
            } 
            

            //again using fprinf for formatting
            FILE* out = fopen(outFile.c_str(), "w");
            if(!header.empty()){
                fprintf(out, "%s\n", header[col].c_str());
            }
            fprintf(out, "%.5g\n", sum);
            fclose(out);
        }

};

class Operation{
    OPTYPE type;
    public:
        Operation(OPTYPE type){
            this->type = type;
        }

        bool eval(TableEntry val1, TableEntry val2){

            if(val1.type != val2.type){
                return false;
            }

            switch(this->type){
                case EQ:{
                    if(val1.type == NUM){
                        return val1.val.num == val2.val.num;
                    }
                    else{
                        return val1.val.str->compare(*val2.val.str) == 0;
                    }
                    break;
                }
                case LT:{
                    if(val1.type == NUM){
                        return val1.val.num < val2.val.num;
                    }
                    else{
                        return val1.val.str->compare(*val2.val.str) < 0;
                    }
                    break;
                }
                case GT:{
                    if(val1.type == NUM){
                        return val1.val.num > val2.val.num;
                    }
                    else{
                        return val1.val.str->compare(*val2.val.str) > 0;
                    }
                    break;
                }
                case NEQ:{
                    if(val1.type == NUM){
                        return val1.val.num != val2.val.num;
                    }
                    else{
                        return val1.val.str->compare(*val2.val.str) != 0;
                    }
                    break;
                }
            }
            return false;
        }
};


class Condition{
    private:
        Operation* op;
        vector<TableEntry> vals1;
        vector<TableEntry> vals2;
        Table* table;
    public:
        Condition(char* cond, Table* table){
            this->table = table;
            int opOffset = 1;
            switch(cond[0]){
                case '=':{
                    if(cond[1] == '='){
                        this->op = new Operation(EQ);                            
                        opOffset++;
                    }
                    else{
                        cout << "COND ERROR\n";
                        exit(-1);
                    }
                    break;
                }
                case '>':{
                    this->op = new Operation(GT);
                    break;
                }
                case '<':{
                    if(cond[1] == '>'){
                        this->op = new Operation(NEQ);
                        opOffset++;
                    }
                    else{
                        this->op = new Operation(LT);
                    }
                    break;
                }
                default:
                    cout << "COND ERROR\n";
                    exit(-1);
            }
            char* condVals = cond + opOffset;
            char* tok1 = strtok(condVals, " ,");
            char* tok2 = strtok(NULL, " ,");
            
            if(!tok1 || !tok2){
                cout << "COND ERROR\n";
                exit(-1);
            }
            
            switch(tok1[0]){
                case '@':{
                    tok1 += 1;
                    vector<string> header = table->getHeader();
                    if(header.empty()){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }
                    int col = 0;
                    string tokString = string(tok1);
                    for(int i = 0; i<table->getNumCols(); i++){
                        if(tokString.compare(header[i]) == 0){
                            col = i;
                            goto DONE1;
                        }
                    }
                    cout << "COND ERROR\n";
                    exit(-1);
                    DONE1:
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals1.push_back(tableVals[i][col]);
                    }
                    break;
                }
                case '$':{
                    tok1 += 1;
                    int col = atoi(tok1);
                    if ((col == 0) && (tok1[0] != '0')){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }

                    if(col >= table->getNumCols()){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }

                    string tokString = string(tok1);
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals1.push_back(tableVals[i][col]);
                    }
                    break;
                }
                default:{
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals1.push_back(TableEntry(tok1));
                    }
                    break;
                }  
            }

            switch(tok2[0]){
                case '@':{
                    tok2 += 1;
                    vector<string> header = table->getHeader();
                    if(header.empty()){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }
                    int col = 0;
                    string tokString = string(tok2);
                    for(int i = 0; i<table->getNumCols(); i++){
                        if(tokString.compare(header[i]) == 0){
                            col = i;
                            goto DONE2;
                        }
                    }
                    cout << "COND ERROR\n";
                    exit(-1);
                    DONE2:
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals2.push_back(tableVals[i][col]);
                    }
                    break;
                }
                case '$':{
                    tok2 += 1;
                    int col = atoi(tok2);
                    if ((col == 0) && (tok2[0] != '0')){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }

                    if(col >= table->getNumCols()){
                        cout << "COND ERROR\n";
                        exit(-1);
                    }

                    string tokString = string(tok2);
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals2.push_back(tableVals[i][col]);
                    }
                    break;
                }
                default:{
                    vector<vector<TableEntry> > tableVals = table->getVals();
                    for(int i = 0; i<table->getNumRows(); i++){
                        this->vals2.push_back(TableEntry(tok2));
                    }
                    break;
                }  
            }
        }
        void eval(){
            vector<int> printedRows;
            for(int i = 0; i<vals1.size(); i++){
                if(this->op->eval(this->vals1[i], this->vals2[i])){
                    printedRows.push_back(i);
                }
            }
            table->printRows(printedRows);
        }
};

int main(int argc, char* argv[]){
    bool header = (strcmp(argv[1], "-header") == 0);
    string cmdString = string(argv[header + 1]);

    if(argc < 4 + header){
        cout << "OTHER ERROR\n";
        exit(-1);
    }

    if(cmdString.compare("-print") == 0){
        vector<int> cols = vector<int>();
        char* colNum = strtok(argv[header+2], " ,");
        int j = 0;
        while(colNum != NULL){
            cols.push_back(atoi(colNum));
            if((cols[j] == 0) && (strcmp(colNum, "0") != 0)){
                cout << "COL INDEX ERROR\n";
                exit(-1);
            }
            colNum = strtok(NULL, " ,");
            j++;
        }
        Table* t = new Table(argv[header+3], argv[header+4], header);
        t->printCols(cols);
        delete t;
    }
    else if(cmdString.compare("-sum") == 0){
        int col = atoi(argv[header+2]);
        if((col == 0) && (strcmp(argv[header+2], "0") != 0)){
            cout << "COL INDEX ERROR\n";
            exit(-1);
        }
        Table* t = new Table(argv[header+3], argv[header+4], header);
        t->sumAndPrint(col);
        delete t;
    }
    else if(cmdString.compare("-when") == 0){
        Table* t = new Table(argv[header+3], argv[header+4], header);
        Condition condStruct = Condition(argv[header+2], t);
        condStruct.eval();
        delete t;
    }
    else{
        cout << "OTHER ERROR\n";
        exit(-1);
    }

    return 0;
}