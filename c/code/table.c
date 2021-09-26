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
void print_when(Table* table, char* cond, int operation);

int main(int argc, char** argv){
	//check for header
	int header = false;
    if(!(strcmp(argv[1], "-header"))){
		header = true;
	}
		
	int cmd_index = header ? 2 : 1;
	//if else ladder for the command
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
	else if(!(strcmp(argv[cmd_index], "-when"))){
		//check for args
		if((argv[1+cmd_index] == NULL) || (argv[2+cmd_index] == NULL) || (argv[3+cmd_index] == NULL)){
			printf("OTHER ERROR\n");
			exit(-1);
		}

		//make table and eval operator
		Table* table = build_table(header, argv[2+cmd_index], argv[3+cmd_index]);
		char* cond = argv[1+cmd_index];
		switch(cond[0]){
			case '<':{
				if(cond[1] == '>'){
					print_when(table, cond+2, NEQ);
				}
				else{
					print_when(table, cond+1, LT);
				}
				break;
			}
			case '>':{
				print_when(table, cond+1, GT);
				break;
			}
			case '=':{
				if(cond[1] != '='){
					printf("COND ERROR\n");
					exit(-1);
				}
				print_when(table, cond+2, EQ);
				break;
			}
			default:
				printf("COND ERROR\n");	
				exit(-1);
		}

	}
	else{
		//error if invalid command is input
		printf("OTHER ERROR\n");
		exit(-1);
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

	char* buff[256][256];	

	//make space for buffer strings
	char* line_buff[256];
	int r = 0;
	char* line_helper = strtok(file_buffer, "\n");
	while(line_helper != NULL){
		line_buff[r] = line_helper;
		r++;
		line_helper = strtok(NULL, "\n");
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
			}
			else{
				table->values[i][j].type = NUM;
				table->values[i][j].val.num = atof(buff[i+header][j]);
			}
		}
	}
	
	table->num_cols = c;
	table->num_rows = r-header;
	table->outfile = out_file_string;
	free(file_buffer);
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
	fprintf(outfile, "%.5g\n", sum);
	fclose(outfile);
}


void print_cols(Table* table, int cols[], int n_cols){
	//open file
	FILE* outfile = fopen(table->outfile, "w");

	if(outfile == NULL){
		printf("OTHER ERROR\n");
	}

	for(int j = 0; j<n_cols; j++){
		if((cols[j] >= table->num_cols) || cols[j] < 0){
			printf("COL INDEX ERROR\n");
			exit(-1);
		}
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
			if(table->values[i][cols[j]].type == NUM){
				fprintf(outfile, "%.5g ", table->values[i][cols[j]].val.num);
			}
			else{
				fprintf(outfile, "%s ", table->values[i][cols[j]].val.str);
			}
		}
		fprintf(outfile, "\n");
	}

	fclose(outfile);	
}

void print_rows(Table* table, int n_rows, int rows[]){
	FILE* outfile = fopen(table->outfile, "w");

	if(outfile == NULL){
		printf("OTHER ERROR\n");
		exit(-1);
	}

	if(table->header != NULL){
		for(int i = 0; i<table->num_cols; i++){
			fprintf(outfile, "%s ", table->header[i]);
		}
		fprintf(outfile, "\n");
	}

	for(int i = 0; i<n_rows; i++){
		for(int j = 0; j<table->num_cols; j++){
			if(table->values[rows[i]][j].type == NUM){
				fprintf(outfile, "%.5g ", table->values[rows[i]][j].val.num);
			}
			else{
				fprintf(outfile, "%s ", table->values[rows[i]][j].val.str);
			}
		}
		fprintf(outfile, "\n");	
	}
	fclose(outfile);
}

//print with condition
void print_when(Table* table, char* cond, int operation){
	char* op1 = strtok(cond, ",");
	char* op2 = strtok(NULL, ",");
	if(!op1 || !op2){
		printf("COND ERROR\n");
		exit(-1);
	}
	
    int n_rows = 0;
	int rows[table->num_rows];

	int op1_type;
	int col1;
	double op1_num;

	int op2_type;
	int col2;
	double op2_num;

	//form values from cond statement
	if(op1[0] == '$'){
		op1_type = COL;
		col1 = atoi(op1 + 1);
		if((col1 == 0) && (op1[1] != '0')){
			printf("COND ERROR\n");
			exit(-1);
		}
		else if(col1 >= table->num_cols || col1 < 0){
			printf("COND ERROR\n");
			exit(-1);
		}
	}
	else if(op1[0] == '@'){
		op1_type = COL;
		op1 = op1 + 1;		
		if(table->header == NULL){
			printf("COND ERROR\n");
			exit(-1);
		}
		for(int i = 0; i < table->num_cols; i++){
			if(strcmp(op1, table->header[i]) == 0){
				col1 = i;
				goto FOUNDCOL1;
			}
		}
		printf("COND ERROR\n");
		exit(-1);	
	}
	else{
		op1_num = atof(op1);
		if(((int) op1_num == 0) && op1[0] != '0'){
			op1_type = CONSTSTR;
		}
		else{
			op1_type = CONSTNUM;
		}
	}
	FOUNDCOL1:


	if(op2[0] == '$'){
		op2_type = COL;
		col2 = atoi(op2 + 1);
		if((col2 == 0) && (op2[1] != '0')){
			printf("COND ERROR\n");
			exit(-1);
		}
		else if(col2 >= table->num_cols || col2 < 0){
			printf("COND ERROR\n");
			exit(-1);
		}
	}
	else if(op2[0] == '@'){
		op2_type = COL;
		op2 = op2 + 1;		
		if(table->header == NULL){
			printf("COND ERROR\n");
			exit(-1);
		}
		for(int i = 0; i < table->num_cols; i++){
			if(strcmp(op2, table->header[i]) == 0){
				col2 = i;
				goto FOUNDCOL2;
			}
		}
		printf("COND ERROR\n");
		exit(-1);	
	}
	else{
		op2_num = atof(op2);
		if(((int) op2_num == 0) && op1[0] != '0'){
			op2_type = CONSTSTR;
		}
		else{
			op2_type = CONSTNUM;
		}
	}
	FOUNDCOL2:


	//execute commands in this big ugly switch statement
	switch(op1_type){
		case COL:{
			switch(op2_type){
				case COL:{
					for(int i = 0; i<table->num_rows; i++){
						switch(operation){
							case GT:{
								if(table->values[i][col1].type != table->values[i][col2].type){
									continue;
								}
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num > table->values[i][col2].val.num)){
									rows[n_rows] = i;
									n_rows++;
								}
								else if((table->values[i][col1].type == STR) && strcmp(table->values[i][col1].val.str, table->values[i][col2].val.str) > 0){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case LT:{
								if(table->values[i][col1].type != table->values[i][col2].type){
									continue;
								}
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num < table->values[i][col2].val.num)){
									rows[n_rows] = i;
									n_rows++;
								}
								else if((table->values[i][col1].type == STR) && strcmp(table->values[i][col1].val.str, table->values[i][col2].val.str) < 0){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case NEQ:{
								if(table->values[i][col1].type != table->values[i][col2].type){
									continue;
								}
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num != table->values[i][col2].val.num)){
									rows[n_rows] = i;
									n_rows++;
								}
								else if((table->values[i][col1].type == STR) && strcmp(table->values[i][col1].val.str, table->values[i][col2].val.str) != 0){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							default:{
								if(table->values[i][col1].type != table->values[i][col2].type){
									continue;
								}
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num == table->values[i][col2].val.num)){
									rows[n_rows] = i;
									n_rows++;
								}
								else if((table->values[i][col1].type == STR) && strcmp(table->values[i][col1].val.str, table->values[i][col2].val.str) == 0){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
						}
					}
					break;
				}
				case CONSTNUM:{
					for(int i = 0; i<table->num_rows; i++){
						switch(operation){
							case GT:{
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num > op2_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case LT:{
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num < op2_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case NEQ:{
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num != op2_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							default:{
								if((table->values[i][col1].type == NUM) && (table->values[i][col1].val.num == op2_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
						}
					}
					break;
				}
				case CONSTSTR:{
					for(int i = 0; i<table->num_cols; i++){
						switch(operation){
							case GT:{
								if((table->values[i][col1].type == STR) && (strcmp(table->values[i][col1].val.str, op2) > 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case LT:{
								if((table->values[i][col1].type == STR) && (strcmp(table->values[i][col1].val.str, op2) < 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case NEQ:{
								if((table->values[i][col1].type == STR) && (strcmp(table->values[i][col1].val.str, op2) != 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							default:{
								if((table->values[i][col1].type == STR) && (strcmp(table->values[i][col1].val.str, op2) == 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
						}
					}
					break;
				}
			}
			break;
		}
		case CONSTNUM:{
			switch(op2_type){
				case COL:{
					for(int i = 0; i<table->num_rows; i++){
						switch(operation){
							case GT:{
								if((table->values[i][col2].type == NUM) && (table->values[i][col2].val.num < op1_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case LT:{
								if((table->values[i][col2].type == NUM) && (table->values[i][col2].val.num > op1_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case NEQ:{
								if((table->values[i][col2].type == NUM) && (table->values[i][col2].val.num != op1_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							default:{
								if((table->values[i][col2].type == NUM) && (table->values[i][col2].val.num == op1_num)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
						}
					}
					break;
				}
				case CONSTNUM:{
					switch(operation){
						case GT:{
							if(op1_num > op2_num){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						case LT:{
							if(op1_num < op2_num){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						case NEQ:{
							if(op1_num != op2_num){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						default:{
							if(op1_num == op2_num){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
					}
					break;
				}
				default:
					break;
			}
			break;
		}
		case CONSTSTR:{
			switch(op2_type){
				case COL:{
					for(int i = 0; i<table->num_cols; i++){
						switch(operation){
							case GT:{
								if((table->values[i][col2].type == STR) && (strcmp(table->values[i][col2].val.str, op1) < 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case LT:{
								if((table->values[i][col2].type == STR) && (strcmp(table->values[i][col2].val.str, op1) > 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							case NEQ:{
								if((table->values[i][col2].type == STR) && (strcmp(table->values[i][col2].val.str, op1) != 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
							default:{
								if((table->values[i][col2].type == STR) && (strcmp(table->values[i][col2].val.str, op1) == 0)){
									rows[n_rows] = i;
									n_rows++;
								}
								break;
							}
						}
					}
					break;
				}
				case CONSTSTR:{
					switch(operation){
						case GT:{
							if(strcmp(op1, op2) > 0){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						case LT:{
							if(strcmp(op1, op2) < 0){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						case NEQ:{
							if(strcmp(op1, op2) != 0){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
						default:{
							if(strcmp(op1, op2) == 0){
								for(int i = 0; i<table->num_rows; i++){
									rows[i] = i;
								}
								n_rows = table->num_rows;
							}
							break;
						}
					}
					break;
				}
				default:{
					break;
				}
			}	
			break;
		}
		default:{
			printf("COND ERROR\n");
			break;
		}

	}
	print_rows(table, n_rows, rows);
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
