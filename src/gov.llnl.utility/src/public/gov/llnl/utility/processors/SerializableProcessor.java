/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.processors;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * This is an annotation processor that verifies that all serializable objects
 * have their serialization version set.
 *
 * It is a common problem when processing files supplied by users that some
 * minor change has resulted in a serialized object with a new id. This requires
 * modifications to load the malformed object. To prevent this from happening we
 * require all objects that are serialized to have a valid version.
 *
 * Only include this processor in projects that require versions in all
 * serialized objects.
 *
 * @author nelson85
 */
@SupportedAnnotationTypes(
        {
          "java.io.Serializable"
        })
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class SerializableProcessor extends AbstractProcessor
{

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv)
  {
    super.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
  {
    Messager mesg = processingEnv.getMessager();
    Elements elems = processingEnv.getElementUtils();
    Types types = processingEnv.getTypeUtils();
    TypeMirror serializable = elems.getTypeElement("java.io.Serializable").asType();
//    mesg.printMessage(Diagnostic.Kind.NOTE, "Process serializable annotations");
    for (Element e : roundEnv.getRootElements())
    {
      // Only applies to concrete objects
      if (e.getModifiers().contains(Modifier.ABSTRACT))
        continue;

      // Must have serialVersionUID set
      boolean serialVersionUID = false;
      if (e.getKind() == ElementKind.CLASS && types.isAssignable(e.asType(), serializable))
      {
        for (Element sub : e.getEnclosedElements())
        {
          if (sub.getSimpleName().toString().equals("serialVersionUID"))
          {
            serialVersionUID = true;
          }
        }
        if (serialVersionUID == false)
        {
          mesg.printMessage(Diagnostic.Kind.ERROR, "serialVersionUID not set on Serializable object", e);
        }
      }
    }

    return false;
  }

}
