function fftplot(X,N,sample_rate,varargin)
% fftplot(X,sample_rate,varargin)

unit='Hz';
lower_limit=0;
log_x=0;
filter_result=0;
filter_func=hamming(10);

if (isvector(X))
  X=X(:);
end

if (nargin<3 | isempty(sample_rate))
  sample_rate=2;
  unit='\times\pi rad';
end

if (nargin<2 | isempty(N))
  N=length(X);
end

i1=1;
while (i1<=length(varargin))
  if isnumeric(varargin{i1})
  else
    switch (lower(varargin{i1}))
      case {'l', 'log'}
        log_x=1;

      case {'f', 'filt', 'filter'}
        filter_result=1;
        if (i1<length(varargin) & isnumeric(varargin{i1+1}))
          filter_func=varargin{i1+1};
          if (~isvector(filter_func))
            filter_func=hanning(filter_func)/sum(hanning(filter_func));
            sum(filter_func)
          end
          i1=i1+1;
        else
          filter_func=hanning(10)/sum(hanning(10));
        end
    end
  end
  i1=i1+1;
end

h=fft(X,N)/N^0.5;
w=[0:N-1]/N*sample_rate;
if (filter_result)
  h2=filter(filter_func,1,abs([h;h;h]));
  h=h2(length(h)+(1:length(h)),:);
end
if (log_x==0)
  plot(w,20*log10(abs(h)));
else
  semilogx(w,20*log10(abs(h)));
  axis tight;
  lower_limit=1;
end
u=axis;
u(1)=lower_limit;
u(2)=sample_rate/2;
axis(u);
grid on;

xlabel(['Frequency (' unit ')'])
ylabel(['Magnitude (dB)'])
