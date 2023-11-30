function xdir(arg1,arg2)
% XDIR control the x direction of an axis
%   XDIR flips the current axis.
%   XDIR NORMAL aligns the X-axis with increasing up the screen.
%   XDIR REVERSE aligns the X-axis with increasing up the screen.
%   XDIR(HANDLE,...) uses another graphics handle.

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
  set(ca,'Xdir','normal');
case {'reverse','rev','r'}
  set(ca,'Xdir','reverse');
end

function flipca(ca)
  dir=get(ca,'Xdir');
  switch dir
  case 'normal'
    set(ca,'XDir','reverse');
  case 'reverse'
    set(ca,'XDir','normal');
  end
  return
