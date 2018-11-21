/*
 * MIR.cpp
 *
 *  Created on: Sep 8, 2015
 *      Author: mutazmanaa
 */

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
#include <boost/algorithm/string.hpp>
#include <math.h>
#include "QueriesParser.h"
#include "ParametersParser.h"
#include "SongParser.h"
#include "InstrumentalSong.h"
#include "LyricsSong.h"

using namespace std;

#define NUM_OF_ARGS 4
const string USAGE_ERROR =
		"Usage: MIR < songs file name > < parameters file name > < queries file name >";
const string SONG_START = "----------------------------------------";
const string QUERY_WORD = "Query word: ";
const string LY_BY = "lyrics by: ";
const string PER_BY = "performed by: ";
const string TAB = "\t";

/**
 * finction print format of the two kinds of songs
 */
void printSong(Song* song, string sngType, int score)
{

	cout << song->getTitle() << TAB << score << TAB;
	if (sngType.compare(LYRICSS) == 0)
	{
		LyricsSong* lySng = (LyricsSong*) song;
		cout << LY_BY << lySng->getLyricsBy() << endl;

	}
	else
	{
		InstrumentalSong* insSng = (InstrumentalSong*) song;
		cout << PER_BY << insSng->getPerformedBy() << endl;

	}
}

int main(int argc, char *argv[])
{
	//check the number of arguments
	if (argc != NUM_OF_ARGS)
	{
		cout << USAGE_ERROR << endl;
		return 0;
	}

	string songFileName = argv[1];
	string paramFileName = argv[2];
	string queryFileName = argv[3];

	 //create parsers

	SongParser sngPrs;
	vector<Song*> sngsVec;

	ParametersParser* prmPrs = new ParametersParser(paramFileName);
	prmPrs->readParameterFile();

	QueriesParser qrsPrs;
	vector<string> qursPrm = qrsPrs.qursParser(queryFileName);

	int score;
	string songString;
	string query;


	vector<int> orderSngsVec;

	//set the score for the songs
	for (int i = 0; i < (int) qursPrm.size(); ++i)
	{
		sngsVec = sngPrs.readSongsFromFile(songFileName);
		cout << SONG_START << std::endl;
		cout << QUERY_WORD << qursPrm[i] << endl;
		cout << endl;

		for (int j = 0; j < (int) sngsVec.size(); ++j)
		{
			score = (sngsVec[j])->tagScore(qursPrm[i])
					* (*prmPrs).getTagMatchScore();
			score += (sngsVec[j])->getScore(qursPrm[i], *prmPrs);
			(sngsVec[j])->setScore(score);


		}

		//sort the song vector by score from the greates

		Song* temp;
		for (int j = 0; j < (int) sngsVec.size(); ++j)
		{
			for (int k = 1; k < (int) sngsVec.size(); ++k)
			{

				if ((sngsVec[k-1])->getSongScore() < (sngsVec[k])->getSongScore())
				{
					temp = sngsVec[k];
					sngsVec[k] = sngsVec[k - 1];
					sngsVec[k - 1] = temp;

				}

			}


		}



			// print the proper songs
			for (int j = 0; j < (int) sngsVec.size(); ++j)
			{
				if (sngsVec[j]->getSongScore() > 0)
				{
					printSong(sngsVec[j], sngsVec[j]->getSongType(),
							sngsVec[j]->getSongScore());
				}



			}
	}

	//free the allocations
	for (int j = 0; j < (int) sngsVec.size(); ++j)
	{
		delete sngsVec[j];
	}

	delete prmPrs;



	return 0;
}

