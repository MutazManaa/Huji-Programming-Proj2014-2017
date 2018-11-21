// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

        

        @color
        @i

        (START)
        
        @24576
        D=M
        @WHITE
        D                       ;JEQ
        
        
        (BLACK)
        @color
        M=-1

        @DO
        0                       ;JMP
        
        (WHITE)
        @color
        M=0
        


        (DO)
        
        @16384
        D=A
        @i
        M=D


        (LOOP)
        @color
        D=M
        
        @i
        A=M
        M=D

        @i
        M=M+1
        

        D=M
        @24576
        D=A-D

        
        @LOOP
        D                       ;JNE

        @START
        0                       ;JMP
        
