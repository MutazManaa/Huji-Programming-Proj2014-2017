/*
 * MatrixExceptions.h
 *
 *  Created on: Sep 21, 2015
 *      Author: mutazmanaa
 */

#ifndef MATRIXEXCEPTIONS_H_
#define MATRIXEXCEPTIONS_H_

#include<string>

/**
**class exceptions
**/
class MatrixExceptions : public std::exception
{
public:
	/**
	*cstpr get error msg
	*/
	MatrixExceptions(std::string errorMsg)
	{
		this->_errorMsg = errorMsg;
	}
	
	/**
	*convert msg to char* and throw exception	
	*/
	virtual const char* what() const throw()
	{
		return this->_errorMsg.c_str();
	}




private:
	std::string _errorMsg;
};

#endif /* MATRIXEXCEPTIONS_H_ */
