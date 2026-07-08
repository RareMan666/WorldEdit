package com.sk89q.worldedit.util.formatting;

import java.util.ArrayList;
import java.util.List;

public class StyledFragment extends Fragment {
   private final List<Fragment> children = new ArrayList<>();
   private StyleSet style;
   private Fragment lastText;

   public StyledFragment() {
      this.style = new StyleSet();
   }

   public StyledFragment(StyleSet style) {
      this.style = style;
   }

   public StyledFragment(Style... styles) {
      this.style = new StyleSet(styles);
   }

   public StyleSet getStyle() {
      return this.style;
   }

   public void setStyles(StyleSet style) {
      this.style = style;
   }

   public List<Fragment> getChildren() {
      return this.children;
   }

   protected Fragment lastText() {
      if (!this.children.isEmpty()) {
         Fragment text = this.children.get(this.children.size() - 1);
         if (text == this.lastText) {
            return text;
         }
      }

      Fragment text = new Fragment();
      this.lastText = text;
      this.children.add(text);
      return text;
   }

   public StyledFragment createFragment(Style... styles) {
      StyledFragment fragment = new StyledFragment(styles);
      this.append(fragment);
      return fragment;
   }

   public StyledFragment append(StyledFragment fragment) {
      this.children.add(fragment);
      return this;
   }

   public StyledFragment append(String str) {
      this.lastText().append(str);
      return this;
   }

   public StyledFragment append(Object obj) {
      this.append(String.valueOf(obj));
      return this;
   }

   public StyledFragment append(StringBuffer sb) {
      this.append(String.valueOf(sb));
      return this;
   }

   public StyledFragment append(CharSequence s) {
      this.append(String.valueOf(s));
      return this;
   }

   public StyledFragment append(boolean b) {
      this.append(String.valueOf(b));
      return this;
   }

   public StyledFragment append(char c) {
      this.append(String.valueOf(c));
      return this;
   }

   public StyledFragment append(int i) {
      this.append(String.valueOf(i));
      return this;
   }

   public StyledFragment append(long lng) {
      this.append(String.valueOf(lng));
      return this;
   }

   public StyledFragment append(float f) {
      this.append(String.valueOf(f));
      return this;
   }

   public StyledFragment append(double d) {
      this.append(String.valueOf(d));
      return this;
   }

   public StyledFragment newLine() {
      this.append("\n");
      return this;
   }
}
