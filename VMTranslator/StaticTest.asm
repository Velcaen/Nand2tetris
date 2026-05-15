//vm command:push constant 111@constant 111
D=A
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:push constant 333@constant 333
D=A
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:push constant 888@constant 888
D=A
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:pop static 8//get the top element of stack
@SP
M=M-1
A=M
D=M
@StaticTest.static 8
D=M

//vm command:pop static 3//get the top element of stack
@SP
M=M-1
A=M
D=M
@StaticTest.static 3
D=M

//vm command:pop static 1//get the top element of stack
@SP
M=M-1
A=M
D=M
@StaticTest.static 1
D=M

//vm command:push static 3@StaticTest.static 3
D=M
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:push static 1@StaticTest.static 1
D=M
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:sub
//get the top element of stack
@SP
M=M-1
A=M
D=M
// store the result temporarily
@R14
M=D
//get the top element of stack
@SP
M=M-1
A=M
D=M
// store the result temporarily
@R13
M=D
@R13
D=M
@R14
D=D-M
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//vm command:push static 8@StaticTest.static 8
D=M
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

//wm command:add
//get the top element of stack
@SP
M=M-1
A=M
D=M
// store the result temporarily
@R14
M=D
//get the top element of stack
@SP
M=M-1
A=M
D=M
// store the result temporarily
@R13
M=D
@R13
D=M
@R14
D=D+M
//push the value into stack
@SP
A=M
M=D
@SP
M=M+1

