// Divides R13 by R14 and stores the value in R15
// it will be referred to as a/b in code

        @0
        D=A
        @R15
        M=D

        @13
        D=M
        @a
        M=D

        @14
        D=M
        @b
        M=D

        @1
        D=A
        @base
        M=D
        
        (LOOP)
        
        @R14
        D=M
        @a
        D=M-D
        @ENDLOOP
        D                       ;JLT

        @b
        D=M
        @a
        D=M-D
        @ELSE
        D                       ;JLT
        (IF)
        @b
        D=M
        @a
        M=M-D
        
        @base
        D=M
        @R15
        M=M+D

        @b
        M=M<<
        @base
        M=M<<
        
       
        @ENDIF
        0                       ;JMP
        (ELSE)
        
        @b
        M=M>>
        @base
        M=M>>
        
        (ENDIF)
        
        @LOOP
        0                       ;JMP

        (ENDLOOP)
        



        
