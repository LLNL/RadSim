function [pfa,pd]=plotROC(score_fa, score_pd, varargin)
% Plotting function for ROC curves
%   plotROC(false_alarm_scores, detection_scores, {properties})
%
%  Available properties
%    Line Properties
%      Color           - (float[3]) what color is the line
%      LineStyle       - (string) how should the line plot ('-' default)
%      LineWidth       - (int) how wide should the line be 
%      Marker          - (string) what should be the marker ('none' default)
%      MarkerSize      - (int) size of the marker
%      MarkerFaceColor - (float[3]) what color is the marker filled
%      MarkerEdgeColor - (float[3]) what color is the marker edge
%
%    Labeling Properties
%      Legend - (string) Add a legend entry for this plot
%
%    Errorbar Properties
%      Confidence - (float) What confidence interval to use (0.95 default)
%      ErrorShade - (float) How much lighter should the errorbar (0-4)
%      ErrorLineStyle - (float) How much lighter should the errorbar (0-4)
%      ErrorLegend - (boolean) Should the error bar appear in the legend
%      
%  Marker Styles
%  ==================
%     .     point       v     triangle (down)                        
%     o     circle      ^     triangle (up)                         
%     x     x-mark      <     triangle (left)                      
%     +     plus        >     triangle (right)                    
%     *     star        p     pentagram                          
%     s     square      h     hexagram                          
%     d     diamond       
%
%  Line Styles
%  ==================
%    -     solid
%    :     dotted
%    -.    dashdot 
%    --    dashed   
%    (none)  no line
%          
%          
%          


% Version 
%   2008/1/10 - initial tool with error bars
%   2008/1/11 - fixed minor error in the error bar calculation
%       added many useful(?) properties
%   2012/3/14 - add support for estimating what a bank of replications
%       would do (false_alarm_power)

% We will have two styles of inputs
%   score lists
%   histograms

use_hold=ishold;
% Perform the plotting
% if we were not told to hold the figure, clear it.
if (~ishold)
  clf; 
end
hold on;

% Get the user options
opts=getOpts(varargin);

% Support for PFA scaline
[pd,pfa,n0,n1]=make_curve(score_fa, score_pd);
if (opts.false_alarm_power~=1)
    pfa=1-(1-pfa).^opts.false_alarm_power;
end
pfa=pfa*opts.false_alarm_scale;

% Plot chance curve
if opts.use_chance
  chance=10.^[-4:0.2:0];
  h_chance=area(chance*opts.false_alarm_scale,chance,'FaceColor',0.7*[1 1 1],'EdgeColor',0.5*[1 1 1]);
  hasbehavior(h_chance,'legend',0); % disable legend for these items
end

if (opts.use_points==1)
  h_ROC=line(pfa(1:2:end),pd(1:2:end),opts.roc_prop{:});
else
  h_ROC=line(pfa,pd,opts.roc_prop{:});
end

% Plot Wilson confidence interval
if (opts.error_points==1)
  confidence=sqrt(opts.error_confidence); % sqrt is because we have 2 dimensional confidence interval
  Z=sqrt(2)*erfinv(confidence);

  h=line(1,1,opts.roc_prop{:});
  color=(opts.error_shade+get(h,'Color'))/(1+opts.error_shade);
  delete(h);

  for i0=1:2:length(pd)
    pfa1= (pfa(i0) + Z^2/n0/2 + Z * sqrt(1/n0*(pfa(i0).*(1-pfa(i0))+Z^2/4/n0)))/(1+Z^2/n0);
    pfa2= (pfa(i0) + Z^2/n0/2 - Z * sqrt(1/n0*(pfa(i0).*(1-pfa(i0))+Z^2/4/n0)))/(1+Z^2/n0);
    pd1= (pd(i0) + Z^2/n1/2 + Z * sqrt(1/n1*(pd(i0).*(1-pd(i0))+Z^2/4/n1)))/(1+Z^2/n1);
    pd2= (pd(i0) + Z^2/n1/2 - Z * sqrt(1/n1*(pd(i0).*(1-pd(i0))+Z^2/4/n1)))/(1+Z^2/n1);
    h_conf=line([pfa1 pfa1 pfa2 pfa2 pfa1],[pd1 pd2 pd2 pd1 pd1],'Color',color,opts.error_prop{:});
    hasbehavior(h_conf,'legend',0); % disable legend for these items
  end
end

% Plot Wilson confidence interval
if (opts.error_bars==1)
  % http://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval
  % Wilson interval estimate for confidence intervals
  % given by  
  %    p + Z^2/2n       Z sqrt( 1/n* ( p *(1-p) + Z^2/4n ))
  %   ------------ +/- ------------------------------------
  %    1 + Z^2/n            1 + Z^2/n
  % 
  % Where Z is the number of sigmas to produce x% percential interval 1.96=95%

  confidence=sqrt(opts.error_confidence); % sqrt is because we have 2 dimensional confidence interval
  Z=sqrt(2)*erfinv(confidence);
  pfa1= (pfa + Z^2/n0/2 + Z * sqrt(1/n0*(pfa.*(1-pfa)+Z^2/4/n0)))/(1+Z^2/n0);
  pfa2= (pfa + Z^2/n0/2 - Z * sqrt(1/n0*(pfa.*(1-pfa)+Z^2/4/n0)))/(1+Z^2/n0);
  pd1= (pd + Z^2/n1/2 + Z * sqrt(1/n1*(pd.*(1-pd)+Z^2/4/n1)))/(1+Z^2/n1);
  pd2= (pd + Z^2/n1/2 - Z * sqrt(1/n1*(pd.*(1-pd)+Z^2/4/n1)))/(1+Z^2/n1);

  % Draw the line and set the scale
  h=line(1,1,opts.roc_prop{:});
  color=(opts.error_shade+get(h,'Color'))/(1+opts.error_shade);
  delete(h);
  h_conf1=line(pfa1,pd2,'Color',color,opts.error_prop{:});
  h_conf2=line(pfa2,pd1,'Color',color,opts.error_prop{:});
  hasbehavior(h_conf1,'legend',0); % disable legend for these items
  if (opts.error_legend==0)
    hasbehavior(h_conf2,'legend',0); 
  end
end


% Add legend
if (opts.legend_add==1)
  h_legend=legend;
  if (isempty(h_legend))
    legend_strings={};
    legend_handles=[];
    legend(legend_strings{:})
  else
    % use old legend
    legend_strings=get(h_legend,'String');
    legend_handles=get(h_legend,'UserData');
    legend_handles=legend_handles.handles;
  end
  legend_strings{end+1}=opts.legend_text;
  legend_handles(end+1)=h_ROC;
  if (opts.error_legend==1)
    legend_strings{end+1}=sprintf('%s - %.0f%% Confidence',opts.legend_text, 100*opts.error_confidence) ;
    legend_handles(end+1)=h_conf1;
  end
  legend_handles
  legend_strings
  legend(legend_handles,legend_strings{:},'Location','SE')
end


% Make the plot pretty 
set(gca,'XMinorTick','on')
set(gca,'XScale','log')
set(gca,'YLim',[0.0 1.01])
if (opts.use_grid)
  set(gca,'XGrid','on');
  set(gca,'YGrid','on');
  set(gca,'XMinorGrid','on');
  set(gca,'YMinorGrid','on');
else
  set(gca,'XGrid','off');
  set(gca,'YGrid','off');
  set(gca,'XMinorGrid','off');
  set(gca,'YMinorGrid','off');
end

set(gcf,'Color',[1 1 1]);
%set(gca,'PlotBoxAspectRatio',[1 1 1])
set(gca,'FontSize',14,'FontName','Times New Roman');
set(gca,'Box','on')

% Create labels
xlabel(opts.xlabel,'FontSize',16,'FontName','Times New Roman');
ylabel(opts.ylabel,'FontSize',16,'FontName','Times New Roman');

if use_hold==0
  hold off;
end
 
if nargout<1
  clear
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Convert a set of scores into a ROC pd/pfa curve
function [pd,pfa,n0,n1]=make_curve(score0, score1)
% sort the scores for both
score0=sort(score0(:));
score1=sort(score1(:));

% get the length of the two lists
n0=length(score0);
n1=length(score1);

n2=max(n0,n1);
pd=zeros(2*n2,1);
pfa=zeros(2*n2,1);

% to get this right we are going to have to walk through both lists 
% at the same time.
i0=1; % counter for list 1
i1=1; % counter for list 2
i2=2; % counter for new list
pd(1)=n1; % trivial first point.  If everything is an alarm then Pd=1 Pfa=1
pfa(1)=n0;
while (i0<n0)&(i1<=n1)

  % while there are scores less than the next threat, we can raise the threshold
  while (i0<=n0) & (i1<=n1) & score0(i0)<score1(i1)
    i0=i0+1;
  end

  % is we hit the end of the list of scores we have the highest supportable 
  % Pd given the current Pfa
  if (i0>n0)
    pd(i2)=n1-i1+1;
    pfa(i2)=1;
    i2=i2+1;
    break;
  end

  pd(i2)=n1-i1+1;
  pfa(i2)=n0-i0+1;
  i2=i2+1;

  % while the next bkg is greater then the threat we reduce the pd
  while (i0<=n0) & (i1<=n1) & score0(i0)>=score1(i1)
    i1=i1+1;
  end

  pd(i2)=n1-i1+1;
  pfa(i2)=n0-i0+1;
  i2=i2+1;

end

i2=i2-1;
pfa=pfa(1:i2)/n0;
pd=pd(1:i2)/n1;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Convert a histogram into a ROC pd/pfa curve
function [pd,pfa,n0,n1]=make_curve2(hist0, hist1)

% get the length of the two lists
n0=sum(hist0);
n1=sum(hist1);

n2=max(length(hist0),length(hist1));
pd=zeros(2*n2,1);
pfa=zeros(2*n2,1);

i2=2; % counter for new list
pd(1)=n1; % trivial first point.  If everything is an alarm then Pd=1 Pfa=1
pfa(1)=n0;
for i0=1:n2
  pd(i2)=pd(i2-1)-hist1(i0);
  pfa(i2)=pfa(i2-1);
  i2=i2+1;
  pd(i2)=pd(i2-1);
  pfa(i2)=pfa(i2-1)-hist0(i0);
  i2=i2+1;
end

i2=i2-1;
pfa=max(pfa(1:i2-1),0.5)/n0;
pd=max(pd(1:i2-1),0.5)/n1;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%

function opts=getOpts(argv)


% initialize control variables
opts.error_bars=0;
opts.error_points=0;
opts.error_confidence=0.95;
opts.error_shade=1;
opts.error_legend=0;

opts.legend_add=0;
opts.legend_text='unlabeled';

opts.use_points=0;
opts.use_chance=0;
opts.use_grid=0;

opts.xlabel='FAR';
opts.ylabel='PD';
opts.false_alarm_scale=1;
opts.false_alarm_power=1;

% process the arguments
opts.roc_prop={};
opts.error_prop={};
i1=1;
i2=1;
i3=1;
while i1<=length(argv)
  %argv{i1}

  %properties to copy
  switch(lower(argv{i1}))

    case {'xlabel'}
      i1=i1+1;
      opts.xlabel=argv{i1};

    case {'ylabel'}
      i1=i1+1;
      opts.ylabel=argv{i1};

    case {'fa','false_alarm_scale'}
      i1=i1+1;
      opts.false_alarm_scale=argv{i1};
      
    case {'fapow','false_alarm_power'}
      i1=i1+1;
      opts.false_alarm_power=argv{i1};

  % LINE properties
    % copy these properties to the line.
    case {'linestyle','linewidth','color', ...
      'marker','markersize','markerfacecolor', 'markeredgecolor'}
      opts.roc_prop{i2}=argv{i1}; i2=i2+1; i1=i1+1;  %#ok<AGROW>
      opts.roc_prop{i2}=argv{i1}; i2=i2+1;           %#ok<AGROW>

  % Labeling properties
    % add legend as we plot
    case {'legend'}
      i1=i1+1;
      opts.legend_add=1;
      opts.legend_text=argv{i1};

  % Errorbar properties
    % Manually turn on error bars
    case {'errorbars'}
      i1=i1+1;
      opts.error_bars=argv{i1};

    case {'errorpoints'}
      i1=i1+1;
      opts.error_points=argv{i1};

    % what confidence window should we use
    case {'confidence'} 
      i1=i1+1;
      opts.error_confidence=argv{i1};
      opts.error_bars=1;

    % how different should the confidence be in color
    case {'errorshade'}  
      i1=i1+1;
      opts.error_shade=argv{i1}; 
      opts.error_bars=1;

    case {'errorlinestyle','errorlinewidth'}
      opts.error_prop{i3}=argv{i1}(6:end);
      i1=i1+1;
      opts.error_prop{i3+1}=argv{i1};
      i3=i3+2;
      
    case {'alllinestyle','alllinewidth'}
      opts.roc_prop{i2}=argv{i1}(4:end);
      opts.error_prop{i3}=argv{i1}(4:end);
      i1=i1+1;
      opts.roc_prop{i2+1}=argv{i1};
      opts.error_prop{i3+1}=argv{i1};
      i3=i3+2;
      i2=i2+2;
 
    % should the error bar be in the legend?
    case {'errorlegend'} 
      i1=i1+1;
      opts.error_legend=argv{i1}; 
      opts.error_bars=1;

    % plot the chance bar
    case {'chance'}
      i1=i1+1;
      opts.use_chance=argv{i1};

    % plot using step or point style
    case {'points'}
      i1=i1+1;
      opts.use_points=argv{i1};

    % show grid
    case {'grid'}
      i1=i1+1;
      opts.use_grid=argv{i1};


  end
  i1=i1+1;
end


