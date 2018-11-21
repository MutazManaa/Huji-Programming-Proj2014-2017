function [Group] = TagImgs(dataPath)
files = dir(dataPath);
Group = zeros(length(files)-2,1);
for i = 3:length(files)
    Group(i-2) = strcmp(files(i).name(4), 'P');
end

end