/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */

import gov.llnl.rtk.physics.Geometry;
import gov.llnl.rtk.physics.LayerImpl;
import gov.llnl.rtk.physics.MaterialImpl;
import gov.llnl.rtk.physics.SphericalModel;
import gov.llnl.rtk.physics.Units;
import gov.llnl.utility.io.WriterException;
import gov.llnl.utility.xml.bind.DocumentWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author nelson85
 */
public class SourceModelTest
{
//  static public void main(String[] args) throws IOException, WriterException
//  {
//    SphericalModel model = new SphericalModel();
//    model.setGeometry(Geometry.newSpherical());
//    MaterialImpl material = new MaterialImpl();
//    material.addElement("Cs137", 0, 10 * Units.get("uCi"), true);
//    MaterialImpl material2 = new MaterialImpl();
//    material2.addElement("Pb", 1, 0, true);
//    LayerImpl layer = new LayerImpl();
//    layer.setThickness(1.0 * Units.get("cm"));
//    layer.setMaterial(material);
//
//    LayerImpl layer2 = new LayerImpl();
//    layer2.setThickness(20.0 * Units.get("cm"));
//    layer2.setMaterial(material2);
//
//    model.addLayer(layer);
//    model.addLayer(layer2);
//
//    DocumentWriter<SphericalModel> smw = DocumentWriter.create(SphericalModel.class);
//    smw.saveFile(Paths.get("model.xml"), model);
//  }
}
