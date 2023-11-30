function ff=ar2ff(ar,N)

ff=fft(ar,N).^-1;
