/*
 * ParametersParser.h
 *
 *  Created on: Sep 6, 2015
 *      Author: mutazmanaa
 */

#ifndef PARAMETERSPARSER_H_
#define PARAMETERSPARSER_H_

#include <list>
#include <map>
#include <vector>
#include <string>
#include <sstream>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <string>
#include <cstdlib>
#include <cassert>
#include <cstdio>
#include <fstream>
#include <iostream>
#include <iterator>

/**
 * class treat parameter file and parse it
 */

class ParametersParser
{
public:
	/**
	 * c'stor
	 */
	ParametersParser();

	/**
	 * c'stor get only file name
	 */
	ParametersParser(std::string paramsFileName);

	/**
	 * c'stor get the private fields
	 */
	ParametersParser(std::string paramsFileName, std::map<std::string, int> paramMap,
					 std::vector<std::string> identifiersVector);

	/**
	 * d'tor
	 */
	~ParametersParser();

	/**
	 * getting the match score
	 */
	int getTagMatchScore();

	/**
	 * getting the LyricsMatchScore
	 */
	int getLyricsMatchScore();

	/**
	 * getting InstrumentMatchScore
	 */
	int getInstrumentMatchScore();

	/**
	 * getting BpmLikelihoodWeight
	 */
	int getBpmLikelihoodWeight();

	/**
	 * getting the ParamsFileName
	 */
	std::string getParamsFileName();

	/**
	 * getting the map that include params and his values
	 */
	std::map<std::string, int> getParametersMap();

	/**
	 * get identifiers as vector include identifierand  value as complete string
	 */
	std::vector<std::string> getIdentifiersVector();

	/**
	 * parsing the file
	 */
	void readParameterFile();



private:

	std::string _paramsFileName;
	std::map<std::string, int> _paramMap;
	std::vector<std::string> _identifiersVector;

};


#endif /* PARAMETERSPARSER_H_ */
