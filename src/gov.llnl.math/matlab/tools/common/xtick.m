function xtick(arg1,arg2)
% XTICK controls the printing of tick labels on y axis.
%   XTICK toggles printing.
%   XTICK ON/OFF sets to print on not print
%   XTICK(HANDLE,...) uses another graphics handle.

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
  set(ca,'XtickLabelMode','auto');
case {'off','rev','r'}
  set(ca,'XtickLabel',[]);
end

function toggleca(ca)
  label=get(ca,'XtickLabel');
  if (size(label)==[0 0])
    set(ca,'XtickLabelMode','auto');
  else 
    set(ca,'XtickLabel',[]);
  end
  return
