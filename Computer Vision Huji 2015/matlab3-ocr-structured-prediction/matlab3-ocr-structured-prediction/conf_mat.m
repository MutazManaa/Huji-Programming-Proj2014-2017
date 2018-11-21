function [cmat] = conf_mat(Y, C, names)

if iscell(Y) Y = cell2mat(Y); end;
if iscell(C) C = cell2mat(C); end;

%to row vectors
if size(Y,1) > size(Y,2) Y = Y'; end;
if size(C,1) > size(C,2) C = C'; end;
assert(length(Y) == length(C), 'Y and C should have same length');

%labels census
if ~exist('names', 'var')
    names = unique([Y,C]);
end
U = length(names);

cmat = zeros(U);
for y = 1:U
    for c = 1:U
        cmat(y,c) = sum(Y==y & C==c);
    end
end

cmat = cmat ./ repmat(sum(cmat,2),1, size(cmat,2));

if nargout < 1
    imagesc(cmat, [0 1]);
    clear cmat;
    colorbar;
    axis square;
    set(gca, 'xtick',1:U, 'xticklabel',names, 'ytick',1:U, 'yticklabel', names);    
    xlabel classification;
    ylabel label;
    set(findall(gcf,'type','text'),'fontSize',15,'fontWeight','bold')
end