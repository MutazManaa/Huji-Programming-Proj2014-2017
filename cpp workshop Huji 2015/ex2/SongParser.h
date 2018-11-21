/*
 * SongParser.h
 *
 *  Created on: Sep 8, 2015
 *      Author: mutazmanaa
 */

#ifndef SONGPARSER_H_
#define SONGPARSER_H_

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
#include "InstrumentalSong.h"
#include "LyricsSong.h"


/**
 *class parse the song file
 */
class SongParser
{
public:
	/**
	 * c'stor
	 */
	SongParser();

	/**
	 * c'stor get private feilds
	 */
	SongParser(std::string songsFileName, std::map<std::string, int> mapSongTags);

	/**
	 * d'stor
	 */
	~SongParser();

	/**
	 * parse the song from the file
	 */
	std::vector<Song*> readSongsFromFile(std::string songsFileName);

private:
	std::string _songsFileName;
	std::map<std::string, int> _mapSongTags;
	std::string _getWordList(const std::string line);

};

#endif /* SONGPARSER_H_ */
