/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.CharacteristicGroupReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.CharacteristicGroupWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(CharacteristicGroupReader.class)
@WriterInfo(CharacteristicGroupWriter.class)
public class CharacteristicGroup extends ComplexObject
{
  private String name;
  private final List<Characteristic> characteristics = new ArrayList<>();

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @return the characteristics
   */
  public List<Characteristic> getCharacteristics()
  {
    return characteristics;
  }
  
  public void addCharacteristic(Characteristic c)
  {
    this.characteristics.add(c);
  }
      
}
