function [D,H] = readsac(filename)
%READSEGY: Read SAC data
%
%   Reads data D and trace header from SAC file. 
%   The data and headers are extracted  in 
%   a range given by hw (header words).
%   
%   [D,H] = readsac(filename) returns the data D and
%
%   IN   filename: name of sac
%
%   OUT  D: the data (in a matrix) 
%        H: the header in a structure
%

FID = fopen(filename,'r','b');      % b means use sun byte order.
H = readstruct(FID, def_sac );
D = fread(FID, inf, 'float');
fclose(FID);
