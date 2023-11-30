% testWaveletSmoothing.m
clc;close all;clear;



% the default wavelet scale is 2
scale = 3;
wtf = gov.llnl.math.wavelet.WaveletTransformFactory();
wtf.setFamily("daub");
% email from Karl Nelson 12/11/2018
%Please note that the current
%definition of daub are as in the wiki thus the daub[4] in the supplied code is daub,length=8 because the table was
%looking up the coefficients of twice the length.


wtf.setLength(12); % same as in extract_smoothed_signal_using_modwt
wt = wtf.create();


%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% inputs for this script which tests the source extractor
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

% You can use test_extract_source_profile_with_ground_truth.m in several different ways.
% The recordSource parameter determines whether it is from simulation or database or serialized records.
%

% choose only one as to where the record is coming from
recordSource = 'Simulation';
%recordSource = 'DAC_TER';
%recordSource = 'Charleston';
%recordSource = 'VDCE';

%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% inputs for the source extractor
%~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
% this is an input for the inject source simulation code to insert one source
% if this flag is set to 0, then two sources will be inserted (one NORM and the other either a fissile, industrial, or
% medical (this is hard coded but can be changed)
insertSingleSourceFlag = 0;


% This flag has effect only when recordSource = 'Simulation';
% Keep has this flag is always set to 0. If set to 1, it reprocesses the last used record which is always serialized a
% srecord.ernie (in case you want to debug)
useExistingRecord = 0;


switch recordSource
    
    case 'Simulation'
        dbc = gov.llnl.ernie.Database(); % select non-emitting record for injector
        nRecords = 1;
        recordManipulator = gov.llnl.ernie.common.RecordManipulator();
        recordManipulator.loadSources('../data/shieldingFe.xml', '../data/sourceLibrary1.0.xml');
        
    case 'Charleston'
        dbc = gov.llnl.ernie.Database(); % select non-emitting record for injector
        recordNumber = [703643737,682220094,699311505,689918058,682213355,682211658,707696821,698540366,682207720,601459243,707688737,684429096,704137117,671561897,667862193,675091969,605909164,675087499,675090513,649847532];
        %recordNumber = recordNumber(16:end);
        nRecords = length(recordNumber);
        
    case 'VDCE'
        dbc = gov.llnl.ernie.Database('RPM_VDCE_May2016');
        %recordIds=load('../VDCE_records.txt');
        recordNumber = [205980:205988  206520:206530  206820:206830 ];
        %recordNumber = recordNumber(16:end);
        nRecords = length(recordNumber);
        bkgdListDir = 'C:\Users\chandrasekar1\Documents\ErnieTrunk\proj-ernie\matlab\fromZack\average background info';
        userProvidedBkgdFlag = true;
        
    case  'DAC_TER'
        % provide appropriate directories
        % choose directory where serialized ernie records
        serializedDir =  'C:\Users\chandrasekar1\Documents\ErnieTrunk\proj-ernie\matlab\fromZack\Source\records';
        
        % test reinjected
        bkgdListDir = 'C:\Users\chandrasekar1\Documents\ErnieTrunk\proj-ernie\matlab\fromZack\average background info';
        
        
        filelist = dir( strcat( serializedDir, '/*.ernie' ) )';
        nRecords = length(filelist);
        saveResultsFlag = 1;
        %saveResultsPrefix = [recordSource '_reinject'];
        
end

serializedRecordFilename = '';
figure('Units','normalized', 'Position',[0 0 1 1], 'WindowStyle', 'docked');



for iRecord = 1:nRecords
    if(~mod(iRecord,100))
        fprintf('%d/%d\n', iRecord,nRecords);
    end
    
    switch recordSource
        case 'Simulation'
            if(~useExistingRecord)
                [record,combinedSpectrum,distributedResult,distributedSpectrum,truthStruct,compactResult,compactSpectrum] =...
                    inject_two_sources(dbc,recordManipulator,insertSingleSourceFlag);
                save_serialized_record(record,combinedSpectrum,distributedResult,distributedSpectrum,truthStruct,compactResult,compactSpectrum);
            else
                [record,combinedSpectrum,distributedResult,distributedSpectrum,truthStruct,compactResult,compactSpectrum] =...
                    load_serialized_record();
            end
            
        case 'Charleston'
            record = dbc.getRecord(recordNumber(iRecord));
            truthStruct = [];
            
        case 'VDCE'
            record = dbc.getRecord(recordNumber(iRecord));
            truthStruct = [];
            
        case 'DAC_TER'
            rid = filelist(iRecord);
            
            fprintf('%s\n', rid.name);
            serializedRecordFilename = rid.name;
            record = gov.llnl.ernie.RecordSerializer.importRecord( java.io.File(serializedDir, rid.name) );
            if(record.bad)
                fprintf('%s Record bad, skipping this record \n',  rid.name);
                fprintf('Reason:%s  \n', record.getBadReason);
                continue;
            end
            truthStruct = [];
    end
    
    
    panelDataStruct = get_background_estimate(record);
    
    panelDataStruct.nPanels = record.getNPanels;
    panelDataStruct.nLength = record.getLength;
    panelDataStruct.recordNumber = record.getSegmentDescriptorId;
    panelDataStruct.filename = serializedRecordFilename;
    
    nPanels = panelDataStruct.nPanels;
    nLength = panelDataStruct.nLength;
    nChannels = size(panelDataStruct.panelRawData,2);
    smoothedPanelData = zeros(size(panelDataStruct.panelRawData));
    smoothedPanelData1 = zeros(size(panelDataStruct.panelRawData));
    
    ss = zeros(size(panelDataStruct.panelRawData));
    
    for iChannel = 1:nChannels
        
        for jPanel = 1:nPanels
            
            startIndex = (jPanel-1)*nLength +1;
            endIndex = startIndex + nLength-1;
            smoothedPanelData(startIndex:endIndex,iChannel) = extract_smoothed_signal_using_dwt(panelDataStruct.panelRawData(startIndex:endIndex,iChannel),scale);
            
            smoothedPanelData1(startIndex:endIndex,iChannel) = extract_smoothed_signal_using_modwt(panelDataStruct.panelRawData(startIndex:endIndex,iChannel),scale);
            
            ss(startIndex:endIndex,iChannel) = wt.smooth(panelDataStruct.panelRawData(startIndex:endIndex,iChannel),scale);
            
            
        end
    end
    
    for iChannel = 1:nChannels
        subplot(2,1,1);
        h1 = plot(panelDataStruct.panelRawData(:,iChannel),'b.-');
        hold on;
        h2 = plot(smoothedPanelData(:,iChannel),'g.-');
        hold on;
        h3 = plot(smoothedPanelData1(:,iChannel),'kx-');
        h4 = plot(ss(:,iChannel),'mo-');
        %pause;
        title(['Record: ' num2str(panelDataStruct.recordNumber) ', Energy Channel: ' num2str(iChannel) ', Source: ' , recordSource], 'interpreter', 'none');
        xlabel('Time index');
        legend([h1 h2 h3 h4], {'raw gamma counts', 'smoothing using built-in dwt using sym8', 'smoothing using overcomplete wt using db6', 'Java wavelet method using db6'});
        ylabel('Counts');
        subplot(2,1,2);
        plot(smoothedPanelData1(:,iChannel)-ss(:,iChannel),'b.-');
        title(['Record: ' num2str(panelDataStruct.recordNumber) ', Energy Channel: ' num2str(iChannel) ', Source: ' , recordSource], 'interpreter', 'none');
        xlabel('Time index');
        ylabel('Error (Matlab output- Java output)');
        pause(0.5)
        clf;
    end
    
end

close all;
return



%             ix = startIndex:endIndex;
%             subplot(2,1,1);
%             h1 = plot(panelDataStruct.panelRawData(ix,iChannel),'b.-');
%             hold on;
%             h2 = plot(smoothedPanelData(ix,iChannel),'g.-');
%             hold on;
%             h3 = plot(smoothedPanelData1(ix,iChannel),'kx-');
%             h4 = plot(ss(ix,iChannel),'mo-');
%             %pause;
%             title(['Record: ' num2str(panelDataStruct.recordNumber) ', Panle: ' num2str(jPanel)  ', Energy Channel: ' num2str(iChannel) ', Source: ' , recordSource], 'interpreter', 'none');
%             xlabel('Time index');
%             legend([h1 h2 h3 h4], {'raw gamma counts', 'smoothing using built-in dwt using sym8', 'smoothing using overcomplete wt using db6', 'Java wavelet method using db6'});
%             ylabel('Counts');
%             subplot(2,1,2);
%             plot(smoothedPanelData1(:,iChannel)-ss(:,iChannel),'b.-');
%             title(['Record: ' num2str(panelDataStruct.recordNumber) ', Energy Channel: ' num2str(iChannel) ', Source: ' , recordSource], 'interpreter', 'none');
%             xlabel('Time index');
%             ylabel('Error (Matlab output- Java output)');
%             pause(0.5)
%             clf;