#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "table.h"

#define false 0;
#define true 1;

int main(int argc, char** argv) {
	int header = false;
    if(!(strcmp(argv[1], "-header"))){
		header = true;
	}
	
	int cmd_index = header ? 2 : 1;
	if(!(strcmp(argv[cmd_index], "-sum"))){
		if(argv[1+cmd_index] || argv[2+cmd_index] || argv[3+cmd_index]){
			printf("OTHER ERROR");
			exit(-1);
		}
		Table* table = build_table(header, argv[2+cmd_index], argv[3+cmd_index]);
	}

    return 0;
}

Table* build_table(int header, char* table_file_string, char* out_file_string){
	FILE* table_file = fopen(table_file_string, "r");
	if(!table_file){
		printf("OTHER ERROR");
		exit(-1);	
	}
	Table* table = calloc(1, sizeof(Table));

	return table;	
}

void free_table(Table* table){
	for(int i = 0; *table->values[i] != NULL; i++){
		for(int j = 0 ;**table->values != NULL; j++){
			free(table->values[i][j]);
		}
	}
}