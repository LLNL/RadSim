// ROOT macro file for plotting example B4 histograms
//
// Can be run from ROOT session:
// root[0] .x plotHisto.C

{
  gROOT->Reset();
  gROOT->SetStyle("Plain");

  // Draw histos filled by Geant4 simulation
  //

  // Open file filled by Geant4 simulation
  TFile f("RadSim.root");

  // Create a canvas and divide it into 3x2 pads
  TCanvas* c1 = new TCanvas("c1", "", 20, 20, 1500, 1000);
  c1->Divide(3,2);

  // Draw Ebeta histogram in the pad 1
  c1->cd(1);
  gPad->SetLogy(1);
  TH1D* hist1 = (TH1D*)f.Get("Ebeta");
  hist1->Draw("HIST");

  // Draw ElectronEnergy_escape histogram in the pad 2
  c1->cd(2);
  gPad->SetLogy(1);
  TH1D* hist2 = (TH1D*)f.Get("ElectronEnergy_escape");
  hist2->Draw("HIST");

  // // Draw PositronEnergy_escape histogram in the pad 3
  // // with logaritmic scale for y
  TH1D* hist3 = (TH1D*)f.Get("PositronEnergy_escape");
  c1->cd(3);
  gPad->SetLogy(1);
  hist3->Draw("HIST");

  // // Draw GammaEnergy_primary histogram in the pad 4
  // // with logaritmic scale for y
  TH1D* hist4 = (TH1D*)f.Get("GammaEnergy_primary");
  c1->cd(4);
  gPad->SetLogy(1);
  hist4->Draw("HIST");

  // Draw GammaEnergy_escape histogram in the pad 5
  // with logaritmic scale for y
  c1->cd(5);
  gPad->SetLogy(1);
  TH1D* hist5 = (TH1D*)f.Get("GammaEnergy_escape");
  hist5->Draw("HIST");
}
