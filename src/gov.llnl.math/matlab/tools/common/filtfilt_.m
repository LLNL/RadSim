function [ X2 ] = filtfilt( B, A, X, dim )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here

X1=filter(B,A,X,[],dim);
if dim==1
   X1=flipud(X1); 
else
   X1=fliplr(X1);
end

X2=filter(B,A,X1,[],dim);

if dim==1
   X2=flipud(X2); 
else
   X2=fliplr(X2);
end

end

