/*
 * InstrumentalSong.cpp
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#include "InstrumentalSong.h"

using namespace std;

const string INSTRUMENTALS = "INSTINSTRUMENTALS";
const string SPACE = " ";
const string TABB = "\t ";

/**
 * c'stor with default pbm
 */
InstrumentalSong::InstrumentalSong() : _bpm(0)
{
};

/**
 *c'stor creat instremental song with params
 */
InstrumentalSong::InstrumentalSong(string title, map<string, int> mapSongTags,
								   string instruments, string performedBy, int bpm):
								   Song(title, mapSongTags)
{

	this->_instruments = instruments;
	this->_performedBy = performedBy;
	this->_bpm = bpm;

}

/**
 * d'stor
 */
InstrumentalSong::~InstrumentalSong()
{
}

/**
 * private help method calculated the match score
 * with the exponent
 *
 * */
int InstrumentalSong::_getMatchrateScore(string query, ParametersParser param)
{
	vector<string> identifiers = param.getIdentifiersVector();
	string temp;
	string identifier;
	double m, s;
	for (std::vector<string>::iterator it = identifiers.begin();
			it != identifiers.end(); ++it)
	{
		identifier = (*it).substr(0, query.length());
		if (query.compare(identifier.c_str()) == 0)
		{
			*it = (*it).erase(0, query.length() + 2);
			m = atof((*it).substr(0, (*it).find(SPACE)).c_str());
			(*it) = (*it).erase(0, (*it).find(SPACE) + 2);
			s = atof((*it).c_str());
			int pbm1 = param.getBpmLikelihoodWeight();
			int ppm = this->_bpm;
			int score;
			double exp1 = pow((ppm - m), 2);
			 exp1 = -(exp1 / (2 * pow(s, 2)));
			score = (int)floor(pow(exp(1), exp1) * pbm1);
			return score;

		}
		else
		{
			continue;
		}

	}

	return 0;
}

/**
 * get the final score of instremental song
 */

int InstrumentalSong::getScore(string query, ParametersParser param)
{
	vector<string> instrumentalVector;
	int bpmScore = 0;
	boost::split(instrumentalVector, this->_instruments,
			boost::is_any_of(TABB));
	for (std::vector<string>::iterator it = instrumentalVector.begin();
			it != instrumentalVector.end(); ++it)
	{
		if (query.compare((*it).c_str()) == 0)
		{

			bpmScore = param.getInstrumentMatchScore();
		}
	}
	int matchRate = 0;
	if (this->getBpm() > 0)
	{
		 matchRate = _getMatchrateScore(query, param);
	}


	return bpmScore + matchRate;
}

/**
 * GETTERS private methods
 */
string InstrumentalSong::getPerformedBy()
{
	return this->_performedBy;
}

string InstrumentalSong::getInstruments()
{
	return this->_instruments;
}

int InstrumentalSong::getBpm()
{
	return this->_bpm;
}

std::string InstrumentalSong::getSongType()
{
	return INSTRUMENTALS;

}

