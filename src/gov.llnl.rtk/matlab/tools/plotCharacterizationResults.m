function plotCalibrationResults(calibrationResults)
% Plot the results form extracting the calibration set.

% Print a summary
for i=1:calibrationResults.size()
  rec=calibrationResults.get(i-1);
  if (isempty(rec.info))
    continue;
  end
  fprintf('%.2f\t%f\t%f\n',rec.getEnergy(), rec.info.channel, rec.channel);
end

% Produce a pretty plot
clf
r=ceil(sqrt(calibrationResults.size()));
c=ceil(calibrationResults.size()/r);
for i=1:calibrationResults.size();
  rec=calibrationResults.get(i-1);
  roi=rec.roi;
  subplot(r,c,i);
  N=length(rec.spectrum);
  plot([0:N-1],[rec.spectrum rec.fit rec.residual]);
  mx=max(rec.spectrum(roi.lower+1:roi.upper));
  axis([roi.lower roi.upper-1 0 1.1*mx]);
  if (~isempty(rec.info))
    line(rec.info.channel*[1 1],[0 1.1*mx],'Color',[1 0 0 ]);
  end
  line(rec.channel*[1 1],[0 1.1*mx],'Color',[0 0 1 ]);
  h=line([roi.center roi.center],[0 1.1*mx],'Color',[0 0 0 ],'LineStyle','--');

  title(sprintf('%s %.1fkeV',rec.label, rec.getEnergy()))
end


