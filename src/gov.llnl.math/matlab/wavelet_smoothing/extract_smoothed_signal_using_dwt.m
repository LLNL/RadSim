function smoothedData = extract_smoothed_signal_using_dwt(noisyData,waveletScale)

waveletStr = 'sym8';

%waveletStr = 'db6';


if(~exist('waveletScale', 'var'))
    if(max(noisyData) > 2e2)
        waveletScale = 2;
    else
        waveletScale = 3;
    end
else
    
    if(waveletScale == 1)
        if(max(noisyData) > 5*std(noisyData) && max(noisyData) > 1e2) % demand high SNR
            waveletScale = 1;
        elseif(max(noisyData) > 2e2)
            waveletScale = 2;
        else
            waveletScale = 3;
        end
    end
end

%     if(waveletScale == 1)
%         if(max(noisyData) > 5*std(noisyData) && max(noisyData) > 1e2) % demand high SNR
%             waveletScale = 1;
%         else
%             waveletScale = 2;
%         end
%     end

%-------------------------------------------------------------------------------
% [C,L] = wavedec(X,N,'wname') returns the wavelet decomposition of the signal X at level N, using 'wname'. N must be a
% strictly positive integer (see wmaxlev for more information). The output decomposition structure contains the wavelet
% decomposition vector C and the bookkeeping vector L. The structure is organized as in this level-3 decomposition
% example.


[waveletC,bookKeepL] = wavedec(noisyData,waveletScale,waveletStr) ; % no need  for extending the signal so its length is a power of 2
Level = waveletScale;
smoothedData = wrcoef('a', waveletC,bookKeepL,waveletStr,Level);

return




%%

% L = length(noisyData);                     % Length of signal
% NFFT = 2^nextpow2(L); % Next power of 2 from length of y
% 
% if(NFFT > L)
%     % extend all signals in preparation for 1d DWT to the next power of 2
%     aExtended = wextend('1D','sym',noisyData,NFFT-L, 'r');
% else
%     aExtended = noisyData;
% end
% 
% [waveletC,bookKeepL] = wavedec(noisyData,waveletScale,waveletStr) ;
% Level = waveletScale;
% sourceSignal = wrcoef('a', waveletC,bookKeepL,waveletStr,Level);
% 
% [waveletDenoisedCoeff, bookKeepL2] = wavedec(sourceSignal,waveletScale,waveletStr) ;
% 
% 
% smoothedData = sourceSignal(1:L);
% %smoothedData(smoothedData<0) = 0;
% 
% 
% [XDEN,DENOISEDCFS,ORIGCFS] = wdenoise(noisyData,Level,'wavelet',waveletStr,'DenoisingMethod','BlockJS');

