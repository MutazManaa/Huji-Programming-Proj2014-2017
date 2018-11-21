/*
 * queriesParser.cpp
 *
 *  Created on: Sep 6, 2015
 *      Author: mutazmanaa
 */

#include "QueriesParser.h"

using namespace std;


/**
 * c'stor
 */
QueriesParser::QueriesParser()
{
};
/**
 * d'stor
 */
QueriesParser::~QueriesParser()
{

}

/**
 * parse the file and save the parameters in vector
*/
vector<string> QueriesParser::qursParser(string qursFileName)
{
    ifstream instream(qursFileName.c_str());
    if (!instream.is_open())
    {
       cerr << "Error! Can't open file: " << qursFileName << "." << endl;
    }

    string line  = "";
    vector<string> qurs;


    while(instream.good())
    {
        getline(instream, line);
        if(line.empty())
        {
        	break;
        }
        qurs.push_back(line);
    }
    return qurs;

}
