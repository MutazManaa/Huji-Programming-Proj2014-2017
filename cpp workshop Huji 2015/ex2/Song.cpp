/*
 * Song.cpp
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#include "Song.h"

using namespace std;
/**
 * c'stor
 */
Song::Song()
{
};
Song::Song(string title, map<std::string, int> mapSongTags)
{
	this->_title = title;
	this->_mapSongTags = mapSongTags;
	this->_score = 0;

}

/**
 * d'stor
 */
Song::~Song()
{
}

/**
 * return the tag score if the query exist
 */
int Song::tagScore(string query)
{
	string keyQuery;
	map<string, int>::iterator i;
	for (i = this->_mapSongTags.begin(); i != this->_mapSongTags.end(); ++i)
	{

		keyQuery = i->first;
		if (query.compare(keyQuery.c_str()) == 0)
		{
			return i->second;
		}
	}
	return 0;
}

/**
 * getters and setters of the private feilds
 */

std::string Song::getTitle()
{
	return this->_title;

}
std::map<std::string, int> Song::getMapSongTags()
{
	return this->_mapSongTags;

}

void Song::setScore(int score)
{
	this->_score = score;
}
int Song::getSongScore()
{
	return this->_score;
}


