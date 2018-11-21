function [im] = imReadAndConvert(filename, representation)

imgProp = imfinfo(filename);
type = imgProp.ColorType;
org = imread(filename);

if (strcmp(type,'truecolor')) 
    if (representation == 1)
        im = rgb2gray(double(org) ./ 255);
    elseif ( (representation == 2) || (representation == 1) )

        im = double(org) ./ 255;
    end

end    

end 

