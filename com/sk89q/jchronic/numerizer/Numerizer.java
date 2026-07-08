package com.sk89q.jchronic.numerizer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numerizer {
   protected static Numerizer.DirectNum[] DIRECT_NUMS;
   protected static Numerizer.TenPrefix[] TEN_PREFIXES;
   protected static Numerizer.BigPrefix[] BIG_PREFIXES;
   private static final Pattern DEHYPHENATOR = Pattern.compile(" +|(\\D)-(\\D)");
   private static final Pattern DEHALFER = Pattern.compile("a half", 2);
   private static final Pattern DEHAALFER = Pattern.compile("(\\d+)(?: | and |-)*haAlf", 2);
   private static final Pattern ANDITION_PATTERN = Pattern.compile("(\\d+)( | and )(\\d+)(?=\\W|$)");

   static {
      List<Numerizer.DirectNum> directNums = new LinkedList<>();
      directNums.add(new Numerizer.DirectNum("eleven", "11"));
      directNums.add(new Numerizer.DirectNum("twelve", "12"));
      directNums.add(new Numerizer.DirectNum("thirteen", "13"));
      directNums.add(new Numerizer.DirectNum("fourteen", "14"));
      directNums.add(new Numerizer.DirectNum("fifteen", "15"));
      directNums.add(new Numerizer.DirectNum("sixteen", "16"));
      directNums.add(new Numerizer.DirectNum("seventeen", "17"));
      directNums.add(new Numerizer.DirectNum("eighteen", "18"));
      directNums.add(new Numerizer.DirectNum("nineteen", "19"));
      directNums.add(new Numerizer.DirectNum("ninteen", "19"));
      directNums.add(new Numerizer.DirectNum("zero", "0"));
      directNums.add(new Numerizer.DirectNum("one", "1"));
      directNums.add(new Numerizer.DirectNum("two", "2"));
      directNums.add(new Numerizer.DirectNum("three", "3"));
      directNums.add(new Numerizer.DirectNum("four(\\W|$)", "4$1"));
      directNums.add(new Numerizer.DirectNum("five", "5"));
      directNums.add(new Numerizer.DirectNum("six(\\W|$)", "6$1"));
      directNums.add(new Numerizer.DirectNum("seven(\\W|$)", "7$1"));
      directNums.add(new Numerizer.DirectNum("eight(\\W|$)", "8$1"));
      directNums.add(new Numerizer.DirectNum("nine(\\W|$)", "9$1"));
      directNums.add(new Numerizer.DirectNum("ten", "10"));
      directNums.add(new Numerizer.DirectNum("\\ba\\b", "1"));
      DIRECT_NUMS = directNums.toArray(new Numerizer.DirectNum[directNums.size()]);
      List<Numerizer.TenPrefix> tenPrefixes = new LinkedList<>();
      tenPrefixes.add(new Numerizer.TenPrefix("twenty", 20L));
      tenPrefixes.add(new Numerizer.TenPrefix("thirty", 30L));
      tenPrefixes.add(new Numerizer.TenPrefix("fourty", 40L));
      tenPrefixes.add(new Numerizer.TenPrefix("fifty", 50L));
      tenPrefixes.add(new Numerizer.TenPrefix("sixty", 60L));
      tenPrefixes.add(new Numerizer.TenPrefix("seventy", 70L));
      tenPrefixes.add(new Numerizer.TenPrefix("eighty", 80L));
      tenPrefixes.add(new Numerizer.TenPrefix("ninety", 90L));
      tenPrefixes.add(new Numerizer.TenPrefix("ninty", 90L));
      TEN_PREFIXES = tenPrefixes.toArray(new Numerizer.TenPrefix[tenPrefixes.size()]);
      List<Numerizer.BigPrefix> bigPrefixes = new LinkedList<>();
      bigPrefixes.add(new Numerizer.BigPrefix("hundred", 100L));
      bigPrefixes.add(new Numerizer.BigPrefix("thousand", 1000L));
      bigPrefixes.add(new Numerizer.BigPrefix("million", 1000000L));
      bigPrefixes.add(new Numerizer.BigPrefix("billion", 1000000000L));
      bigPrefixes.add(new Numerizer.BigPrefix("trillion", 1000000000000L));
      BIG_PREFIXES = bigPrefixes.toArray(new Numerizer.BigPrefix[bigPrefixes.size()]);
   }

   public static String numerize(String str) {
      String numerizedStr = DEHYPHENATOR.matcher(str).replaceAll("$1 $2");
      numerizedStr = DEHALFER.matcher(numerizedStr).replaceAll("haAlf");

      for (Numerizer.DirectNum dn : DIRECT_NUMS) {
         numerizedStr = dn.getName().matcher(numerizedStr).replaceAll(dn.getNumber());
      }

      for (Numerizer.Prefix tp : TEN_PREFIXES) {
         Matcher matcher = tp.getName().matcher(numerizedStr);
         if (matcher.find()) {
            StringBuffer matcherBuffer = new StringBuffer();

            do {
               if (matcher.group(1) == null) {
                  matcher.appendReplacement(matcherBuffer, String.valueOf(tp.getNumber()));
               } else {
                  matcher.appendReplacement(matcherBuffer, String.valueOf(tp.getNumber() + Long.parseLong(matcher.group(1).trim())));
               }
            } while (matcher.find());

            matcher.appendTail(matcherBuffer);
            numerizedStr = matcherBuffer.toString();
         }
      }

      for (Numerizer.Prefix bp : BIG_PREFIXES) {
         Matcher matcher = bp.getName().matcher(numerizedStr);
         if (matcher.find()) {
            StringBuffer matcherBuffer = new StringBuffer();

            do {
               if (matcher.group(1) == null) {
                  matcher.appendReplacement(matcherBuffer, String.valueOf(bp.getNumber()));
               } else {
                  matcher.appendReplacement(matcherBuffer, String.valueOf(bp.getNumber() * Long.parseLong(matcher.group(1).trim())));
               }
            } while (matcher.find());

            matcher.appendTail(matcherBuffer);
            numerizedStr = matcherBuffer.toString();
            numerizedStr = andition(numerizedStr);
         }
      }

      Matcher matcher = DEHAALFER.matcher(numerizedStr);
      if (matcher.find()) {
         StringBuffer matcherBuffer = new StringBuffer();

         do {
            matcher.appendReplacement(matcherBuffer, String.valueOf(Float.parseFloat(matcher.group(1).trim()) + 0.5F));
         } while (matcher.find());

         matcher.appendTail(matcherBuffer);
         numerizedStr = matcherBuffer.toString();
      }

      return numerizedStr;
   }

   public static String andition(String str) {
      StringBuffer anditionStr = new StringBuffer(str);
      Matcher matcher = ANDITION_PATTERN.matcher(anditionStr);

      while (matcher.find()) {
         if (matcher.group(2).equalsIgnoreCase(" and ") || matcher.group(1).length() > matcher.group(3).length()) {
            anditionStr.replace(
               matcher.start(), matcher.end(), String.valueOf(Integer.parseInt(matcher.group(1).trim()) + Integer.parseInt(matcher.group(3).trim()))
            );
            matcher = ANDITION_PATTERN.matcher(anditionStr);
         }
      }

      return anditionStr.toString();
   }

   protected static class BigPrefix extends Numerizer.Prefix {
      public BigPrefix(String name, long number) {
         super(Pattern.compile("(\\d*) *" + name, 2), number);
      }
   }

   protected static class DirectNum {
      private Pattern _name;
      private String _number;

      public DirectNum(String name, String number) {
         this._name = Pattern.compile(name, 2);
         this._number = number;
      }

      public Pattern getName() {
         return this._name;
      }

      public String getNumber() {
         return this._number;
      }
   }

   protected static class Prefix {
      private Pattern _name;
      private long _number;

      public Prefix(Pattern name, long number) {
         this._name = name;
         this._number = number;
      }

      public Pattern getName() {
         return this._name;
      }

      public long getNumber() {
         return this._number;
      }
   }

   protected static class TenPrefix extends Numerizer.Prefix {
      public TenPrefix(String name, long number) {
         super(Pattern.compile("(?:" + name + ")( *\\d(?=\\D|$))*", 2), number);
      }
   }
}
