function [D,H] = readsegy(filename)
%READSEGY: Read SEGY data
%
%   Reads data D and trace header from  SEGY file. 
%   The data and headers are extracted  in 
%   a range given by hw (header words).
%   
%   [D,H] = readsegy(filename) returns the data D and
%
%   IN   filename: name of segy
%
%   OUT  D: the data (in a matrix) 
%        H: the header in a structure
%

%  any resemblance to original readsegy is purely coincidental at this point.
FID = fopen(filename,'r','b');      % b means use sun byte order.
H = readstruct(FID, def_segy );
D = fread(FID, inf, 'short');
D = D*H.scale_fac;
fclose(FID);
