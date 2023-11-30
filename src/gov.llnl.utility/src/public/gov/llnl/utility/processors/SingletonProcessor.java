/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.processors;

import gov.llnl.utility.annotation.Singleton;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

/**
 *
 * @author nelson85
 */
@SupportedAnnotationTypes(
        {
          "gov.llnl.utility.annotation.Singleton"
        })
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class SingletonProcessor extends AbstractProcessor
{

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv)
  {
    super.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
  {
    Types types = processingEnv.getTypeUtils();
    Elements elems = processingEnv.getElementUtils();
    Messager mesg = processingEnv.getMessager();

//    mesg.printMessage(Diagnostic.Kind.NOTE, "Process singleton annotations");
//    TypeMirror required = elems.getTypeElement(Singletons.SingletonRequired.class.getTypeName()).asType();
//    for (Element e : roundEnv.getRootElements())
//    {
//      mesg.printMessage(Kind.NOTE, "Test " + e);
//      if (e.getModifiers().contains(Modifier.ABSTRACT))
//        continue;
//      if (!types.isAssignable(required, e.asType()))
//        continue;
//      if (e.getAnnotation(Singleton.class) == null)
//        mesg.printMessage(Kind.ERROR, "Singleton is required " + e.toString());
//    }
    for (Element e : roundEnv.getElementsAnnotatedWith(Singleton.class))
    {
      boolean accessor = false;
      boolean instance = false;
      Set<Modifier> mod = e.getModifiers();
      if (!mod.contains(Modifier.FINAL) && !mod.contains(Modifier.ABSTRACT))
        mesg.printMessage(Kind.ERROR, "Singleton classes must be final.", e);
      for (Element sub : e.getEnclosedElements())
      {
        if (sub.getSimpleName().toString().equals("INSTANCE"))
        {
          instance = true;
          Set<Modifier> smod = sub.getModifiers();
          if (!smod.contains(Modifier.FINAL) || !smod.contains(Modifier.PRIVATE) || !smod.contains(Modifier.STATIC))
            mesg.printMessage(Kind.ERROR, "Singleton INSTANCE must be private, static, and final.", sub);
        }

        if (sub.getSimpleName().toString().equals("getInstance"))
        {
          accessor = true;
          Set<Modifier> smod = sub.getModifiers();
          if (!smod.contains(Modifier.STATIC) || !smod.contains(Modifier.PUBLIC))
            mesg.printMessage(Kind.ERROR, "Singleton getInstance() must be public and static.", sub);
        }
        ElementKind kd = sub.getKind();
        if (kd == ElementKind.CONSTRUCTOR && sub.getModifiers().contains(Modifier.PUBLIC))
          mesg.printMessage(Kind.ERROR, "Singleton must not contain public constructor", sub);
      }

      if (!accessor)
        mesg.printMessage(Kind.ERROR, "Singleton must have getInstance()", e);
      if (!instance)
        mesg.printMessage(Kind.ERROR, "Singleton must contain INSTANCE", e);
    }
    return true;
  }

}
