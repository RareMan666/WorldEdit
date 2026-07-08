package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.utils.Range;

public class IntegerRepeaterDayPortion extends RepeaterDayPortion<Integer> {
   public IntegerRepeaterDayPortion(Integer type) {
      super(type);
   }

   protected Range createRange(Integer type) {
      return new Range(type * 60 * 60, (type + 12) * 60 * 60);
   }

   @Override
   protected int _getWidth(Range range) {
      return 43200;
   }
}
