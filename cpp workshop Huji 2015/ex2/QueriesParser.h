/*
 * QueriesParser.h
 *
 *  Created on: Sep 6, 2015
 *      Author: mutazmanaa
 */

#ifndef QUERIESPARSER_H_
#define QUERIESPARSER_H_

#include <fstream>
#include <iostream>
#include <string>
#include <vector>
#include <stdio.h>
#include <stdlib.h>

/**
 * quiries class deal with the quieries un the queiry file
 */
class QueriesParser
{
public:

	/**
	 * c'stor
	 */
	QueriesParser();

	/**
	 * d'stor
	 */
	~QueriesParser();

	/**
	 * parse the file and save the parameters in vector
	 */
	std::vector<std::string> qursParser(std::string qursFileName);
};
#endif

