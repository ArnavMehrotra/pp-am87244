
#include <iostream>
#include <string.h>
#include <string>
#include <fstream>
#include <sstream>

using namespace std;

typedef enum TYPE{
    STR,
    NUM
}_type;

typedef union _value{
    string str;
    double num;
}Value;

class TableEntry{
    public:
        TYPE type;
        Value val;
};

class Table{
    private:
        TableEntry** values;
        string outFile;
        int rows;
        int cols;
        string* header;
    public:
        Table(string tableFile, string out, bool head){
            outFile = out;
            string buffer[256][256];

            ifstream t(tableFile);
            string fileString;
            getline(t, fileString);
            int r = 0;
            int c = 0;

            //read file into buffer
            while(!fileString.empty()){
                stringstream ss(fileString);
                string val;
                int last_c = c;
                c = 0;
                while(ss >> val){
                    buffer[r][c] = val; 
                    c++; //oops I did it again 
                }
                if((last_c != c) && (last_c != 0)){
                    cout << "NUM COLS ERROR\n";
                    exit(-1);
                }
                getline(t, fileString);
                r++;
            }

            if(head){
                header = (string*) calloc(c, sizeof(string));
                for(int i = 0; i<c; i++){
                    header[i] = buffer[0][i];
                }
            }
        
            values = (TableEntry**) calloc(r, sizeof(TableEntry*));
            for(int i = 0; i<r; i++){
                values[i] = (TableEntry*)calloc(c, sizeof(TableEntry));
            }

            for(int i=0; i<r-head; i++){
                for(int j=0; j<c; j++){
                    if((atoi(buffer[i+head][j].c_str()) == 0) && (buffer[i+head][j].c_str()[0] != '0')){
                        values[i][j].type = STR;
                        values[i][j].val.str = buffer[i+head][j];
                    }
                    else{
                        values[i][j].type = NUM;
                        values[i][j].val.num = atof(buffer[i+head][j].c_str());
                    }
                }
            }

            rows = r-head;
            cols = c;
        }

        void printCols(int n_cols, int printCols[]){
            for(int i = 0; i<n_cols; i++){
                if((printCols[i] >= cols) || printCols[i] < 0){
                    cout << "COL INDEX ERROR\n";
                }
            }

            if(header != NULL){
                for(int i = 0; i<n_cols; i++){
                    cout << header[i] << " ";
                }
                cout << "\n";
            }

            for(int i = 0; i<rows; i++){
                for(int j = 0; j<n_cols; j++){
                    if(values[i][printCols[j]].type == NUM){
                        printf("%.5g ",values[i][printCols[j]].val.num);
                    }
                    else{
                        cout << values[i][printCols[j]].val.str << " ";
                    }
                }
                cout << "\n";
            }
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
        int cols[256];
        int n_cols = 0;

        char* colNum = strtok(argv[header+2], ",");
        while(colNum != NULL){
            cols[n_cols] = atoi(colNum);
            n_cols++;
            if((cols[n_cols] == 0) && (strcmp(colNum, "0") != 0)){
                cout << "COL INDEX ERROR\n";
                exit(-1);
            }
        }



        Table* t = new Table(argv[header+3], argv[header+4], header);
    }
    return 0;
}
