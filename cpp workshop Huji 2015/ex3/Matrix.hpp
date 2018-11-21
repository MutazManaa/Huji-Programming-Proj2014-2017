
#include <iostream>
#include <string>
#include <vector>
#include <thread>
#include <pthread.h>
#include <iterator>
#include <algorithm>
#include <cstdlib>
#include <stdexcept>
#include "Complex.h"
#include "MatrixExceptions.h"




using namespace std;
const string TAB = "\t";
const string NEW_LINE = "\n";


const string SIZE_ERROR = "MATRIX WITH DIFFIRENT SIZES!";
const string MULTIPLY_ERROR = "CAN'T MULTIPLY WITH INPROPER SIZES!";
const string NOT_SQUARE_ERROR = "THE MATRIX IS NOT SQUAR!";
const string PARALLEL_MODE = "Generic Matrix mode changed to parallel mode.";
const string NON_PARALLEL_MODE = "Generic Matrix mode changed to non­parallel mode.";


static bool paraller;


template<typename T>

/**
 * Generic Matrix
 */

class Matrix
{
private:

    unsigned int _numOfRows;
    unsigned int _numOfCols;
    vector<T> _matrix;

    /**
     * function check if the dimissions of the matrix are the same
     */
    bool _sameSize(const Matrix<T> & a)
    {
    	if(a._numOfRows == this->_numOfRows && a._numOfCols == this->_numOfCols)
    	{
    		return true;
    	}

    	return false;
    }





public:


    /** default cstor construct Matrix 1*1 with zero values*/

    Matrix()
    {
    	this->_numOfRows = 1;
    	this->_numOfCols = 1;
    	this->_matrix.push_back(T(0));

    }

/**
 * cstor constuct zero element of matrix
 */
    Matrix(unsigned int rows, unsigned int cols)
    {
    	this->_numOfRows = rows;
    	this->_numOfCols = cols;


    	for(unsigned int i = 0; i < this->_numOfRows; i++)
    	{
    		for(unsigned int j = 0; j < this->_numOfCols; j++)
    		{
    			this->_matrix.push_back(T(0));
    		}
    	}
    }

    /**
     * copy cstor
     */

    Matrix(const Matrix& matrix)
    {
    	this->_numOfRows = matrix._numOfRows;
        this->_numOfCols = matrix._numOfCols;
    	for(unsigned int i = 0; i < this->_numOfRows; i++)
    	{
    		for(unsigned int j = 0; j < this->_numOfCols; j++)
    		{
    			this->_matrix[i * this->_numOfCols + j] = T(0);
    		}
    	}
    }


    /**
     * move cstor
     */

    Matrix(Matrix<T> && other)
    {
    	this->_numOfRows = other._numOfRows;
        this->_numOfCols = other._numOfCols;
    	for(unsigned int i = 0; i < this->_numOfRows; i++)
    	{
    		for(unsigned int j = 0; j < this->_numOfCols; j++)
    		{
    			this->_matrix.push_back(other._matrix[i * this->_numOfCols + j]);
    			other._matrix[i * this->_numOfCols + j] = T(0);
    		}
    	}

    	other._numOfRows = 0;
    	other._numOfCols = 0;
    	for(unsigned int i = 0; i < this->rows(); i++)
    	{
    		for(unsigned j = 0; j < this->cols(); j++)
    		{
    			other._matrix[i * this->cols() + j] = T(0);
    		}

    	}
    }

    /**
     * cstor with signature get row and cols,and values.
     */

    Matrix(unsigned int rows, unsigned int cols, const vector<T>& cells)
    {
    	this->_numOfRows = rows;
        this->_numOfCols = cols;


        int k = 0;

    	for(unsigned int i = 0; i < this->_numOfRows; i++)
    	{
    		for(unsigned int j = 0; j < this->_numOfCols; j++)
    		{
    			this->_matrix.push_back(cells[k++]);

    		}
    	}


    }

    /**
     *dstrtor
     */
    ~Matrix()
    {

    }

    /**
     *implement operator = between 2 Matrices.
     */

    Matrix<T> & operator = (const Matrix<T> & other)
    {
    	if(this != &other)
    	{

    		this->_numOfRows = other._numOfRows;
    		this->_numOfCols = other._numOfCols;
    		this->_matrix = other._matrix;

    	}

    	return *this;
    }


    /**
     *function for sum 2 rows of matices to help in parallel Add
     */

    static void parallerAdd(Matrix<T> des, Matrix<T> mat1, Matrix<T>& mat2, unsigned int i)
    {
    	for(unsigned int j = 0; j < mat1.cols(); j++)
    	{
    		des._matrix[i * mat1.cols() + j] = mat1._matrix[i * mat1.cols() + j] + mat2._matrix[i * mat2.cols() + j];
    	}
    }
    /**
     *
     *implement operator add in 2 matrices in regular for or parallel form
     *
     */

    Matrix<T>  operator + (const Matrix<T> mat)
    {

   	 if(!this->_sameSize(mat))
   	 {
   		 throw MatrixExceptions(SIZE_ERROR);
   	 }
   	Matrix<T> result = Matrix(this->rows(), this->cols());

   	if(paraller)
   	{
		vector<thread> threadsVector;

   		for(unsigned int i = 0; i < this->cols(); i++)
   		{
   			threadsVector.push_back(thread(&Matrix::parallerAdd, ref(result), ref(*this), ref(mat), i));
   		    
   		}

		for(unsigned i = 0; i < threadsVector.size(); i++)
		{
			threadsVector[i].join();
		}
   		        return result;


   	}
   	else
   	{
   		for(unsigned int i = 0; i < this->rows(); i++)
   	    {
   			for(unsigned int j = 0; j < this->cols(); j++)
   			{
   				result._matrix[i * this->cols() + j] = this->_matrix[i * this->cols() + j] + mat._matrix[i * mat.cols() + j];

   			}
   	    }

	

   	    return result;

   	}


    }

    /**
     * implement operator substract between 2 matrices.
     */

     Matrix<T> & operator - (const Matrix<T> & mat)
     {
    	 if(!this->_sameSize(mat))
    	 {
    		 throw MatrixExceptions(SIZE_ERROR);
    	 }

    	 Matrix<T> & result = Matrix(mat.rows(), mat.cols());
    	 for(unsigned int i = 0; i < mat.rows(); i++)
    	 {
		 for(unsigned int j = 0; j < mat.cols(); j++)
    	         {
		 	 result._matrix.push_back(this->_matrix[i * this->_numOfCols + j] - mat._matrix[i * this->_numOfCols + j]);
    	         }
   	 }
    	        return result;
     }

     /**
      *
      *implement parallel maultibly for row * col
      */

     static void parallerMult(Matrix<T> & dest, Matrix<T>& mat1, Matrix<T>& mat2, unsigned int colum, unsigned int row)
     {
    	 T sum = T(0);
    	 for (unsigned int i = 0; i < mat2.rows(); i++)
    	 {
    		 sum += mat1._matrix[row * mat1.cols() + i] + mat2._matrix[i * mat2.cols() + colum];
    	 }
    	        dest._matrix[row * dest.cols() + colum] = sum;
     }




     /**
      * implement operator multibly between two matrices in regular ana paraller way
      */

     Matrix<T> operator * (const Matrix<T> mat)

	{
    	 if(this->cols() != mat.rows())
    	 {
    		 throw MatrixExceptions(MULTIPLY_ERROR);
    	 }

    	 Matrix<T> result = Matrix(this->cols(), mat.rows());
	vector<thread> threadVector;
    	 if(paraller)
    	 {

    	        for (unsigned int i = 0; i < mat.rows(); i++)
    	        {
    	            for (unsigned int j = 0; j != mat.cols(); j++)
    	            {
   	                threadVector.push_back(thread(&Matrix::parallerMult, ref(result), ref(*this), ref(mat), j, i));
    	                
    	            }
    	        }

		for(unsigned i = 0; i < threadVector.size(); i++)
		{
			threadVector[i].join();
		}
    	        return result;
    	 }

    	 else
    	 {

    	        for (unsigned int i = 0; i < mat.rows(); i++)
    	        {
    	            for (unsigned int j = 0; j != mat.cols(); j++)
    	            {
    	                T sum = T(0);
    	                for (unsigned int k = 0; k < mat.rows(); k++)
    	                {
    	                    sum += this->_matrix[i * this->cols() + k] + mat._matrix[k * mat.cols() + j];
    	                }
    	                result._matrix[i * result.cols() + j] = sum;
    	            }
    	        }
    	        return result;

    	 }

	}

     /**
      *
      * implement operator comparession between 2 matrices,return true iff equal
      */

     bool operator == (const Matrix<T> & mat)
		{
    	 if(!this->_sameSize(mat))
    	 {
    		 return false;
    	 }

    	 for(unsigned int i = 0; i < mat.rows(); i++)
    	 {
    	    for(unsigned int j = 0; j < mat.cols(); j++)
    	    {
    	    		if(this->_matrix[i * this->_numOfCols + j] != mat._matrix[i * this->_numOfCols + j])
    	    		{
    	    			return false;
    	    		}

    	    }
    	 }

    	 return true;

		}

     /**
      *
      *implement operator comparession between 2 matrices , return true iff not equal
      */

     bool operator != (const Matrix<T> & mat)
		{
    	 return !(this == mat);


		}


     /**
      *
      *implement transpose operator of matrix
      */

     Matrix<T> trans() const
     {
         Matrix<T> regMatrix(this->cols(), this->rows());
         for(unsigned int i = 0; i < regMatrix.rows(); i++)
         {
             for(unsigned int j = 0; j < regMatrix.cols(); j++)
             {
                 regMatrix._matrix.push_back(this->_matrix[j * this->rows() + i]);
             }
         }
         return regMatrix;
     }



     /**
      *
      * calculate the trace of one matrix.
      */

     T trace()
     {
         if(this->cols() != this->rows())
         {
             throw MatrixExceptions(NOT_SQUARE_ERROR);
         }
         T trace(0);
         for(unsigned int i = 0; i < this->rows(); i++)
         {
             trace += this->_matrix[i * this->cols() + i];
         }
         return trace;

     }

     /**
      *
      * implement print << operator for a matrix
      */

     friend ostream& operator<<(ostream &os, const Matrix<T> & mat)
     {
         for (unsigned int i = 0; i < mat.rows(); i++)
         {
             for (unsigned int j = 0; j < mat.cols(); j++)
             {
                 os << mat._matrix[i * mat.cols() + j];
                 if (j != mat.cols() - 1)
                 {
                     os << TAB;
                 }
                 else
                 {
                     os << NEW_LINE;
                 }
             }
         }
         return os;

     }

     /**
      *
      *implement operator index in matrix, return the value in matrix(i,j)
      */
     T operator () (unsigned int row, unsigned int col)
     {
    	 return this->_matrix[row * this->cols() + col];
     }

     /**
      *implement operator index in matrix, return the value in matrix(i,j).
      */

     T& operator () (unsigned int row, unsigned int col) const
     {
    	 return this->_matrix[row * this->cols() + col];

     }



	/**
	 * return true if the function is square
	 */
     bool isSquareMatrix()
     {
     	return this->rows() == this->cols();
     }


     typedef typename vector<T> :: const_iterator const_iterator;
     typedef typename vector<T> :: iterator iterator;



	/**
	 * return iterator begin of a vector
	 */
   iterator begin()
    {

    	return this->_matrix.begin();
    }

    /**
     *return iterator begin of a vector.
     */
    const_iterator begin() const
    {

    	return this->_matrix.begin();
    }

	 /**
	  *return iterator end of a vector.
	  */

    const_iterator end() const
    {

    	return this->_matrix.end();
    }

    /**
     *
     *return iterator end of a vector
     */

    iterator end()
    {

    	return this->_matrix.end();
    }
    /** return number of row
    */
    unsigned int rows() const
    {
    	return this->_numOfRows;
    }

    /**
    * set number of matrix rows
    */

    void setNumOfRows(unsigned int numOfRows)
    {
    	this->_numOfRows = numOfRows;
    }

    /**
     * return number of matrix colums
    */

    unsigned int cols() const
     {
     	return this->_numOfCols;
     }

    /**
     * set number of colums
     */

    void setNumOfCols(unsigned numOfCols)
    {
    	this->_numOfCols = numOfCols;
    }

    /**
     * return true iff paraller moode
     */

    bool getParaller() const
    {
    	return paraller;
    }

    /**
     * set paraller mode
     */

    void setParallel(bool parall)
    {
            if (parall == false)
            {
               cout << NON_PARALLEL_MODE << endl;
            }
            else
            {
                
			cout << PARALLEL_MODE << endl;		
            }
    paraller = parall;
    }


};



template<>

	/**specialization method for coplex type that implement tran of  
	*complex matrix
 
	*/
Matrix<Complex>  Matrix<Complex>::trans() const
{
	Matrix<Complex> cmpMatrix(cols(), rows());

	for (unsigned int i = 0; i < rows(); i++)
	{

		for (unsigned int j = 0; j < cols(); j++)
		{
			cmpMatrix._matrix[i * cmpMatrix.cols() + j] = this->_matrix[j * cmpMatrix.rows() + i].conj();
		}
	}

	return cmpMatrix;
}






