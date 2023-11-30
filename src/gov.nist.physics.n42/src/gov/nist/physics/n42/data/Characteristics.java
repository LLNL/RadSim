/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.data;

import gov.nist.physics.n42.reader.CharacteristicsReader;
import gov.llnl.utility.xml.bind.ReaderInfo;
import gov.llnl.utility.xml.bind.WriterInfo;
import gov.nist.physics.n42.writer.CharacteristicsWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author nelson85
 */
@ReaderInfo(CharacteristicsReader.class)
@WriterInfo(CharacteristicsWriter.class)
public class Characteristics extends ComplexObject
{
  private final List<Characteristic> characteristics = new ArrayList<>();
  private final List<CharacteristicGroup> characteristicGroups = new ArrayList<>();

  public void addCharacteristic(Characteristic c)
  {
    this.getCharacteristics().add(c);
  }

  public void addCharacteristicGroup(CharacteristicGroup c)
  {
    this.getCharacteristicGroups().add(c);
  }

  /**
   * @return the characteristics
   */
  public List<Characteristic> getCharacteristics()
  {
    return characteristics;
  }

  /**
   * @return the characteristicGroups
   */
  public List<CharacteristicGroup> getCharacteristicGroups()
  {
    return characteristicGroups;
  }

}
