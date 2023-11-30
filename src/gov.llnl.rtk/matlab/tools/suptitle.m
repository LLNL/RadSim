function [h, ax]=suptitle(text)
%
%  Centers a title over a group of subplots.
%  Returns a handle to the title and the handle to an axis.
%   [ax,h]=suptitle(text)
%           returns handles to both the axis and the title.
%   ax=suptitle(text)
%           returns a handle to the title only.

% Don't disturb the current axes
fig=gcf;
ca=[];
if ~isempty(findobj(fig,'type','axes'))
  ca=fig.CurrentAxes;
end

ax=findobj(gcf,'tag','suptitle');
position=[0 0.85 1 0.1];

if isempty(ax)
  % Create a hidden axis
  ax=axes('Units','Normal','Position',position,'Visible','off');
  h=get(ax,'Title');
  
  % Up the font size
  fs=get(h,'FontSize')+4;
  set(h,'FontSize',fs);
  set(h,'Visible','on')

  % Tag it for later
  set(ax,'tag','suptitle');
end

if isempty(text)
  delete(ax);
  return
end

% Place the super title
title(text);
h=get(ax,'Title');
set(fig,'Name',text);

% Restore axis
if ~isempty(ca)
  fig.CurrentAxes=ca;
end
