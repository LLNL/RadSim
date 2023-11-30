/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.io;

import gov.llnl.utility.ClassUtilities;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 *
 * @author nelson85
 */
public class FieldProperties implements PropertyInterface
{
  Object parent;

  @Retention(RetentionPolicy.RUNTIME)
  public @interface Property
  {
    String name() default "";

    boolean set() default true;

    boolean get() default true;
  }

  public FieldProperties()
  {
    this.parent = this;
  }

  public FieldProperties(Object parent)
  {
    this.parent = parent;
  }

  private Field findProperty(String key)
  {
    Field[] fields = parent.getClass().getDeclaredFields();
    for (Field f : fields)
    {
      Property annotation = f.getAnnotation(Property.class);
      if (annotation == null)
        continue;
      if (annotation.name().equals("") && f.getName().equals(key))
        return f;
      if (annotation.name().equals(key))
        return f;
    }
    return null;
  }

  public void setProperty(String key, Object property) throws PropertyException
  {
    Field field = findProperty(key);
    if (field == null)
      throw new PropertyException("Unable to find property " + key);

    if (field.getType().isPrimitive())
    {
      if (property == null)
        throw new PropertyException("Primitives cannot be assigned to null");
      ClassUtilities.Primitive prim = ClassUtilities.getPrimitive(field.getType());
      property = prim.cast(property);
    }
    else
    {
      if (!field.getType().isAssignableFrom(property.getClass()))
        throw new PropertyException(
                String.format("Property %s is incorrect type, expected %s, got %s",
                        key, field.getType(), property.getClass()));
    }

    try
    {
      Property annotation = field.getAnnotation(Property.class);
      if (annotation == null || annotation.set() == false)
        throw new PropertyException(String.format("Property %s is not writable", key));
      field.setAccessible(true);
      field.set(parent, property);
    }
    catch (SecurityException | IllegalArgumentException | IllegalAccessException ex)
    {
      throw new PropertyException("Unable to set property " + key);
    }
  }

  public Object getProperty(String key) throws PropertyException
  {
    try
    {
      Field field = parent.getClass().getDeclaredField(key);
      Property annotation = field.getAnnotation(Property.class);
      if (annotation == null || annotation.get() == false)
        throw new PropertyException(String.format("Property %s is not readable", key));
      field.setAccessible(true);
      return field.get(parent);
    }
    catch (NoSuchFieldException | SecurityException ex)
    {
      throw new PropertyException("Unable to find property " + key);
    }
    catch (IllegalArgumentException | IllegalAccessException ex)
    {
      throw new PropertyException("Unable to get property " + key);
    }
  }

}
