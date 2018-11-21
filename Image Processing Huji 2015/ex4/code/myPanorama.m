%
% This script generates all of the provided example panoramas (once your 
% generatePanorama.m is ready and functional). Input images are read from 
% ../data/inp/mine and resulting panorams are then saved to 
% ../data/out/mine
%

warning('OFF','images:initSize:adjustingMag');
tic;
numFrames = 3;
inpPathFormat = '../data/inp/mine/garden%d.jpg';
 outPath = '../data/out/mine/garden.jpg';
renderAtFrame = ceil(numFrames/2);
generatePanorama(inpPathFormat,outPath,numFrames,renderAtFrame,true);
toc;
pause(2);
close all;
tic;
numFrames = 3;
inpPathFormat = '../data/inp/mine/cafe%d.jpg';
outPath = '../data/out/mine/cafe.jpg';
renderAtFrame = ceil(numFrames/2);
generatePanorama(inpPathFormat,outPath,numFrames,renderAtFrame,true);
toc;
pause(2);
close all;

warning('ON','images:initSize:adjustingMag');
