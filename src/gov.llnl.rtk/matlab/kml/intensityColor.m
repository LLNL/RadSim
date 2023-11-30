%% intensityColor([1.0 0 0])

function style=intensityStyle(col);

style.alpha=128;
style.styles=64;
style.scale=max(0.1+0.9*[0:63]/63,0.25);
style.cmap= ones(64,1)*col;

