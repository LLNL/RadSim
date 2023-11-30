function [sacwave] = rdsac(sfile)

fid=fopen(sfile, 'r','b');  % force the format to be big endian
sacwave.DELTA=fread(fid,1,'float');
sacwave.DEPMIN=fread(fid,1,'float');
sacwave.DEPMAX=fread(fid,1,'float');
sacwave.SCALE=fread(fid,1,'float');
sacwave.ODELTA=fread(fid,1,'float');

sacwave.B=fread(fid,1,'float');
sacwave.E=fread(fid,1,'float');
sacwave.O=fread(fid,1,'float');
sacwave.A=fread(fid,1,'float');
sacwave.INT0=fread(fid,1,'float');

sacwave.T0=fread(fid,1,'float');
sacwave.T1=fread(fid,1,'float');
sacwave.T2=fread(fid,1,'float');
sacwave.T3=fread(fid,1,'float');
sacwave.T4=fread(fid,1,'float');

sacwave.T5=fread(fid,1,'float');
sacwave.T6=fread(fid,1,'float');
sacwave.T7=fread(fid,1,'float');
sacwave.T8=fread(fid,1,'float');
sacwave.T9=fread(fid,1,'float');

sacwave.F=fread(fid,1,'float');
sacwave.RESP0=fread(fid,1,'float');
sacwave.RESP1=fread(fid,1,'float');
sacwave.RESP2=fread(fid,1,'float');
sacwave.RESP3=fread(fid,1,'float');

sacwave.RESP4=fread(fid,1,'float');
sacwave.RESP5=fread(fid,1,'float');
sacwave.RESP6=fread(fid,1,'float');
sacwave.RESP7=fread(fid,1,'float');
sacwave.RESP8=fread(fid,1,'float');

sacwave.RESP9=fread(fid,1,'float');
sacwave.STLA=fread(fid,1,'float');
sacwave.STLO=fread(fid,1,'float');
sacwave.STEL=fread(fid,1,'float');
sacwave.STDP=fread(fid,1,'float');

sacwave.EVLA=fread(fid,1,'float');
sacwave.EVLO=fread(fid,1,'float');
sacwave.EVEL=fread(fid,1,'float');
sacwave.EVDP=fread(fid,1,'float');
sacwave.UN0=fread(fid,1,'float');

sacwave.USER0=fread(fid,1,'float');
sacwave.USER1=fread(fid,1,'float');
sacwave.USER2=fread(fid,1,'float');
sacwave.USER3=fread(fid,1,'float');
sacwave.USER4=fread(fid,1,'float');

sacwave.USER5=fread(fid,1,'float');
sacwave.USER6=fread(fid,1,'float');
sacwave.USER7=fread(fid,1,'float');
sacwave.USER8=fread(fid,1,'float');
sacwave.USER9=fread(fid,1,'float');

sacwave.DIST=fread(fid,1,'float');
sacwave.AZ=fread(fid,1,'float');
sacwave.BAZ=fread(fid,1,'float');
sacwave.GCARC=fread(fid,1,'float');
sacwave.INT1=fread(fid,1,'float');

sacwave.INT2=fread(fid,1,'float');
sacwave.DEPMEN=fread(fid,1,'float');
sacwave.CMPAZ=fread(fid,1,'float');
sacwave.CMPINC=fread(fid,1,'float');
sacwave.UN1=fread(fid,1,'float');

sacwave.UN2=fread(fid,1,'float');
sacwave.UN3=fread(fid,1,'float');
sacwave.UN4=fread(fid,1,'float');
sacwave.UN5=fread(fid,1,'float');
sacwave.UN6=fread(fid,1,'float');

sacwave.UN7=fread(fid,1,'float');
sacwave.UN8=fread(fid,1,'float');
sacwave.UN9=fread(fid,1,'float');
sacwave.UN10=fread(fid,1,'float');
sacwave.UN11=fread(fid,1,'float');

sacwave.NZYEAR=fread(fid,1,'long');
sacwave.NZJDAY=fread(fid,1,'long');
sacwave.NZHOUR=fread(fid,1,'long');
sacwave.NZMIN=fread(fid,1,'long');
sacwave.NZSEC=fread(fid,1,'long');

sacwave.NZMSEC=fread(fid,1,'long');
sacwave.NVHDR=fread(fid,1,'long');
sacwave.INT3=fread(fid,1,'long');
sacwave.INT4=fread(fid,1,'long');
sacwave.NPTS=fread(fid,1,'long');

sacwave.INT5=fread(fid,1,'long');
sacwave.INT6=fread(fid,1,'long');
sacwave.UN12=fread(fid,1,'long');
sacwave.UN13=fread(fid,1,'long');
sacwave.UN14=fread(fid,1,'long');

sacwave.IFTYPE=fread(fid,1,'long');
sacwave.IDEP=fread(fid,1,'long');
sacwave.IZTYPE=fread(fid,1,'long');
sacwave.UN15=fread(fid,1,'long');
sacwave.IINST=fread(fid,1,'long');

sacwave.ISTREG=fread(fid,1,'long');
sacwave.IEVREG=fread(fid,1,'long');
sacwave.IEVTYP=fread(fid,1,'long');
sacwave.IQUAL=fread(fid,1,'long');
sacwave.ISYNTH=fread(fid,1,'long');

sacwave.UN16=fread(fid,1,'long');
sacwave.UN17=fread(fid,1,'long');
sacwave.UN18=fread(fid,1,'long');
sacwave.UN19=fread(fid,1,'long');
sacwave.UN20=fread(fid,1,'long');

sacwave.UN21=fread(fid,1,'long');
sacwave.UN22=fread(fid,1,'long');
sacwave.UN23=fread(fid,1,'long');
sacwave.UN24=fread(fid,1,'long');
sacwave.UN25=fread(fid,1,'long');

sacwave.LEVEN1=fread(fid,1,'long');
sacwave.LEVEN2=fread(fid,1,'long');
sacwave.LPSPOL1=fread(fid,1,'long');
sacwave.LPSPOL2=fread(fid,1,'long');
sacwave.LOVROK1=fread(fid,1,'long');

sacwave.KSTNM=fread(fid,8,'char');
sacwave.KEVNM=fread(fid,16,'char');

sacwave.KHOLE=fread(fid,8,'char');
sacwave.KO=fread(fid,8,'char');
sacwave.KA=fread(fid,8,'char');

sacwave.KT0=fread(fid,8,'char');
sacwave.KT1=fread(fid,8,'char');
sacwave.KT2=fread(fid,8,'char');

sacwave.KT3=fread(fid,8,'char');
sacwave.KT4=fread(fid,8,'char');
sacwave.KT5=fread(fid,8,'char');

sacwave.KT6=fread(fid,8,'char');
sacwave.KT7=fread(fid,8,'char');
sacwave.KT8=fread(fid,8,'char');

sacwave.KT9=fread(fid,8,'char');
sacwave.KF=fread(fid,8,'char');
sacwave.KUSER0=fread(fid,8,'char');

sacwave.KUSER1=fread(fid,8,'char');
sacwave.KUSER2=fread(fid,8,'char');
sacwave.KCMPNM=fread(fid,8,'char');

sacwave.KNETWK=fread(fid,8,'char');
sacwave.KDATRD=fread(fid,8,'char');
sacwave.KINST=fread(fid,8,'char');

sacwave.x = fread(fid, sacwave.NPTS, 'float');

status=fclose(fid);

