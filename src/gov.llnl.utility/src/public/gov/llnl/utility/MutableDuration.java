/*
 * Copyright 2019, Lawrence Livermore National Security, LLC.
 * All rights reserved
 *
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nelson85
 */
public class MutableDuration implements TemporalAmount, Comparable<TemporalAmount>
{
  static final long NANOS_PER_SECOND = 1000_000_000L;
  static final List<TemporalUnit> UNITS = Arrays.asList(ChronoUnit.SECONDS, ChronoUnit.NANOS);

  long seconds = 0;
  int nanos = 0;
  
  public MutableDuration()
  {}

  MutableDuration(long seconds, int nano)
  {
    this.seconds = seconds;
    this.nanos = nano;
    this.normalize();
  }

  public static MutableDuration of(Duration d)
  {
    return MutableDuration.ofSeconds(d.getSeconds(), d.getNano());
  }

  public static MutableDuration ofSeconds(long seconds, int nano)
  {
    return new MutableDuration(seconds, nano);
  }

  public static MutableDuration ofSeconds(long seconds)
  {
    return new MutableDuration(seconds, 0);
  }

  @Override
  public String toString()
  {
    return Duration.ofSeconds(seconds, this.nanos).toString();
  }

  @Override
  public long get(TemporalUnit unit)
  {
    if (unit == ChronoUnit.SECONDS)
    {
      return seconds;
    }
    else if (unit == ChronoUnit.NANOS)
    {
      return nanos;
    }
    else
    {
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
    }
  }

  @Override
  public List<TemporalUnit> getUnits()
  {
    return UNITS;
  }

  @Override
  public Temporal addTo(Temporal temporal)
  {
    if (seconds != 0)
    {
      temporal = temporal.plus(seconds, ChronoUnit.SECONDS);
    }
    if (nanos != 0)
    {
      temporal = temporal.plus(nanos, ChronoUnit.NANOS);
    }
    return temporal;
  }

  @Override
  public Temporal subtractFrom(Temporal temporal)
  {
    if (seconds != 0)
    {
      temporal = temporal.minus(seconds, ChronoUnit.SECONDS);
    }
    if (nanos != 0)
    {
      temporal = temporal.minus(nanos, ChronoUnit.NANOS);
    }
    return temporal;
  }

  public void addAssign(Duration d)
  {
    this.seconds += d.getSeconds();
    this.nanos += d.getNano();
    normalize();
  }

  public void subtractAssign(Duration d)
  {
    this.seconds -= d.getSeconds();
    this.nanos -= d.getNano();
    normalize();
  }

  private void normalize()
  {
    if (this.nanos < 0 || this.nanos > NANOS_PER_SECOND)
    {
      this.seconds = Math.addExact(seconds, Math.floorDiv(nanos, NANOS_PER_SECOND));
      this.nanos = (int) Math.floorMod(nanos, NANOS_PER_SECOND);
    }
  }

  public Duration toDuration()
  {
    return Duration.ofSeconds(seconds, nanos);
  }

  @Override
  public int compareTo(TemporalAmount o)
  {
    long sec1 = o.get(ChronoUnit.SECONDS);
    long nano1 = o.get(ChronoUnit.NANOS);

    if (sec1 < seconds)
      return 1;
    if (sec1 > seconds)
      return -1;
    if (nano1 < nanos)
      return 1;
    if (nano1 > nanos)
      return -1;
    return 0;
  }

  public void assign(Duration d)
  {
    this.seconds = d.getSeconds();
    this.nanos = d.getNano();
  }

  public boolean isNegative()
  {
    if (this.seconds < 0)
      return true;
    return this.nanos < 0;
  }

  public boolean isZero()
  {
    return this.seconds == 0 && this.nanos == 0;
  }

  public void clear()
  {
    this.seconds = 0;
    this.nanos = 0;
  }

  public long toMillis()
  {
    return this.seconds*1000+this.nanos/1000000;
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (o instanceof Duration)
    {
      Duration o2 = (Duration) o;
      return o2.getSeconds() == this.seconds && o2.getNano() == this.nanos;
    }
    if (o instanceof MutableDuration)
    {
      MutableDuration o2 = (MutableDuration) o;
      return o2.seconds == this.seconds && o2.nanos == this.nanos;
    }
    if (o instanceof TemporalAmount)
    {
      Duration o2 = Duration.from((TemporalAmount)o);
      return o2.getSeconds() == this.seconds && o2.getNano() == this.nanos;
    }
    return false;
  }

}
