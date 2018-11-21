function H = HOG(I, cell_x, cell_y, B)
[m,n] = size(I);
hx = [-1,0,1];
dx = conv2(I,hx,'same');
dy = conv2(I,hx','same');
%magnitudes = sqrt(dx.^2 + dy.^2);
%angles = atan(dy ./dx);
%angles(isnan(angles) == 1) = 0;
[angles, magnitudes] = cart2pol(dx,dy);
H = cell((floor(m/cell_y)-1),(floor(n/cell_x)-1));
for i=1:(floor(m/cell_y)-1)
    for j=1:(floor(n/cell_x)-1)
        collectedMagnitudes = magnitudes(((i-1)*cell_y+1):(i+1)*cell_y,((j-1)*cell_x+1):(j+1)*cell_x);
        collectedAngels = angles(((i-1)*cell_y+1):(i+1)*cell_y,((j-1)*cell_x+1):(j+1)*cell_x);
        
        Belements = zeros(B,1);
        for k = 1:B
            result = (collectedAngels >= -pi+ 2*pi*(k-1)/B) & (collectedAngels < (-pi+2*pi*k/B));
            Belements(k) = sum(collectedMagnitudes(result)); 
        end
        
    
        
        if (norm( Belements) ~= 0 )
             Belements =  Belements ./ norm( Belements);
         
        end
        H{i,j} =  Belements;
    end
end

end