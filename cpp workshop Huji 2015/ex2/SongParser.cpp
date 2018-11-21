/*
 * SongParser.cpp
 *
 *  Created on: Sep 8, 2015
 *      Author: mutazmanaa
 */

#include "SongParser.h"
using namespace std;

const std::string SEPARATOR = "=";
const std::string END_OF_SONGS = "***";
const std::string TITLE = "title";
const std::string TAGS = "tags";
const std::string LYRICS = "lyrics";
const std::string LYRICS_BY = "lyricsBy";
const std::string INSTRUMENTS = "instruments";
const std::string PERFORMED_BY = "performedBy";
const std::string BPM = "bpm";

/**
 * c'stor
 */
SongParser::SongParser()
{
};

/**
 * c'stor get params to creat song parser
 */
SongParser::SongParser(string songsFileName, map<std::string, int> mapSongTags)
{
	this->_songsFileName = songsFileName;
	this->_mapSongTags = mapSongTags;
}

/**
 * d'stor
 */
SongParser::~SongParser()
{
}

/**
 * return all the line between {}
 */
std::string SongParser::_getWordList(const std::string line)
{

	size_t pos1 = line.find("{");
	size_t pos2 = line.find("}");

	return line.substr(pos1 + 1, pos2 - pos1 - 1);
}

/**
 * This can either be a basis for a member function of some class or static function or whatever...
 */
vector<Song*> SongParser::readSongsFromFile(string songsFileName)
{

	std::ifstream instream(songsFileName.c_str());
	if (!instream.is_open())
	{

		std::cerr << "Error! Can't open file: " << songsFileName << "."
				<< std::endl;
	}

	std::string line = "";
	vector<Song*> songsVector;
	int lastSong = 0;

	while (instream.good() && !lastSong)
	{

		if (line.compare(SEPARATOR) != 0)
		{
			getline(instream, line);
			// Expect a line of "="
			if (line.empty())
			{
				break;
			}

			if (END_OF_SONGS.compare(line) == 0)
			{

				lastSong = 1;
				break;
			}

		}

		getline(instream, line);
		if (line.empty())
		{
			break;
		}
		// Expect a line of "title: ..."
		size_t pos = TITLE.size() + 2;
		std::string title = line.substr(pos);

		getline(instream, line);
		// Expect a line of "tags: {...}"
		std::string tags = _getWordList(line);

		/**-------make map for tags------*/

		string key;
		int value;
		while (tags.length() > 1)
		{
			key = tags.substr(0, tags.find(" "));
			tags = tags.erase(0, tags.find(" ") + 1);
			if (tags.length() == 1)
			{
				value = atoi(tags.c_str());

			}
			else
			{

				value = atoi(tags.substr(0, tags.find(" ") + 1).c_str());
			}

			this->_mapSongTags.insert(std::pair<string, int>(key, value));

		}

		std::string lyrics = "";
		std::string lyricsBy = "";
		std::string instruments = "";
		std::string performedBy = "";
		std::string bpmStr = "";

		getline(instream, line);
//		if (line.empty())
//		{
//			break;
//		}
		// Expect either lyrics or instruments.
		if (line.substr(0, LYRICS.size()).compare(LYRICS) == 0)
		{
			// Then we have a lyric song

			// Lets get the lyrics:
			lyrics = _getWordList(line);

			// Lets get the lyricsBy:
			getline(instream, line);
//			if (line.empty())
//			{
//				break;
//			}
			pos = LYRICS_BY.size() + 2;
			lyricsBy = line.substr(pos);
			LyricsSong* lySong = new LyricsSong(title, this->_mapSongTags,
					lyrics, lyricsBy);
			songsVector.push_back(lySong);

		}

		else
		{
			// Then we have an instrumental song

			// Lets get the instruments:
			instruments = _getWordList(line);

			// Lets get the performedBy:
			getline(instream, line);
//			if (line.empty())
//			{
//				break;
//			}
			pos = PERFORMED_BY.size() + 2;
			performedBy = line.substr(pos);

			// Lets see if we have bpm:
			if (!instream.good())
			{
				break;
			}
			getline(instream, line);
//			if (line.empty()) {
//				break;
//			}
			if (END_OF_SONGS.compare(line) == 0)
			{
				lastSong = 1;
			}

			if (line.substr(0, BPM.size()).compare(BPM) == 0)
			{

				pos = BPM.size() + 2;
				bpmStr = line.substr(pos);
				InstrumentalSong* inSong = new InstrumentalSong(title,
						this->_mapSongTags, instruments, performedBy,
						atoi(bpmStr.c_str()));
				songsVector.push_back(inSong);

			}
			else
			{
				assert(
						(line.compare(SEPARATOR) == 0) || (line.compare(END_OF_SONGS) == 0));

				InstrumentalSong* inSong = new InstrumentalSong(title,
						this->_mapSongTags, instruments, performedBy, 0);
				songsVector.push_back(inSong);

			}

		}
		this->_mapSongTags.clear();

	}

	instream.close();


	return songsVector;
}

