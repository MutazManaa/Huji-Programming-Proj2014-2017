/*
 * InstrumentalSong.h
 *
 *  Created on: Sep 7, 2015
 *      Author: mutazmanaa
 */

#ifndef INSTRUMENTALSONG_H_
#define INSTRUMENTALSONG_H_

#include <list>
#include <map>
#include <vector>
#include <string>
#include <sstream>
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

#include <cmath>




#include "Song.h"
/**
 * class of instremental song
 */
class InstrumentalSong: public Song
{
public:
	/**
	 * ctors
	 */
	InstrumentalSong();

	/**
	 * ctor get params
	 */
	InstrumentalSong(std::string title, std::map<std::string, int> mapSongTags, std::string instruments, std::string performedBy, int bpm);

	/**
	 * dtor
	 */
	~InstrumentalSong();

	/**
	 * getting score
	 */
	int getScore(std::string query, ParametersParser param);

	/**
	 * geeting performedBy
	 */
	std::string getPerformedBy();

	/**
	 * getting the instruments
	 */
	std::string getInstruments();

	/**
	 * getting the Bpm
	 */
	int getBpm();

	/**
	 * getting the song type
	 */
	std::string getSongType();

private:
    std::string _performedBy;
    std::string _instruments;
    int _bpm;

    int _getMatchrateScore(std::string query, ParametersParser param);
};

#endif /* INSTRUMENTALSONG_H_ */
