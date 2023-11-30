DEVEL=getenv('DEVEL');

paths={};
paths{end+1}=[ DEVEL '/gov.llnl.math/dist/gov.llnl.math.jar'];
paths{end+1}=[ DEVEL '/gov.llnl.utility/dist/gov.llnl.utility.jar'];
paths{end+1}=[ DEVEL '/gov.llnl.rtk/dist/gov.llnl.rtk.jar'];
javaaddpath(paths);

clear DEVEL paths;
