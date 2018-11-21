/*
 * Song.h
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#ifndef SONG_H_
#define SONG_H_

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
#include "ParametersParser.h"

/**
 * super class of songs include virual and protected fields
 * that every song have in common
 */
class Song
{
public:

	/**
	 * c'stor
	 */
	Song();

	/**
	 * c'stor get private fields
	 */
	Song(std::string title, std::map<std::string, int> mapSongTags);

	/**
	 * d'stor
	 */
	virtual ~Song();

	/**
	 * virtual function get the song score (to implemented in the subclasses)
	 */
	virtual int  getScore(std::string query, ParametersParser param) = 0;

	/**
	 * getting the type of the song
	 */
	virtual std::string getSongType() = 0;

	/**
	 * calculate the tag score
	 */
	int tagScore(std::string query);

	/**
	 * getting the song title
	 */
	std::string getTitle();

	/**
	 * getting the song tags and his value
	 */
	std::map<std::string, int> getMapSongTags();

	/**
	 * setting scor to the song
	 */
	void setScore(int score);

	/**
	 * getting the song score
	 */
	int getSongScore();


protected:
std::string _title;
std::map<std::string, int> _mapSongTags;
int _score ;


};

#endif /* SONG_H_ */
