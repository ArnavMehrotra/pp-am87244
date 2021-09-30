
#include <iostream>
#include <string.h>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>

using namespace std;

typedef enum TYPE{
    STR,
    NUM
}_type;

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
        ~TableEntry(){
            if(type == STR){
                delete val.str;
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
            ifstream t(tableFile);
            string fileString;
            getline(t, fileString);
            int r = 0;
            int c = 0;

            if(head){
                char* tok = strtok((char*) fileString.c_str(), " \t");
                while(tok){
                    header.push_back(tok);
                    cout << tok << "\n";
                    tok = strtok(NULL, " \t");
                    c++;
                }
            }

            getline(t, fileString);
            //read file into buffer
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
        }

        void printCols(vector<int> printCols){
            FILE* out = fopen(outFile.c_str(), "w");

            if(!out){
                cout << "OTHER ERROR\n";
            }

            for(int i = 0; i<printCols.size(); i++){
                if((printCols[i] >= cols) || printCols[i] < 0){
                    cout << "COL INDEX ERROR\n";
                }
            }

            if(!header.empty()){
                for(int i = 0; i<printCols.size(); i++){
                    fprintf(out, "%s ",header[printCols[i]].c_str());
                }
                fprintf(out, "\n");
            }

            for(int i = 0; i<rows; i++){
                for(int j = 0; j<printCols.size(); j++){
                    if(values[i][printCols[j]].type == NUM){
                        fprintf(out, "%.5g ",values[i][printCols[j]].val.num);
                    }
                    else{
                        fprintf(out, "%s ", values[i][printCols[j]].val.str->c_str());
                    }
                }
                fprintf(out, "\n");
            }
            fclose(out);
        }
};

int main(int argc, char* argv[]){
    if(argc < 5){
        cout << "OTHER ERROR\n";
        exit(-1);
    }
    bool header = strcmp(argv[1], "-header") == 0;
    string cmdString = string(argv[header + 1]);

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
    }
    return 0;
}
