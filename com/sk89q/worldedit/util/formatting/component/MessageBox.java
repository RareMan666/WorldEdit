package com.sk89q.worldedit.util.formatting.component;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.util.formatting.Style;
import com.sk89q.worldedit.util.formatting.StyledFragment;

public class MessageBox extends StyledFragment {
   private final StyledFragment contents = new StyledFragment();

   public MessageBox(String title) {
      Preconditions.checkNotNull(title);
      int leftOver = 47 - title.length() - 2;
      int leftSide = (int)Math.floor(leftOver * 1.0 / 3.0);
      int rightSide = (int)Math.floor(leftOver * 2.0 / 3.0);
      if (leftSide > 0) {
         this.createFragment(Style.YELLOW).append(this.createBorder(leftSide));
      }

      this.append(" ");
      this.append(title);
      this.append(" ");
      if (rightSide > 0) {
         this.createFragment(Style.YELLOW).append(this.createBorder(rightSide));
      }

      this.newLine();
      this.append(this.contents);
   }

   private String createBorder(int count) {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < count; i++) {
         builder.append("-");
      }

      return builder.toString();
   }

   public StyledFragment getContents() {
      return this.contents;
   }
}
