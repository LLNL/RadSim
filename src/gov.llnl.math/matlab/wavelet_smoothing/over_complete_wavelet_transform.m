%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function waveletCoeff = over_complete_wavelet_transform(x,h0,maxScale)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%
% Function Name:  over_complete_wavelet_transform.m
%
%
% Description: This function returns the wavelet series expansion of the input signal
%
% Input:
%       Star flux data with planetary transit signature buried in it
%       Daubechies 12 tap scaling filter coefficients (could be any other)
%       number of stages of filter banks
%       maxScale - number of scales or number of stages in the filter bank
%       in the wavelet series expansion
% Output:
%       Matrix of wavelet coefficients of size
%       (scales+1)x(length of signal)
%
%
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function waveletCoeff = over_complete_wavelet_transform(x,h0,maxScale)

nx = length(x);    % input data vector length, forced to be a power of 2

m = length(h0); % scaling function (low pass filter impulse response) coefficients length


% coefficients of the wavelet (high pass filter) obtained as
% (+/-)*((-1)^n)*h0(N-n) where N = length of h0
% (under orthogonality conditions for scaling and wavelet functions)

h1 = flipud(h0).*(-1).^(0:m-1)';

% Y = fft(X,n) returns the n-point DFT. If the length of X is less than n,
% X is padded with trailing zeros to length n. If the length of X is greater
% than n, the sequence X is truncated. When X is a matrix, the length of the
% columns are adjusted in the same manner

H0 = fft(h0,nx); % LPF (Filter bank terminology) Scaling coefficients at scale k /Wavelet terminology

H1 = fft(h1,nx); % HPF (Filter bank terminology) Wavelet expansion coefficients at scale k /Wavelet terminology

% higher scale wavelet components can be considered as details on a lower
% scale signal

% find out how many stages of filtering to do
% for any signal that is band limited, there will be an upper scale j = J,
% above which the wavelet coefficients are negligibly small
% - that is indicated by maxScale

waveletCoeff = zeros(nx,maxScale+1);


X = fft(x);


for j = 1:maxScale
    
    % wavelet expansion of signal at scale k
    % signal filtered by wavelet coefficients at scale k (HPF at scale k)
    waveletCoeff(:,j)= real(ifft( X.*H1 ));
    
    % shifting the wavelet coefficients to align in time
    % shift = (filter length - number of zeros trailing (same as the number
    % of zeros in between in between)
    
    nshift = m*2.^(j-1)- 2.^(j-1);
    waveletCoeff(:,j) = circshift(waveletCoeff(:,j),-nshift);
    
    
    % low pass filter the signal for the next iteration
    X = X.*H0;
    
    % MULTIRATE IDENTITIES:  Interchange of filtering and downsampling:
    % downsampling by N followed by filtering with H(z) is equivalent to
    % filtering with the upsampled filter(H(z^N)) before downsampling.
    % (upsampling a filter impulse response is equivalent to introducing
    % 2^k zeros between nonzero coefficients at scale k filter
    
    
    H0=[H0(1:2:end);H0(1:2:end)];
    
    % Upsampling shrinks the original spectrum and creates a compressed
    % image next to it
    
    H1 = [H1(1:2:end);H1(1:2:end)];
    
    
end

x = real(ifft(X)); % lowest resolution signal
waveletCoeff(:,maxScale+1) = x; % wavelet decomposition of the signal at (k+1)th scale
nshift = m*2.^(maxScale-1) -2.^(maxScale-1);
waveletCoeff(:,maxScale+1) = circshift(x, -nshift); % wavelet decomposition of the signal at (k+1)th scale


return
