function ydir(arg1,arg2)
% YTICK controls the printing of tick labels on y axis.
%   YTICK toggles printing.
%   YTICK ON/OFF sets to print on not print
%   YTICK(HANDLE,...) uses another graphics handle.

ca=gca;
opt='';
if (nargin<1)
  toggleca(ca)
  return
end

if (ishandle(arg1))
  if (nargin<2)
    toggleca(arg1)
    return
  else
    ca=arg1;
    opt=arg2;
  end
else
  opt=arg1;
end


switch lower(opt)
case {'on'}
  set(ca,'YtickLabelMode','auto');
case {'off','rev','r'}
  set(ca,'YtickLabel',[]);
end

function toggleca(ca)
  label=get(ca,'YtickLabel');
  if (size(label)==[0 0])
    set(ca,'YtickLabelMode','auto');
  else 
    set(ca,'YtickLabel',[]);
  end
  return
