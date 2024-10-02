package gov.llnl.rtk.mcnp;

import gov.llnl.rtk.flux.FluxBinned;
import gov.llnl.rtk.flux.FluxGroupBin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class Result {

    // private Map<Integer, HashMap<Integer, FluxBinned>> spectra = new HashMap<>();
    private ArrayList<FluxBinned> spectra = new ArrayList<>();

    public Result(File outputFile) throws Exception {
        List<String> allLines = getLines(outputFile);
        List<List<String>> tallies = getTallies(allLines);
        for (List<String> tally : tallies) {
            List<List<String>> spectraLines = getSpectra(tally);
            for (List<String> lines : spectraLines) {
                spectra.add(parseSpectrum(lines));
            }
        }
    }

    private FluxBinned parseSpectrum(List<String> lines) {
        FluxBinned spectrum = new FluxBinned();
        double lowerEnergyBin = 0.0;
        for (String line : lines) {
            String[] entries = line.split("\\s+");
            double upperEnergyBin = Double.parseDouble(entries[1]);
            if (upperEnergyBin == 0.0) {
                continue;
            }
            double counts = Double.parseDouble(entries[2]);
            double uncertainty = counts * Double.parseDouble(entries[3]);
            spectrum.addPhotonGroup(new FluxGroupBin(1000.0 * lowerEnergyBin, 1000.0 * upperEnergyBin, counts, uncertainty));
            lowerEnergyBin = upperEnergyBin;
        }
        return spectrum;
    }

    // Todo: cell tally support
    private List<List<String>> getSpectra(List<String> lines) {
        ArrayList<List<String>> spectra = new ArrayList<>();
        boolean atSpectrum = false;
        for (String line : lines) {
            if (line.contains("energy")) {
                continue;
            }
            if (line.contains("angle")) {
                continue;
            }
            if (line.contains("total")) {
                atSpectrum = false;
            }
            if (atSpectrum) {
                spectra.get(spectra.size() - 1).add(line);
            }
            if (line.contains("surface ")) {
                try {
                    int locationId = Integer.parseInt(line.split("\\s+")[2]);
                    atSpectrum = true;
                    spectra.add(new ArrayList<>());
                } catch (NumberFormatException e) {
                    atSpectrum = false;
                }
            }
        }
        return spectra;
    }

    private List<List<String>> getTallies(List<String> lines) {
        ArrayList<List<String>> tallies = new ArrayList<>();
        boolean atTally = false;
        for (String line : lines) {
            if (line.contains("1tally")) {
                try {
                    int tallyId = Integer.parseInt(line.split("\\s+")[1]);
                    atTally = true;
                    tallies.add(new ArrayList<>());
                } catch (NumberFormatException e) {
                    atTally = false;
                }
            }

            if (line.contains("1status")) {
                atTally = false;
            }

            if (atTally) {
                tallies.get(tallies.size() - 1).add(line);
            }
        }
        return tallies;
    }

    private List<String> getLines(File file) throws Exception {
        Scanner scanner = new Scanner(file);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines;
    }

    public List<FluxBinned> getSpectra() {
        return spectra;
    }

    public static void main(String ... args) throws Exception {
        Result result = new Result(new File("C:\\Users\\lahmann1\\IdeaProjects\\radsim-mcnp-api\\py\\test_dir\\RadSim_SourceGen_Test_1712336443594.output"));
        System.out.println();

    }
}
