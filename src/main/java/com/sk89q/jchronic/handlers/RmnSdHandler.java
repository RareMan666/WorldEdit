package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.repeaters.RepeaterMonthName;
import com.sk89q.jchronic.tags.ScalarDay;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

public class RmnSdHandler extends MDHandler {
   @Override
   public Span handle(List<Token> tokens, Options options) {
      return this.handle(tokens.get(0).getTag(RepeaterMonthName.class), tokens.get(1).getTag(ScalarDay.class), tokens.subList(2, tokens.size()), options);
   }
}
