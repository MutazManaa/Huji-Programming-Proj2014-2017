function [uname1,id1,uname2,id2] = myUname()
    % please update your user name and Id .
    uname1 = 'mutazmanaa';
    id1 = '200750453';  % fill your ID as a string
    uname2 = 'Kiss91';
    id2 = '305319501';   % fill only if handed in by two students.
    if (str2num(id2) >0)
        disp([ ' uName1: ' uname1 ' id: ' id1 ' uNmae2: ' uname2 ' id: ' id2]);
    else
         disp([ ' uName1: ' uname1 ' id: ' id1 ]);
    end