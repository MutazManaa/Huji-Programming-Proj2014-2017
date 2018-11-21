/*
 * LyricsSong.h
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#ifndef LYRICSSONG_H_
#define LYRICSSONG_H_

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

#include "Song.h"

const std::string LYRICSS = "LYRICSS";

/**
 * Lyrics song class with specification with this kind of songs
 */
class LyricsSong: public Song
{
public:
	/**
	 * ctor
	 */
	LyricsSong();

	/**
	 * ctor getting private params
	 */
	LyricsSong(std::string title, std::map<std::string, int> mapSongTags, std::string lyrics, std::string LyricsBy);

	/**
	 * d'tor
	 */
	~LyricsSong();

	/**
	 * get final score of Lyrics song
	 */
	int getScore(std::string query, ParametersParser param);

	/**
	 * get the lyrics of the song
	 */
	std::string getLyrics();

	/**
	 * get LyricsBy feild
	 */
	std::string getLyricsBy();

	/**
	 * get song type
	 */
	std::string getSongType();

private:
	std::string _lyrics;
	std::string _lyricsBy;


};

#endif /* LYRICSSONG_H_ */
