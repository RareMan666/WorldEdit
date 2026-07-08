package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

public interface IHandler {
   Span handle(List<Token> var1, Options var2);
}
