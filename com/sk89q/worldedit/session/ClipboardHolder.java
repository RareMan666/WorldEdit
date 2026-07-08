package com.sk89q.worldedit.session;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.transform.Identity;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.registry.WorldData;

public class ClipboardHolder {
   private final WorldData worldData;
   private final Clipboard clipboard;
   private Transform transform = new Identity();

   public ClipboardHolder(Clipboard clipboard, WorldData worldData) {
      Preconditions.checkNotNull(clipboard);
      Preconditions.checkNotNull(worldData);
      this.clipboard = clipboard;
      this.worldData = worldData;
   }

   public WorldData getWorldData() {
      return this.worldData;
   }

   public Clipboard getClipboard() {
      return this.clipboard;
   }

   public void setTransform(Transform transform) {
      Preconditions.checkNotNull(transform);
      this.transform = transform;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public PasteBuilder createPaste(Extent targetExtent, WorldData targetWorldData) {
      return new PasteBuilder(this, targetExtent, targetWorldData);
   }
}
