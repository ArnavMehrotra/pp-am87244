
#include <iostream>
#include <string.h>
#include <string>
#include <fstream>
#include <sstream>

typedef std::string String;

typedef enum TYPE{
    STR,
    NUM
}_type;

typedef union _value{
    char* str;
    double num;
}Value;

class TableEntry{
    TYPE type;
    Value val;
};

class Table{
    private:
        TableEntry** values;
        String outFile;
        int n_rows;
        int n_cols;
        String* header;
    public:
    
    Table(String tableFile, String out, bool head){
        outFile = out;
        std::ifstream t(tableFile);
        String fileString;
        std::getline(t, fileString);
        while(!fileString.empty()){
            std::cout << fileString << "\n";
            std::getline(t, fileString);
        }
    }

};

int main(int argc, char* argv[]){
    if(argc < 5){
        std::cout << "OTHER ERROR\n";
        exit(-1);
    }
    bool header = strcmp(argv[1], "-header") == 0;
    String cmdString = String(argv[header + 1]);

    if(cmdString.compare("-print") == 0){
        Table* t = new Table(argv[header+3], argv[header+4], header);
    }
    return 0;
}
