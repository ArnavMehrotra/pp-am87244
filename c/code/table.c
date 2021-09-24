#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "table.h"

#define false 0;
#define true 1;


Table* build_table(int header, char* table_file_string, char* out_file_string);
void free_table(Table* table);
void sum_col(int col, Table* table);
void print_cols(Table* table, int cols[], int n_cols);

int main(int argc, char** argv){
	int header = false;
    if(!(strcmp(argv[1], "-header"))){
		header = true;
	}
	
	int cmd_index = header ? 2 : 1;
	if(!(strcmp(argv[cmd_index], "-print"))){
		if((argv[1+cmd_index] == NULL) || (argv[2+cmd_index] == NULL) || (argv[3+cmd_index] == NULL)){
			printf("OTHER ERROR\n");
			exit(-1);
		}
		int cols[256];
		int col_nums = 0;
		char* col_string = strtok(argv[1+cmd_index], ",");
		while(col_string != NULL){
			int col = atoi(col_string);
			if(!col && strcmp(col_string, "0")){
				printf("COL INDEX ERROR\n");
				exit(-1);
			}
			cols[col_nums] = col;
			col_string = strtok(NULL, ",");
			col_nums++;
		}
		Table* table = build_table(header, argv[2+cmd_index], argv[3+cmd_index]);
		print_cols(table, cols, col_nums);	
		free_table(table);

	}
	else if(!(strcmp(argv[cmd_index], "-sum"))){
		if((argv[1+cmd_index] == NULL) || (argv[2+cmd_index] == NULL) || (argv[3+cmd_index] == NULL)){
			printf("OTHER ERROR\n");
			exit(-1);
		}

		int col = atoi(argv[1+cmd_index]);
		if(col == 0 && strcmp(argv[1+cmd_index], "0")){
			printf("COL INDEX ERROR\n");
			exit(-1);
		}

		Table* table = build_table(header, argv[2+cmd_index], argv[3+cmd_index]);
		sum_col(col, table);
		free_table(table);
	}
	

    return 0;
}

//builds a table
Table* build_table(int header, char* table_file_string, char* out_file_string){	
	Table* table = calloc(1, sizeof(Table));

	FILE* table_file = fopen(table_file_string, "r");
	
	if(!table_file){
		printf("OTHER ERROR\n");
		exit(-1);	
	}

	fseek(table_file, 0, SEEK_END);
	long file_length = ftell(table_file);
	fseek(table_file, 0, SEEK_SET);
	char* file_buffer = malloc(file_length);

	if(!file_buffer){
		printf("OTHER ERROR\n");
		exit(-1);
	}
	fread(file_buffer, 1, file_length, table_file);
	fclose(table_file);
	file_buffer[file_length-1] = '\0';
	printf("%s", file_buffer);
	char* buff[256][256];	

	//make space for buffer strings
	char* line_buff[256];
	int r = 0;
	char* help = strtok(file_buffer, "\n");
	while(help != NULL){
		line_buff[r] = help;
		printf("%s\n", help);
		r++;
		help = strtok(NULL, "\n");
	}

	int c = 0;
	for(int i = 0; i<r; i++){
		char* tok = strtok(line_buff[i], " \t");
		int last_c = c;
		c = 0;
		while(tok != NULL){
			buff[i][c] = tok;
			tok = strtok(NULL, " \t");
			c++;
		}
		if((c != last_c) && (last_c != 0)){
			printf("NUM COLS ERROR\n");
			exit(-1);
		}
	}
			
	table->values = calloc(r-header, sizeof(Table_Entry*));
	//read header if there is one
	if(header){
		table->header = calloc(c, sizeof(char*));
		for(int i = 0; i<c; i++){
			table->header[i] = calloc(strlen(buff[0][i])+1, sizeof(char));
			strcpy(table->header[i], buff[0][i]);
		}
	}
	
	//copy buffer into table
	for(int i = 0; i<r - header; i++){
		table->values[i] = calloc(c, sizeof(Table_Entry));	
		for(int j = 0; j<c; j++){
			if((atoi(buff[i+header][j]) == 0) && (strcmp(buff[i+header][j], "0"))){
				table->values[i][j].type = STR;
				table->values[i][j].val.str = calloc(strlen(buff[i+header][j])+1, sizeof(char));
				strcpy(table->values[i][j].val.str, buff[i+header][j]);	
				printf("str: %s\n", buff[i+header][j]);
			}
			else{
				table->values[i][j].type = NUM;
				table->values[i][j].val.num = atof(buff[i+header][j]);
				printf("num: %.5g\n", table->values[i][j].val.num);
			}
		}
	}
	
	table->num_cols = c;
	table->num_rows = r-header;
	table->outfile = out_file_string;
	return table;	
}

//sum values in a column
void sum_col(int col, Table* table){
	if(col >= table->num_cols || col < 0){
		printf("COL INDEX ERROR\n");
		exit(-1);
	}
	double sum = 0;
	for(int i = 0; i<table->num_rows; i++){
		if(table->values[i][col].type == STR){
			printf("TYPE ERROR\n");
			exit(-1);
		}
		sum += table->values[i][col].val.num;
	}

	//print sum to file
	FILE* outfile = fopen(table->outfile, "w");

	if(outfile == NULL){
		printf("OTHER ERROR\n");
	}
	if(table->header != NULL){
		fprintf(outfile, "%s\n", table->header[col]);
	}
	if(!(sum-(int)sum)){
		fprintf(outfile, "%d", (int)sum);
	}
	else{
		fprintf(outfile, "%.5g", sum);
	}
	fclose(outfile);
}


void print_cols(Table* table, int cols[], int n_cols){
	//open file
	FILE* outfile = fopen(table->outfile, "w");

	if(outfile == NULL){
		printf("OTHER ERROR\n");
	}
	
	//print header if there is one
	if(table->header != NULL){
		for(int i = 0; i<n_cols; i++){
			fprintf(outfile, "%s ", table->header[cols[i]]);
		}
		fprintf(outfile, "\n");
	}	

	//print cols
	for(int i = 0; i<table->num_rows; i++){
		for(int j=0; j<n_cols; j++){
			if(cols[j] >= table->num_cols || cols[j] < 0){
				printf("COL INDEX ERROR\n");
				exit(-1);
			}
			if(table->values[i][cols[j]].type == NUM){
				if(table->values[i][cols[j]].val.num - (int)table->values[i][cols[j]].val.num == 0){
					fprintf(outfile, "%d ", (int)table->values[i][cols[j]].val.num);
				}
				else{
					fprintf(outfile, "%.5g ", table->values[i][cols[j]].val.num);
				}
			}
			else{
				fprintf(outfile, "%s ", table->values[i][cols[j]].val.str);
			}
		}
		fprintf(outfile, "\n");
	}

	fclose(outfile);	
}

//frees all memory allocated in table struct
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
