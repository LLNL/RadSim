function ydir(arg1,arg2)
% YDIR control the y direction of an axis
%   YDIR flips the current axis.
%   YDIR NORMAL aligns the Y-axis with increasing up the screen.
%   YDIR REVERSE aligns the Y-axis with increasing up the screen.
%   YDIR(HANDLE,...) uses another graphics handle.

ca=gca;
opt='';
if (nargin<1)
  flipca(ca)
  return
end

if (ishandle(arg1))
  if (nargin<2)
    flipca(arg1)
    return
  else
    ca=arg1;
    opt=arg2;
  end
else
  opt=arg1;
end


switch lower(opt)
case {'normal','norm','n'}
  set(ca,'Ydir','normal');
case {'reverse','rev','r'}
  set(ca,'Ydir','reverse');
end

function flipca(ca)
  dir=get(ca,'Ydir');
  switch dir
  case 'normal'
    set(ca,'YDir','reverse');
  case 'reverse'
    set(ca,'YDir','normal');
  end
  return
