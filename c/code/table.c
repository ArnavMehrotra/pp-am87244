#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "table.h"

#define false 0;
#define true 1;


Table* build_table(int header, char* table_file_string, char* out_file_string);
void free_table(Table* table);
int sum_col(int col, Table* table);

int main(int argc, char** argv){
	int header = false;
    if(!(strcmp(argv[1], "-header"))){
		header = true;
	}
	
	int cmd_index = header ? 2 : 1;
	if(!(strcmp(argv[cmd_index], "-sum"))){
		if((argv[1+cmd_index] == NULL) || (argv[2+cmd_index] == NULL) || (argv[3+cmd_index] == NULL)){
			printf("OTHER ERROR\n");
			exit(-1);
		}

		Table* table = build_table(header, argv[2+cmd_index], argv[3+cmd_index]);
		if(header){
			for(int i = 0; i<table->num_cols; i++){
				printf("%s ", table->header[i]);
			}
			printf("\n");
		}
		int col = atoi(argv[1+cmd_index]);
		if(col == 0 && strcmp(argv[1+cmd_index], "0")){
			printf("COL INDEX ERROR\n");
			exit(-1);
		}
		int sum = sum_col(col, table);
		free_table(table);
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

	char* line = NULL;
	ssize_t linelen;
	size_t line_cap = 0;

	int r = 0;
	int c = 0;
	char* buff[256][256];

	//make space for buffer strings
	for(int i = 0; i<256; i++){
		for(int j = 0; j<256; j++){
			buff[i][j] = calloc(32, sizeof(char));
		}
	}

	//read file into buff and determine size for calloc
	while((linelen = getline(&line, &line_cap, table_file)) != -1){
		char* tokens = strtok(line, " \n\t");	
		int last_c = c;
		c = 0;	
		while(tokens != NULL){
			strcpy(buff[r][c], tokens);
			tokens = strtok(NULL, " \n\t");
			c++; //hehe
		}
		if((c != last_c)&&(last_c != 0)){
			printf("NUM COLS ERROR");
			exit(-1);
		}
		r++;
	}

			
	table->values = calloc(r-header, sizeof(Table_Entry*));

	//read header if there is one
	if(header){
		table->header = calloc(r, sizeof(char*));
		for(int i = 0; i<c; i++){
			table->header[i] = calloc(strlen(buff[0][i])+1, sizeof(char));
			strcpy(table->header[i], buff[0][i]);
		}
	}

	//copy buffer into table
	for(int i = 0; i<r - header; i++){
		table->values[i] = calloc(c, sizeof(Table_Entry));	
		for(int j = 0; j<c; j++){
			if((atof(buff[i+header][j]) == 0) && (strcmp(buff[i+header][j], "0"))){
				table->values[i][j].type = STR;
				table->values[i][j].val.str = calloc(strlen(buff[i+header][j])+1, sizeof(char));
				strcpy(table->values[i][j].val.str, buff[i+header][j]);	
			}
			else{
				table->values[i][j].type = NUM;
				table->values[i][j].val.num = atof(buff[i+header][j]);
			}
		}
	}

	//free buffer strings
	for(int i = 0; i<256; i++){
		for(int j = 0; j<256; j++){
			free(buff[i][j]);
		}
	}

	
	table->num_cols = c;
	table->num_rows = r-header;
	table->outfile = out_file_string;
	return table;	
}

int sum_col(int col, Table* table){
	if(col >= table->num_cols || col < 0){
		printf("COL INDEX ERROR\n");
		exit(-1);
	}
	int sum = 0;
	for(int i = 0; i<table->num_rows; i++){
		if(table->values[i][col].type == STR){
			printf("TYPE ERROR\n");
			exit(-1);
		}
		sum += table->values[i][col].val.num;
	}
	return sum;
}

void free_table(Table* table){
	if(table->header != NULL){
		for(int i = 0; i<table->num_cols; i++){
			free(table->header[i]);
		}
	}
	free(table->header);
	for(int i = 0; i<table->num_rows; i++){
		for(int j = 0; j<table->num_cols; j++){
			if(table->values[i][j].type == STR){
				free(table->values[i][j].val.str);
			}
		}
		free(table->values[i]);
	}
	free(table->values);
	free(table);
}