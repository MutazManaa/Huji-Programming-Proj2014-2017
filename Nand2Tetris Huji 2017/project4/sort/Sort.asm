//sorts the values in R4 -> (R14+R15) in descending order

        @R14
        D=M
        @i
        M=D
        
        @j

        (OUTLOOP)

        @R14
        D=M
        @j
        M=D
        
        (INLOOP)
        @a
        @b
        
        @j
        A=M
        D=M
        @a
        M=D

        @j
        A=M
        A=A+1
        D=M
        @b
        M=D

        @a
        D=M
        @b
        D=D-M
        

        @NOSWAP
        D                       ;JGT

        @b
        D=M
        @j
        A=M
        M=D

        @a
        D=M
        @j
        A=M
        A=A+1
        M=D

        
        (NOSWAP)

        @j
        M=M+1

        
        @R15
        D=M
        @R14
        D=D+M
        @j
        D=D-M
        D=D-1
        
        @INLOOP
        D                       ;JNE




        
        @i
        M=M+1

        @R15
        D=M
        @R14
        D=D+M
        @i
        D=D-M

        @OUTLOOP
        D                       ;JNE
