
#include <iostream>
#include <string.h>
#include <string>
#include <fstream>
#include <sstream>

typedef std::string String;

class TableEntry{

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
        std::cout << fileString << "\n";
    }

};

int main(int argc, char* argv[]){
    if(argc < 4){
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
