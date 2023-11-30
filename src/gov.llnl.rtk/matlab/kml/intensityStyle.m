function style=intensityStyle(al);

if nargin < 1
  al=255;
end

style.alpha=al;
style.styles=64;
style.scale=max(0.1+0.9*[0:63]/63,0.25);
style.cmap=jet;

