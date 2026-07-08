package com.sk89q.worldedit.util.formatting.component;

import com.sk89q.worldedit.util.formatting.Style;

public class CommandListBox extends MessageBox {
   private boolean first = true;

   public CommandListBox(String title) {
      super(title);
   }

   public CommandListBox appendCommand(String alias, String description) {
      if (!this.first) {
         this.getContents().newLine();
      }

      this.getContents().createFragment(Style.YELLOW_DARK).append(alias).append(": ");
      this.getContents().append(description);
      this.first = false;
      return this;
   }
}
