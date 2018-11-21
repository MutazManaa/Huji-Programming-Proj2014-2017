/*
 * parametersParser.cpp
 *
 *  Created on: Sep 6, 2015
 *      Author: mutazmanaa
 */

#include "ParametersParser.h"

using namespace std;

const string TAGS_MATCH_SCORE = "tagMatchScore: ";
const string LYRICS_MATCH_SCORE = "lyricsMatchScore: ";
const string INSTRUMENTS_MATCH_SCORE = "instrumentMatchScore: ";
const string BPM_LIKELIHOOD_WEIGHT = "bpmLikelihoodWeight: ";

/**
 * c'stor
 */
ParametersParser::ParametersParser()
{
};

/**
 * c'stor get param file name to creat  param parser
 */
ParametersParser::ParametersParser(string paramsFileName)
{
	this->_paramsFileName = paramsFileName;

}
/**
 * c'stor get params to creat param parser
 */
ParametersParser::ParametersParser(string paramsFileName,
								   map<string, int> paramMap, vector<string> identifiersVector)
{

	this->_paramsFileName = paramsFileName;
	this->_paramMap = paramMap;
	this->_identifiersVector = identifiersVector;

}

/**
 * d'stor
 */
ParametersParser::~ParametersParser()
{

}

/**
 * parsing the file and put the information in STL's
 */
void ParametersParser::readParameterFile()
{
	ifstream instream(this->_paramsFileName.c_str());
	if (!instream.is_open())
	{
		std::cerr << "Error! Can't open file: " << this->_paramsFileName << "." << endl;
	}

	std::string line;
	string key;
	int value;
	int i = 0;
	vector<string> params;
	params.push_back(TAGS_MATCH_SCORE);
	params.push_back(LYRICS_MATCH_SCORE);
	params.push_back(INSTRUMENTS_MATCH_SCORE);
	params.push_back(BPM_LIKELIHOOD_WEIGHT);

	while (instream.good())
	{
		getline(instream, line);
//		if (line.empty())
//		{
//			break;
//		}
		if (i < 4)
		{
			key = line.substr(0, params[i].size());
			line = line.erase(0, params[i].size());
			value = atoi(line.c_str());
			_paramMap.insert(pair<string, int>(key, value));

		}
		else
		{
			this->_identifiersVector.push_back(line);
		}
		i++;
	}
}

/**
 * getters for the private feilds
 */
std::string ParametersParser::getParamsFileName()
{

	return this->_paramsFileName;
}
map<string, int> ParametersParser::getParametersMap()
{
	return this->_paramMap;
}

vector<string> ParametersParser::getIdentifiersVector()
{
	return this->_identifiersVector;
}

int ParametersParser::getTagMatchScore()
{
	return this->_paramMap[TAGS_MATCH_SCORE];
}

int ParametersParser::getLyricsMatchScore()
{
	return this->_paramMap[LYRICS_MATCH_SCORE];
}

int ParametersParser::getInstrumentMatchScore()
{
	return this->_paramMap[INSTRUMENTS_MATCH_SCORE];
}

int ParametersParser::getBpmLikelihoodWeight()
{
	return this->_paramMap[BPM_LIKELIHOOD_WEIGHT];
}

