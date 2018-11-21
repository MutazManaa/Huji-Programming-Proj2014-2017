function [res] = renderPyramid(pyr,levels)
res = pyr{1,1};
tempResult = res;
rowSize = size(pyr{1,1},1);
k = 2;
while k < levels + 1 
    [rows,cols] = size(pyr{k,1});
    tempResult = [tempResult [pyr{k,1} ; zeros(rowSize - rows,cols)] ]; 
    k = k + 1;
end
res = tempResult;
end