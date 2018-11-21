#################################################################
#
#				Warning!!!!
#				============
#	
# this make file compiles all the sources with implicit rules
# therefore in order to make sure all the changes were made 
# run it with "make clean all"
#
##################################################################

EXEC = 

# list of all the sources files
SRCS	=  	ex0.cpp		\
		Model.cpp 	\
		Sphere.cpp \
		ShaderIO.cpp	\
	    bimage.cpp     \
#OpenMesh definitions
CG_HOME = /cs/course/2013/cg
OM_DIR = $(CG_HOME)/OpenMesh
OM_LIBS = -L$(OM_DIR)/lib -lOpenMeshCore
OM_INCLUDE=  -I$(OM_DIR)/include
		
# compiler 
CC	= g++ $(OM_INCLUDE) -D_GLIBCXX_USE_CXX11_ABI=0 

INCLUDE = -I/usr/include -I/usr/X11R6/include -I/cs/course/current/cg/include

OBJS = $(subst .cpp,.o,$(SRCS)) 		#substituting .cc -> .o

# in order to run in debug mode use the command "make DBG=1"
ifdef DBG

	CFLAGS = 	\
		-Wall 	\
		-Werror \
		-DDEBUG	\
		-O0		\
		-g3		\
		-Wno-deprecated
					
						
else

	CFLAGS = 		\
		-Wall		\
		-DNDEBUG    \
		-O2
				 
endif


# linker
LD	= $(CC) $(OM_LIBS)
LDFLAGS	=


# the name of the lib to create 
APP = ex2

# each lib should add with -l flag
LIBS =  -L/usr/lib -lm -lGLEW -lglut -lGL -lGLU -ldl -L/usr/X11R6/lib


# rule to change the suffix of the sources files
OBJS	= ${SRCS:.cpp=.o}

# clear out all suffixes
.SUFFIXES:
# list only those we use
.SUFFIXES: .o .cpp

# define a suffix rule for .c -> .o
.cpp.o :
	$(CC) $(CFLAGS) $(INCLUDE) -c $<

##################################
# 	default target 		##
##################################
all : $(APP)

$(APP) : $(OBJS)
	$(LD)  $(OBJS) $(LIBS) -o $(APP)

$(OBJS) : $(SRCS)


tar:
	tar cvf $(EXEC).tar Makefile $(SRCS) $(HDRS) README
	

.PHONY:
clean :
	rm -f $(OBJS) $(EXEC) *~ $(APP)
