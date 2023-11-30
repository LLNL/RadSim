/*
 * Copyright 2016, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author nelson85
 */
@SuppressWarnings("override")
public class ClassUtilities
{

  public static boolean isBoxed(Class<?> cls)
  {
    if (cls.equals(Integer.class))
      return true;
    if (cls.equals(Double.class))
      return true;
    if (cls.equals(Float.class))
      return true;
    if (cls.equals(Short.class))
      return true;
    if (cls.equals(Long.class))
      return true;
    if (cls.equals(Byte.class))
      return true;
    if (cls.equals(Character.class))
      return true;
    return false;
  }

  public static Class<?> getPrimitiveType(Class<?> cls)
  {
    try
    {
      if (!isBoxed(cls))
        return null;
      return (Class<?>) cls.getField("TYPE").get(null);
    }
    catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public static Class<?> forNamePrimative(String className)
  {
    if ("double".equals(className))
      return Double.TYPE;
    if ("int".equals(className))
      return Integer.TYPE;
    if ("boolean".equals(className))
      return Boolean.TYPE;
    if ("char".equals(className))
      return Character.TYPE;
    if ("short".equals(className))
      return Short.TYPE;
    if ("long".equals(className))
      return Long.TYPE;

    throw new UnsupportedOperationException("Unknown primitive type " + className);
  }

  public interface ValueOf<Type>
  {
    /**
     * Get the value for the string. This will throw an exception if the
     * conversion is not possible.
     *
     * @param s
     * @return the type extracted from the string, will not be null.
     * @throws IllegalArgumentException if the string is not convertable to the
     * requested type.
     */
    Type valueOf(String s) throws IllegalArgumentException;

    Class getObjectType();
  }

  public interface Primitive<Type> extends ValueOf
  {
    Class getPrimitiveType();

    Class<Type> getBoxedType();

    Type valueOf(String s);

    Type cast(Object boxed) throws ClassCastException;

    default public Class getObjectType()
    {
      return getPrimitiveType();
    }
  }

//<editor-fold desc="primitives" defaultstate="collapsed">
  public static final Primitive<Boolean> BOOLEAN_PRIMITIVE = new BooleanPrimitive();

  static final class BooleanPrimitive implements Primitive<Boolean>
  {
    public Boolean valueOf(String s)
    {
      s = s.trim();
      if(s.equals("0"))
      {
        return false;
      }
      if(s.equals("1"))
      {
        return true;
      }
      return Boolean.valueOf(s);
    }

    public Class<Boolean> getBoxedType()
    {
      return Boolean.class;
    }

    public Class getPrimitiveType()
    {
      return boolean.class;
    }

    @Override
    public Boolean cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Boolean.class)
        return (Boolean) boxed;
      throw new ClassCastException();
    }
  }

  public static final Primitive<Byte> BYTE_PRIMITIVE = new BytePrimitive();

  static final class BytePrimitive implements Primitive<Byte>
  {
    public Byte valueOf(String s)
    {
      return Byte.valueOf(s.trim());
    }

    public Class<Byte> getBoxedType()
    {
      return Byte.class;
    }

    public Class getPrimitiveType()
    {
      return byte.class;
    }

    @Override
    public Byte cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return (Byte) boxed;
      throw new ClassCastException();
    }
  }

  public static final Primitive CHARACTER_PRIMITIVE = new CharacterPrimitive();

  static final class CharacterPrimitive implements Primitive<Character>
  {
    public Character valueOf(String s)
    {
      return s.charAt(0);
    }

    public Class<Character> getBoxedType()
    {
      return Character.class;
    }

    public Class getPrimitiveType()
    {
      return char.class;
    }

    @Override
    public Character cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Character.class)
        return (Character) boxed;
      throw new ClassCastException();
    }
  }

  public static final Primitive SHORT_PRIMITIVE = new ShortPrimitive();

  static final class ShortPrimitive implements Primitive<Short>
  {
    public Short valueOf(String s)
    {
      return Short.valueOf(s.trim());
    }

    public Class<Short> getBoxedType()
    {
      return Short.class;
    }

    public Class getPrimitiveType()
    {
      return short.class;
    }

    @Override
    public Short cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return ((Byte) boxed).shortValue();
      if (cls == Short.class)
        return (Short) boxed;
      throw new ClassCastException();
    }

  };
  public static final Primitive INTEGER_PRIMITIVE = new IntegerPrimitive();

  static final class IntegerPrimitive implements Primitive<Integer>
  {
    public Integer valueOf(String s)
    {
      return Integer.valueOf(s.trim());
    }

    public Class<Integer> getBoxedType()
    {
      return Integer.class;
    }

    public Class getPrimitiveType()
    {
      return int.class;
    }

    @Override
    public Integer cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return ((Byte) boxed).intValue();
      if (cls == Short.class)
        return ((Short) boxed).intValue();
      if (cls == Integer.class)
        return ((Integer) boxed);
      throw new ClassCastException();
    }

  }

  public static final Primitive LONG_PRIMITIVE = new LongPrimitive();

  static final class LongPrimitive implements Primitive<Long>
  {
    public Long valueOf(String s)
    {
      return Long.valueOf(s.trim());
    }

    public Class<Long> getBoxedType()
    {
      return Long.class;
    }

    public Class getPrimitiveType()
    {
      return long.class;
    }

    @Override
    public Long cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return ((Byte) boxed).longValue();
      if (cls == Short.class)
        return ((Short) boxed).longValue();
      if (cls == Integer.class)
        return ((Integer) boxed).longValue();
      if (cls == Long.class)
        return (Long) boxed;
      throw new ClassCastException();
    }
  }

  public static final Primitive FLOAT_PRIMITIVE = new FloatPrimitive();

  static final class FloatPrimitive implements Primitive<Float>
  {
    public Float valueOf(String s)
    {
      return Float.valueOf(s.trim());
    }

    public Class<Float> getBoxedType()
    {
      return Float.class;
    }

    public Class getPrimitiveType()
    {
      return float.class;
    }

    @Override
    public Float cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return ((Byte) boxed).floatValue();
      if (cls == Short.class)
        return ((Short) boxed).floatValue();
      if (cls == Integer.class)
        return ((Integer) boxed).floatValue();
      if (cls == Long.class)
        return ((Long) boxed).floatValue();
      if (cls == Float.class)
        return (Float) boxed;
      throw new ClassCastException();
    }
  }

  public static final Primitive DOUBLE_PRIMITIVE = new DoublePrimitive();

  static final class DoublePrimitive implements Primitive<Double>
  {
    public Double valueOf(String s)
    {
      return Double.valueOf(s.trim());
    }

    public Class<Double> getBoxedType()
    {
      return Double.class;
    }

    public Class getPrimitiveType()
    {
      return double.class;
    }

    @Override
    public Double cast(Object boxed)
    {
      Class<?> cls = boxed.getClass();
      if (cls == Byte.class)
        return ((Byte) boxed).doubleValue();
      if (cls == Short.class)
        return ((Short) boxed).doubleValue();
      if (cls == Integer.class)
        return ((Integer) boxed).doubleValue();
      if (cls == Long.class)
        return ((Long) boxed).doubleValue();
      if (cls == Float.class)
        return ((Float) boxed).doubleValue();
      if (cls == Double.class)
        return (Double) boxed;
      throw new ClassCastException();
    }

  }
  //</editor-fold>

  static final ValueOf STRING_CONVERTER = new ValueOf<String>()
  {
    @Override
    public String valueOf(String s)
    {
      return s;
    }

    @Override
    public Class getObjectType()
    {
      return String.class;
    }
  };

  static final ValueOf INSTANT_CONVERTER = new ValueOf<Instant>()
  {
    @Override
    public Instant valueOf(String s)
    {
      return DateUtilities.convert(s, Instant::from);
    }

    @Override
    public Class getObjectType()
    {
      return Instant.class;
    }

  };

  static final ValueOf UUID_CONVERTER = new ValueOf<UUID>()
  {
    @Override
    public UUID valueOf(String s) throws IllegalArgumentException
    {
      return UUID.fromString(s);
    }

    @Override
    public Class getObjectType()
    {
      return UUID.class;
    }

  };

  static final class EnumConverter<Type extends Enum<Type>> implements ValueOf<Type>
  {
    Class<Type> cls;

    @SuppressWarnings("unchecked")
    EnumConverter(Class cls)
    {
      this.cls = cls;
    }

    @Override
    public Type valueOf(String s)
    {
      return Enum.valueOf(cls, s.toUpperCase());
    }

    @Override
    public Class getObjectType()
    {
      return cls;
    }

  }

  final static Primitive[] PRIMITIVES = new Primitive[]
  {
    BOOLEAN_PRIMITIVE,
    BYTE_PRIMITIVE,
    CHARACTER_PRIMITIVE,
    SHORT_PRIMITIVE,
    INTEGER_PRIMITIVE,
    LONG_PRIMITIVE,
    FLOAT_PRIMITIVE,
    DOUBLE_PRIMITIVE
  };

  public static Primitive getPrimitive(Class c)
  {
    if (c == int.class)
      return PRIMITIVES[4];
    if (c == double.class)
      return PRIMITIVES[7];
    if (c == boolean.class)
      return PRIMITIVES[0];
    if (c == float.class)
      return PRIMITIVES[6];
    if (c == byte.class)
      return PRIMITIVES[1];
    if (c == short.class)
      return PRIMITIVES[3];
    if (c == long.class)
      return PRIMITIVES[5];
    if (c == char.class)
      return PRIMITIVES[2];
    return null;
  }

  public static Primitive getBoxedPrimitive(Class c)
  {
    if (c == Integer.class)
      return PRIMITIVES[4];
    if (c == Double.class)
      return PRIMITIVES[7];
    if (c == Boolean.class)
      return PRIMITIVES[0];
    if (c == Float.class)
      return PRIMITIVES[6];
    if (c == Byte.class)
      return PRIMITIVES[1];
    if (c == Short.class)
      return PRIMITIVES[3];
    if (c == Long.class)
      return PRIMITIVES[5];
    if (c == Character.class)
      return PRIMITIVES[2];
    return null;
  }

  /**
   *
   * @param cls
   * @return null if there is no value converter.
   */
  public static ValueOf newValueOf(Class cls)
  {
    if (cls.isPrimitive())
      return getPrimitive(cls);
    if (cls == String.class)
      return STRING_CONVERTER;
    if (cls.isEnum())
      return new EnumConverter(cls);
    if (cls == UUID.class)
      return UUID_CONVERTER;
    if (cls == Date.class)
      throw new UnsupportedOperationException("Date was replaced by Instant");
    if (cls == Instant.class)
      return INSTANT_CONVERTER;
    if (Number.class.isAssignableFrom(cls))
      return getBoxedPrimitive(cls);
    if (cls == Boolean.class)
      return getBoxedPrimitive(cls);
    return null;
  }

  public static boolean isInnerClass(Class<?> cls)
  {
    return cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers());
  }
}
