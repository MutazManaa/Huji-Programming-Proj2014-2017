/*
 * LyricsSong.cpp
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#include "LyricsSong.h"

using namespace std;


const string LYRICSSS = "LYRICSS";
const string TABBB = "\t ";

/**
 * c'stor
 */
LyricsSong::LyricsSong()
{

};
/**
 * c'stor creat Lyrics Song with private param
 */
LyricsSong::LyricsSong(string title, map<std::string, int> mapSongTags,
					   string lyrics, string lyricsBy):Song(title, mapSongTags)
{
	this->_lyrics = lyrics;
	this->_lyricsBy = lyricsBy;


}

/**
 * d'stor
 */
LyricsSong::~LyricsSong()
{
}

/**
 * calculate the score of the lyrics song
 */

int LyricsSong::getScore(string query, ParametersParser param)
{
	int counter = 0;
	vector<string> lyricsVector;

	boost::split(lyricsVector, this->_lyrics, boost::is_any_of(TABBB));
	for (std::vector<string>::iterator it = lyricsVector.begin();
			it != lyricsVector.end(); ++it)
	{
		if ((*it).compare(query) == 0)
		{
			counter++;
		}
	}

	return (int) (counter * param.getLyricsMatchScore());

}

/**
 * getter of private feilds
 */
string LyricsSong::getLyrics()
{
	return this->_lyrics;
}

string LyricsSong::getLyricsBy()
{
	return this->_lyricsBy;
}

std::string LyricsSong::getSongType()
{
	return LYRICSSS;

}


