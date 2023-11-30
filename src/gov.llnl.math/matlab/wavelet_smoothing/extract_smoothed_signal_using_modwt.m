function smoothedData = extract_smoothed_signal_using_modwt(noisyData,waveletScale)



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






m = 12; % using Daubechies 2*m tap scaling filter
h1 = daubh0(m/2); % 2*m coefficients
evenLength = true;
if(mod(length(noisyData),2))
    % make length of data an even  number
    noisyData = [noisyData; noisyData(end)];
    evenLength = false;
    
end

[waveletCoeff] = over_complete_wavelet_transform(noisyData,h1,waveletScale);
multiResSignal = reconstruct_multi_resolution_wave(waveletCoeff,h1);

if(~evenLength)
    % make length of data an even  number
    multiResSignal = multiResSignal(1:end-1,:);
end

smoothedData = multiResSignal(:,waveletScale+1);


return;

