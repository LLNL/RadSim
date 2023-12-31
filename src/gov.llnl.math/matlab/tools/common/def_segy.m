function [segy] = segy_struct;

segy.lineSeq = 'int';
segy.reelSe='int';
segy.event_number='int'; 
segy.channel_number='int'; 
segy.energySourcePt='int'; 
segy.cdpEns='int'; 
segy.traceInEnsemble='int'; 
segy.traceID='short'; 
% Trace identification code:
%1 = seismic data    4 = time break 7 = timing
%2 = dead       5 = uphole     8 = water break
%3 = dummy      6 = sweep 9..., optional use 
segy.vertSum='short';
segy.horSum='short';
segy.dataUse='short'; 
segy.sourceToRecDist='int';
segy.recElevation='int';
segy.sourceSurfaceElevation='int';
segy.sourceDepth='int';
segy.datumElevRec='int';
segy.datumElemSource='int';
segy.recWaterDepth='int';
segy.sourceWaterDepth='int';
segy.elevationScale='short';
segy.coordScale='short';
segy.sourceLongOrX='int';
segy.sourceLatOrY='int';
segy.recLongOrX='int';
segy.recLatOrY='int';
segy.coordUnits='short';
segy.weatheringVelocity='short';
segy.subWeatheringVelocity='short';
segy.sourceUpholeTime='short';
segy.recUpholeTime='short';
segy.sourceStaticCor='short';
segy.recStaticCor='short';
segy.totalStatic='short'; 
segy.lagTimeA='short';
segy.lagTimeB='short';
segy.delay='short';
segy.muteStart='short';
segy.muteEnd='short';
segy.sampleLength='short'; 
segy.deltaSample='short'; 
segy.gainType='short'; 
segy.gainConst='short';
segy.initialGain='short';
segy.correlated='short'; 
segy.sweepStart='short';
segy.sweepEnd='short';
segy.sweepLength='short'; 
segy.sweepType='short'; 
segy.sweepTaperAtStart='short';
segy.sweepTaperAtEnd='short';
segy.taperType='short';
segy.aliasFreq='short';
segy.aliasSlope='short';
segy.notchFreq='short';
segy.notchSlope='short';
segy.lowCutFreq='short';
segy.hiCutFreq='short';
segy.lowCutSlope='short';
segy.hiCutSlope='short';
segy.year='short';
segy.day='short';
segy.hour='short';
segy.minute='short';
segy.second='short';
segy.timeBasisCode='short';
segy.traceWeightingFactor='short';
segy.phoneRollPos1='short';
segy.phoneFirstTrace='short';
segy.phoneLastTrace='short';
segy.gapSize='short';
segy.taperOvertravel='short';
segy.station_name='char[6]''=>char';
segy.sensor_serial='char[8]''=>char';
segy.channel_name='char[4]''=>char';
segy.totalStaticHi='short'; 
segy.samp_rate='int';
segy.data_form='short';
segy.m_secs='short';
segy.trigyear='short';
segy.trigday='short';
segy.trighour='short';
segy.trigminute='short';
segy.trigsecond='short';
segy.trigmills='short';
segy.scale_fac='float';
segy.inst_no='short';
segy.not_to_be_used='short';
segy.num_samps='int';
segy.maxvalue='int';
segy.minvalue='int';

