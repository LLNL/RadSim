/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.nist.physics.n42.reader;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.ReaderContext;
import gov.nist.physics.n42.N42Package;
import gov.nist.physics.n42.data.ComplexObject;
import gov.nist.physics.n42.data.MultimediaData;
import gov.nist.physics.n42.data.RadItemInformation;
import org.xml.sax.Attributes;

/**
 *
 * @author nelson85
 */
@Reader.Declaration(pkg = N42Package.class,
        name = "MultimediaData",
        order = Reader.Order.SEQUENCE,
        cls = MultimediaData.class,
        typeName = "MultimediaDataType")
@Reader.Attribute(name = "id", type = String.class, required = false)
@Reader.Attribute(name = "radItemInformationReferences", type = String.class, required = false)
@Reader.Attribute(name = "sequenceNumber", type = Integer.class, required = false)
public class MultimediaDataReader extends ObjectReader<MultimediaData>
{
  @Override
  public MultimediaData start(ReaderContext context, Attributes attr) throws ReaderException
  {
    MultimediaData out = new MultimediaData();
    ReaderUtilities.register(context, out, attr);
    ReaderUtilities.addReferences(context, out,
            MultimediaData::addRadItem,
            RadItemInformation.class,
            attr.getValue("radItemInformationReferences"));
    return out;
  }

  @Override
  public Reader.ElementHandlerMap getHandlers(ReaderContext context)
          throws ReaderException
  {
    Reader.ReaderBuilder<MultimediaData> builder = this.newBuilder();
        builder.element("Remark").callString(ComplexObject::addRemark).optional().unbounded();
//    builder.element("MultimediaDataDescription").optional();
//    ReaderBuilder<MultimediaData> grp = builder.choice(Options.OPTIONAL, Options.UNBOUNDED);
//    grp.element("BinaryUTF8Object").callString()
//    grp.element("BinaryHexObject").callString();
//    grp.element("BinaryBase64Object").callString();
//    builder.element("MultimediaCaptureStartDateTime").optional();
//    builder.element("MultimediaCaptureDuration").optional();
//    builder.element("MultimediaFileURI").optional();
//    builder.element("MultimediaFileSizeValue").optional();
//    builder.element("MultimediaDataMIMEKind").optional();
//    builder.element("EncodingMIMEKind").optional();
//    builder.element("MultimediaDeviceCategoryCode").optional();
//    builder.element("MultimediaDeviceIdentifier").optional();
//    builder.element("ImagePerspectiveCode").optional();
//    builder.element("ImageWidthValue").optional();
//    builder.element("ImageHeightValue").optional();
    builder.reader(new ExtensionReader("MultimediaDataExtension")).nop().optional().unbounded();
    return builder.getHandlers();
  }

}
