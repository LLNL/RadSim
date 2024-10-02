/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.processors;

import gov.llnl.utility.io.ReaderException;
import gov.llnl.utility.xml.bind.ObjectReader;
import gov.llnl.utility.xml.bind.Reader;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.ReaderContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 * @author nelson85
 */
@SupportedAnnotationTypes("gov.llnl.utility.xml.bind.Reader.Declaration")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ReaderProcessor extends AbstractProcessor
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
    mesg.printMessage(Diagnostic.Kind.NOTE, "Process reader annotations");

    // Get a forward declaration for the reader class.
    TypeMirror readerClass = types.erasure(elems.getTypeElement(Reader.class.getTypeName()).asType());

    // Verify that all concrete readers are properly annotated.
    for (Element elem : roundEnv.getRootElements())
    {

      // Skip any readers that are abstract
      if (elem.getModifiers().contains(Modifier.ABSTRACT))
        continue;

      // Skip anything which is not a class
      if (elem.getKind() != ElementKind.CLASS)
        continue;

      // Skip declarations on non-reader types as those are handled seperately
      if (!types.isAssignable(elem.asType(), readerClass))
        continue;

      // Check to make sure it has a declaration
      Reader.Declaration decl = elem.getAnnotation(Reader.Declaration.class);
      if (decl == null)
      {
        mesg.printMessage(Diagnostic.Kind.ERROR, "Readers must have Reader.Declaration", elem);
      }

      // Make sure that either cls is declared or getObjectClass is defined
      TypeMirror cls = getClassAnnotation(elem, Reader.Declaration.class, "cls");
      Map<String, ExecutableElement> methods = getMethods((TypeElement) elem);
      if (cls == null && methods.get("getObjectClass") == null)
      {
        mesg.printMessage(Diagnostic.Kind.ERROR, "Readers should have class defined", elem);
      }
      else if (cls != null && methods.get("getObjectClass") != null)
      {
        mesg.printMessage(Diagnostic.Kind.ERROR, "Remove old getObjectClass", methods.get("getObjectClass"));
      }

      // Check to see that all members are final.
      Map<String, VariableElement> fields = getFields((TypeElement) elem);
      for (Map.Entry<String, VariableElement> field : fields.entrySet())
      {
        System.out.println("  Field " + field.getValue());
        if (!field.getValue().getModifiers().contains(Modifier.FINAL))
        {
          mesg.printMessage(Diagnostic.Kind.ERROR, "Field " + field.getKey() + " is not final", field.getValue());
        }
      }

    }

    // Process all reader declarations.
    for (Element elem : roundEnv.getElementsAnnotatedWith(Reader.Declaration.class))
    {
      // Skip
      if (types.isAssignable(elem.asType(), readerClass))
      {
        check(elem);
      }
      else
      {
        buildCode(elem);
      }
    }
    return true;
  }

//<editor-fold desc="internal" defaultstate="collapsed">
  Map<String, ExecutableElement> getMethods(TypeElement type)
  {
    Map<String, ExecutableElement> out = new HashMap<>();
    for (Element e : type.getEnclosedElements())
    {
      if (!(e instanceof ExecutableElement))
        continue;
      ExecutableElement ee = (ExecutableElement) e;
      out.put(ee.getSimpleName().toString(), ee);
    }
    return out;
  }

  Map<String, VariableElement> getFields(TypeElement type)
  {
    Map<String, VariableElement> out = new HashMap<>();
    for (Element e : type.getEnclosedElements())
    {
      if (!(e instanceof VariableElement))
        continue;
      VariableElement ee = (VariableElement) e;
      out.put(ee.getSimpleName().toString(), ee);
    }
    return out;
  }

  private void check(Element element)
  {
    TypeElement typeElement = (TypeElement) element;

    Types types = processingEnv.getTypeUtils();
    Elements elems = processingEnv.getElementUtils();
    Messager mesg = processingEnv.getMessager();

    Reader.Declaration decl = element.getAnnotation(Reader.Declaration.class);
    TypeMirror schema = getClassAnnotation(element, Reader.Declaration.class, "pkg");

    TypeMirror readerClass = types.erasure(elems.getTypeElement(ObjectReader.class.getTypeName()).asType());

    // Prevent the landmine that the cls in the declaration and the class of the reader are mismatched.
    {
      DeclaredType t = (DeclaredType) typeElement.asType();
      DeclaredType t0 = (DeclaredType) typeElement.asType();
      while (t != null && !types.isAssignable(readerClass, t))
      {
        t = (DeclaredType) ((TypeElement) types.asElement(t)).getSuperclass();
        // Derived types like ObjectStringReader<>
        if(!t.getTypeArguments().isEmpty())
          break;
      }
      TypeMirror t2 = ((DeclaredType) t).getTypeArguments().get(0);
      TypeMirror t3 = getClassAnnotation(element, Reader.Declaration.class, "cls");
      if (!types.isAssignable(t3, t2))
        mesg.printMessage(Diagnostic.Kind.ERROR,
                String.format("cls and template type mismatch at %s (%s,%s,%s,%s)", typeElement, t0, t, t2, t3), typeElement);
    }

    TypeElement readerElement = (TypeElement) types.asElement(readerClass);
    Map<String, ExecutableElement> methods = getMethods(readerElement);
    ExecutableElement startMethod = methods.get("start");
    ExecutableElement endMethod = methods.get("end");
    ExecutableElement contentsMethod = methods.get("contents");
    ExecutableElement handlersMethod = methods.get("getHandlers");

    if (decl.referenceable() && Option.check(decl.options(), Option.CONTENT_REQUIRED))
    {
      mesg.printMessage(Diagnostic.Kind.ERROR, "referenceable elements can not require content", element);
    }

    if (decl.copyable() && !decl.referenceable())
    {
      mesg.printMessage(Diagnostic.Kind.ERROR, "Copiable elements must be referenceable", element);
    }

    // This is a restrictions we need to have to create newInstance.  If a class is
    // not public it may not have an accessable public constructor.
    if (!element.getModifiers().contains(Modifier.PUBLIC))
    {
      mesg.printMessage(Diagnostic.Kind.ERROR, "Readers must be public", element);
    }

    // Check that contents is implemented if the contexts are TEXT or MIXED
    // Check that start or end is implemented if contents are ELEMENTS
    boolean hasStart = false;
    boolean hasEnd = false;
    boolean hasContents = false;
    boolean hasHandlers = false;
    TypeElement root = typeElement;
    while (root != null)
    {
      for (ExecutableElement sub : ElementFilter.methodsIn(root.getEnclosedElements()))
      {
        if (elems.overrides(sub, startMethod, readerElement))
        {
          hasStart = true;
        }

        if (elems.overrides(sub, endMethod, readerElement))
        {
          hasEnd = true;
        }
        if (elems.overrides(sub, handlersMethod, readerElement))
        {
          hasHandlers = true;
        }
        if (elems.overrides(sub, contentsMethod, readerElement))
        {
          hasContents = true;
        }
      }

      TypeMirror sc = root.getSuperclass();
      if (types.erasure(sc).equals(readerClass))
        break;

      root = (TypeElement) types.asElement(sc);
    }

    switch (decl.contents())
    {
      case ELEMENTS:
        if (!hasStart && !hasEnd)
          mesg.printMessage(Diagnostic.Kind.ERROR, "Elements must have a start or end that returns non-null", typeElement);
        if (!hasHandlers)
          mesg.printMessage(Diagnostic.Kind.ERROR, "getHandlers is required for ELEMENT contents", typeElement);
        break;

      case TEXT:
      case MIXED:
        if (!hasContents)
          mesg.printMessage(Diagnostic.Kind.ERROR, "contents is required for TEXT contents", typeElement);
        break;

      case NONE:
        break;
    }

  }

  private void buildCode(Element element)
  {

    TypeElement typeElement = (TypeElement) element;

    // Access elements of the processing environment
    Types types = processingEnv.getTypeUtils();
    Elements elems = processingEnv.getElementUtils();
    Messager mesg = processingEnv.getMessager();
    Filer filer = processingEnv.getFiler();
    try
    {

      Reader.Declaration decl = typeElement.getAnnotation(Reader.Declaration.class);
      TypeMirror schema = getClassAnnotation(typeElement, Reader.Declaration.class, "pkg");
      String pkg = elems.getPackageOf(typeElement).getQualifiedName().toString();
      String typeName = elems.getBinaryName(typeElement).toString();
      typeName = typeName.substring(pkg.length() + 1);
      String readerName = typeName + "$$Reader";
      String declName = typeName.replaceAll("\\$", ".");

      // Find all annotations
      ContentsDecl contents = null;
      List<ElementDecl> elements = new ArrayList<>();
      List<AttributeDecl> attributes = new ArrayList<>();
      for (ExecutableElement sub : ElementFilter.methodsIn(typeElement.getEnclosedElements()))
      {

        Reader.Attribute attrAnnotation = sub.getAnnotation(Reader.Attribute.class);
        if (attrAnnotation != null)
          attributes.add(new AttributeDecl(sub, attrAnnotation));

        Reader.TextContents contentsAnnotation = sub.getAnnotation(Reader.TextContents.class);
        if (contentsAnnotation != null)
          contents = new ContentsDecl(sub, contentsAnnotation);

        Reader.Element elemAnnotation = sub.getAnnotation(Reader.Element.class);
        if (elemAnnotation != null)
        {
          elements.add(new ElementDecl(sub, elemAnnotation));
          if (elemAnnotation.name().isEmpty())
          {
            mesg.printMessage(Diagnostic.Kind.ERROR, "Element name is null", sub);
          }
        }
      }

      if (contents != null && !elements.isEmpty())
      {
        mesg.printMessage(Diagnostic.Kind.ERROR, "Mixed contents are not allowed", contents.method);
        return;
      }

      JavaFileObject builderObject = filer.createSourceFile(pkg + "." + readerName, typeElement);

      String implName = declName;
      TypeMirror impl = getClassAnnotation(typeElement, Reader.Declaration.class, "impl");
      if (impl != null)
      {
        implName = impl.toString();
      }

      try ( PrintWriter writer = new PrintWriter(builderObject.openWriter()))
      {

        writer.append("package ").append(pkg).append(";\n");
//      writer.println("import " + decl.pkg().getTypeName() + ";");
        writer.println("import " + ReaderException.class.getTypeName() + ";");
        writer.println("import " + Reader.class.getTypeName() + ";");
        writer.println("import " + ObjectReader.class.getTypeName() + ";");
        writer.println("import " + ReaderContext.class.getTypeName() + ";");

        writer.println();
        writer.println("@Reader.Declaration(");
        writer.println("  pkg = " + schema.toString() + ".class, ");
        writer.println("  name = \"" + decl.name() + "\",");
        writer.println("  cls = " + declName + ".class,");
        writer.println("  referenceable = " + decl.referenceable() + ",");
        if (decl.options().length > 0)
          mesg.printMessage(Diagnostic.Kind.ERROR, "Need to implement options", element);
        if (contents != null)
          writer.println("  contents = Reader.Contents.TEXT,");
        else
        {
          writer.println("  contents = Reader.Contents.ELEMENTS,");
          writer.println("  order = Reader.Order." + decl.order().toString() + ",");
        }
        writer.println("  document = " + decl.document() + ")");

        for (AttributeDecl attr : attributes)
        {
          writer.print("@Reader.Attribute(name=\"" + attr.name + "\", ");
          writer.print("type=" + attr.type.toString() + ".class");
          if (attr.annotation.required())
            writer.print(", required=true");
          writer.println(")");
        }
        writer.println("public class " + readerName + " extends ObjectReader<" + declName + ">");
        writer.println("{");

        // Start
        writer.println("  @Override");
        writer.println("  public " + declName + " start(ReaderContext context, org.xml.sax.Attributes attributes) throws ReaderException");
        writer.println("  {");
        writer.println("    String attributeValue;");
        writer.println("    " + implName + " out = new " + implName + "();");
        for (AttributeDecl attr : attributes)
        {
          writer.println("    attributeValue = attributes.getValue(\"" + attr.name + "\");");
          writer.println("    if (attributeValue!=null)");
          writer.println("       out." + attr.method.getSimpleName()
                  + "((" + attr.type + ") gov.llnl.utility.ClassUtilities.newValueOf(" + attr.type + ".class).valueOf(attributeValue));");
        }
        writer.println("    return out;");
        writer.println("  }");
        writer.println();

        // Handlers
        writer.println("  @Override");
        writer.println("  public ElementHandlerMap getHandlers(ReaderContext context) throws ReaderException");
        writer.println("  {");
        if (contents != null || elements.isEmpty())
        {
          writer.println("    return null;");
        }
        else
        {
          writer.println("    ReaderBuilder<" + declName + "> builder = this.newBuilder();");
          for (ElementDecl e : elements)
          {
            String contentType = e.method.getParameters().get(0).asType().toString();
            if (e.annotation.any() == true)
            {
              writer.println("    builder.any(" + contentType + ".class)");
              writer.println("      .call((java.util.function.BiConsumer<" + declName + ", " + contentType + ">)" + declName + "::" + e.method.getSimpleName() + ")");
            }
            else
            {
              writer.println("    builder.element(\"" + e.annotation.name() + "\")");
              writer.println("      .contents(" + contentType + ".class)");
              writer.println("      .call(" + declName + "::" + e.method.getSimpleName() + ")");
            }
            if (e.annotation.required())
              writer.println("      .required()");
            if (e.annotation.unbounded())
              writer.println("      .unbounded()");
            if (e.annotation.deferrable())
              writer.println("      .deferrable()");
            writer.println("       ;");
          }
          writer.println("    return builder.getHandlers();");
        }
        writer.println("  }");
        writer.println();

        if (contents != null)
        {
          writer.println("  public " + declName + " contents(ReaderContext context, String textContents) throws ReaderException");
          writer.println("  {");
          VariableElement param0 = contents.method.getParameters().get(0);
          String methodName = contents.method.getSimpleName().toString();
          if (param0.asType().toString().equals("java.lang.String"))
            writer.println("   getObject(context)." + methodName + "(textContents);");
          else
          {
            writer.println("   getObject(context)." + methodName
                    + "((" + param0.asType().toString() + ") gov.llnl.utility.ClassUtilities.newValueOf(" + param0.asType().toString() + ".class).valueOf(textContents));");
          }
          writer.println("    return null;");
          writer.println("  }");
          writer.println();
        }

        writer.println("}");
      }
    }
    catch (FilerException ex)
    {
      mesg.printMessage(Diagnostic.Kind.NOTE, "Skipping reader");
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }

  }

  private TypeMirror getClassAnnotation(Element elem, Class annotation, String value)
  {
    String annotationName = annotation.getCanonicalName();
    Elements elems = processingEnv.getElementUtils();

    TypeMirror type1 = elems.getTypeElement(annotationName).asType();
    for (AnnotationMirror ann : elem.getAnnotationMirrors())
    {
      if (!ann.getAnnotationType().equals(type1))
        continue;
      for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : ann.getElementValues().entrySet())
      {
        if (!entry.getKey().getSimpleName().toString().equals(value))
          continue;
        Object out = entry.getValue().getValue();
        return (TypeMirror) out;
      }
    }
    return null;
  }

  private class ElementDecl
  {
    private final Reader.Element annotation;
    private final ExecutableElement method;

    public ElementDecl(ExecutableElement method, Reader.Element annotation)
    {
      Messager mesg = processingEnv.getMessager();
      this.method = method;
      this.annotation = annotation;

      // Check requirements
      if (method.getParameters().size() != 1)
        mesg.printMessage(Diagnostic.Kind.ERROR, "Elements must take only one parameter", method);
      if (!method.getThrownTypes().isEmpty())
        mesg.printMessage(Diagnostic.Kind.ERROR, "Elements do not support exceptions", method);
    }
  }

  private class AttributeDecl
  {
    final ExecutableElement method;
    final Reader.Attribute annotation;
    TypeMirror type;
    String name;

    public AttributeDecl(ExecutableElement method, Reader.Attribute annotation)
    {
      Types types = processingEnv.getTypeUtils();
      Messager mesg = processingEnv.getMessager();
      this.method = method;
      this.annotation = annotation;

      // Compute required fields
      this.type = getClassAnnotation(method, Reader.Attribute.class, "type");
      if (this.type == null)
      {
        // Use the first argument to get the type
        this.type = method.getParameters().get(0).asType();

        // Watch out for primitives as we need to be boxed.
        if (this.type.getKind().isPrimitive())
        {
          this.type = types.boxedClass((PrimitiveType) this.type).asType();
        }
      }
      this.name = annotation.name();

      // Check requirements
      if (method.getParameters().size() != 1)
        mesg.printMessage(Diagnostic.Kind.ERROR, "Attributes must take only one parameter", method);
      if (!method.getThrownTypes().isEmpty())
        mesg.printMessage(Diagnostic.Kind.ERROR, "Attributes do not support exceptions", method);

      // Handle automatic naming of fields
      if (name.equals("##null"))
      {
        this.name = method.getSimpleName().toString();
        if (!this.name.startsWith(name))
          mesg.printMessage(Diagnostic.Kind.ERROR, "Setter must start with set", method);
        if (this.name.length() == 3)
          mesg.printMessage(Diagnostic.Kind.ERROR, "Setter must have a field", method);
        this.name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
      }
    }
  }

  private class ContentsDecl
  {
    ExecutableElement method;
    Reader.TextContents annotation;

    private ContentsDecl(ExecutableElement method, Reader.TextContents contents)
    {
      Messager mesg = processingEnv.getMessager();
      this.method = method;
      this.annotation = contents;

      // Check requirements
      if (method.getParameters().size() != 1)
        mesg.printMessage(Diagnostic.Kind.ERROR, "Contents must take only one parameter", method);
      if (!method.getThrownTypes().isEmpty())
        mesg.printMessage(Diagnostic.Kind.ERROR, "Contents do not support exceptions", method);

    }
  }

//</editor-fold>
}
