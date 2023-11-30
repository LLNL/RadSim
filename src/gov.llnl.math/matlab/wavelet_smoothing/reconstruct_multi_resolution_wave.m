%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% function [multiResSignal] = reconstruct_multi_resolution_wave(waveletCoeff,h0)
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
%
% Function Name:  reconstruct_multi_resolution_wave.m
%
%
% Description: This function reconstructs the multiresolution signal from
% the overcomplete wavelet series expansion of the signal. 
% This is practically the inverse overcomplete transform
%
% Input:
%       (1) waveletCoeff a matrix of size signal_length x scale containing the
%       overcomplete wavelet series coefficients
%       (2) ho - low pass analysis bank filter for the lowest scale
%
% Output:
%       multiResSignal - multi resolution signal in matrix form of size signal length
%       x scale
%
% Comments: Idea from 'Ripples in Mathematics - The Discrete Wavelet Transform'
% by A. Jensen and A.la Cour-Harbo, Springer-Veralg, 2001
%
%
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
function [multiResSignal] = reconstruct_multi_resolution_wave(waveletCoeff,h0)

% This code is easily understood if figure 5 from the Overcomplete Wavelet
% Transform prototype document is nearby for reference.

[nrows, ncols] = size(waveletCoeff);

multiResSignal = zeros(nrows, ncols);
y = waveletCoeff;

scale = ncols-1;

%n = nrows;

m = length(h0);
% high pass
h1 = flipud(h0).*(-1).^(0:m-1)';

rhlow = zeros(nrows,ncols);
rghigh = zeros(nrows,ncols);
filtlen = zeros(1,ncols-1);
filtlen(1) = m;


g0 = flipud(h0);
g1 = flipud(h1);

for ii = 1:scale
    L = filtlen(1)*2^(ii-1);
    % filters with holes or zeros
    % we can create these filters from the basic h0 = daubh0(12) by filling
    % 2^(j-1) zeros in between samples for each scale j. Here these filters
    % were obtained from OWT
    
    filter = reshape([g0';zeros(2^(ii-1)-1,m)], L, 1);
    rhlow(1:L,ii) =  filter;
    
    filter = reshape([g1';zeros(2^(ii-1)-1,m)], L, 1);
    rghigh(1:L,ii) =  filter;
    filtlen(ii) = L;
end

% treat scale+1 differently
% uses only low pass filters
t = y(:,scale+1);
% this was the shift introduced in over_complete_wavelet_transform.m to align
% the wavelet coefficients in time. Still can't understand why this has to
% be undone for perfect reconstruction!!!
nshift = filtlen(scale) - 2.^(scale-1);
t = circshift(t,nshift);

% for the last scale use the same filter length as the (last-1) scale
for ii = scale:-1:1
    
    L = filtlen(ii);
    % filters with holes or zeros
    % we can create these filters from the basic h0 = daubh0(12) by filling
    % 2^(j-1) zeros in between samples for each scale j. Here these filters
    % were obtained from OWT
    filter = rhlow(1:L,ii) ;
    t = circular_conv(t, filter,ii);% this is circular convolution followed by a shift
    
end

t = t .* 2^-scale; % for OWT scale each signal
multiResSignal(:,scale+1) = t ;

for jj = scale:-1:1
    for ii = jj:-1:1
        L = filtlen(ii);
        if(ii == jj) % beginning, copy wavelet coefficients array into t
            t = y(:,ii);
            nshift = filtlen(ii) - 2.^(ii-1);
            % this was the shift introduced in over_complete_wavelet_transform.m to align
            % the wavelet coefficients in time. Still can't understand why this has to
            % be undone for perfect reconstruction!!!
            t = circshift(t,nshift);
            filter = rghigh(1:L,ii);
        else
            filter = rhlow(1:L,ii);
        end
        t = circular_conv(t, filter,ii); % this is circular convolution followed by a shift
        
    end
    t = t .* 2^-jj; % scale the coefficients
    multiResSignal(:,jj) = t ;
end
return;


function ct = circular_conv(t,filter,iscale)

nlength = length(t);
T = fft(t,nlength);
H = fft(filter,nlength);
ct =  real(ifft(T.*H));
nshift = length(filter) - 2.^(iscale-1);
ct = circshift(ct, -nshift);
return
