function labelEnergyScale(R1, R2, labels)

ax=axis();
dy=(ax(4)-ax(3))*0.1;

% Place the labels
for i=1:length(labels)
  h(i)=text(R2(i,1), R2(i,2), labels{i},'Rotation',90);
end

% Nudge overlapping labels
for i=1:5
  for j=1:length(h)-1
    ex1=get(h(j),'Extent');
    ex2=get(h(j+1),'Extent');
    overlap=ex1(1)+ex1(3)-ex2(1);
    if (overlap>0)
      p1=get(h(j),'Position');
      p2=get(h(j+1),'Position');
      set(h(j),'Position',p1-[overlap/4 0 0]);
      set(h(j+1),'Position',p2+[overlap/4 0 0]);
    end
  end
end

% Post clean up lines
for i=1:length(h)
  p1=get(h(i),'Position');
  h2=line([R1(i,1) p1(1)],[R1(i,2) p1(2)],'Color',[0 0 0]);
 h2.Annotation.LegendInformation.IconDisplayStyle='off';
end


