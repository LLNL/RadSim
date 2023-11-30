function ff=cc2ff(cc,N)

ff=fft(cc2ar(cc,N-1)).^-1;
