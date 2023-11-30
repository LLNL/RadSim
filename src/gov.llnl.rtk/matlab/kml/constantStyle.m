function style=constantStyle(scale);

style.alpha=128;  % set transparency 0-transparent 255=solid
style.styles=64;
style.scale=ones(style.styles)*scale;
style.cmap=jet;

