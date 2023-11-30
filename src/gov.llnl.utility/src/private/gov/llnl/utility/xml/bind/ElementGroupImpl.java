/*
 * Copyright 2016, Lawrence Livermore National Security, LLC. 
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility.xml.bind;

import gov.llnl.utility.annotation.Internal;
import gov.llnl.utility.xml.DomBuilder;
import gov.llnl.utility.xml.bind.Reader.Option;
import gov.llnl.utility.xml.bind.Reader.Order;
import java.util.EnumSet;

/**
 *
 * @author nelson85
 */
@Internal
abstract class ElementGroupImpl implements ElementGroup
{
  EnumSet<Option> flags;
  private ElementGroup parent;

  public ElementGroupImpl(ElementGroup parent, EnumSet<Option> flags)
  {
    this.parent = parent;
    this.flags = flags;
  }

  @Override
  public EnumSet<Option> getElementOptions()
  {
    return null;
  }

  /**
   * @return the parent
   */
  @Override
  public ElementGroup getParent()
  {
    return parent;
  }

  public static ElementGroupImpl newInstance(ElementGroupImpl group, Order order, EnumSet<Option> options)
  {
    switch (order)
    {
      case ALL:
        return new AllGroup(group, options);
      case OPTIONS:
        return new OptionsGroup(group, options);
      case SEQUENCE:
        return new SequenceGroup(group, options);
      case CHOICE:
        return new ChoiceGroup(group, options);
      case FREE:
        return new FreeGroup(group, options);
      default:
        throw new RuntimeException("Unknown order " + order);
    }
  }

  @Override
  public DomBuilder createSchemaGroup(DomBuilder type)
  {
    DomBuilder group = createSchemaGroupImpl(type);
    SchemaBuilderUtilities.applyOptions(group, flags);
    return group;
  }

  public abstract DomBuilder createSchemaGroupImpl(DomBuilder type);

  @Internal
  public static class AllGroup extends ElementGroupImpl
  {
    public AllGroup(ElementGroup parent, EnumSet<Option> flags)
    {
      super(parent, flags);
    }

    @Override
    public DomBuilder createSchemaGroupImpl(DomBuilder type)
    {
      return type.element("xs:all");
    }
  }

  @Internal
  public static class OptionsGroup extends ElementGroupImpl
  {
    public OptionsGroup(ElementGroup parent, EnumSet<Option> flags)
    {
      super(parent, flags);
    }

    // Default is OPTIONAL
    @Override
    public EnumSet<Option> getElementOptions()
    {
      return EnumSet.of(Option.OPTIONAL);
    }

    @Override
    public DomBuilder createSchemaGroupImpl(DomBuilder type)
    {
      return type.element("xs:all");
    }
  }

  public static class SequenceGroup extends ElementGroupImpl
  {
    public SequenceGroup(ElementGroup parent, EnumSet<Option> flags)
    {
      super(parent, flags);
    }

    @Override
    public DomBuilder createSchemaGroupImpl(DomBuilder type)
    {
      return type.element("xs:sequence");
    }
  }

  public static class ChoiceGroup extends ElementGroupImpl
  {
    public ChoiceGroup(ElementGroup parent, EnumSet<Option> flags)
    {
      super(parent, flags);
    }

    @Override
    public DomBuilder createSchemaGroupImpl(DomBuilder type)
    {
      return type.element("xs:choice");
    }
  }

  public static class FreeGroup extends ElementGroupImpl
  {
    public FreeGroup(ElementGroup parent, EnumSet<Option> flags)
    {
      super(parent, flags);
    }

    @Override
    public DomBuilder createSchemaGroupImpl(DomBuilder type)
    {
      return type.element("xs:choice")
              .attr("maxOccurs", "unbounded");
    }
  }

}
