package com.sk89q.worldedit.session;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.registry.WorldData;

public class PasteBuilder {
   private final Clipboard clipboard;
   private final WorldData worldData;
   private final Transform transform;
   private final Extent targetExtent;
   private final WorldData targetWorldData;
   private Vector to = new Vector();
   private boolean ignoreAirBlocks;

   PasteBuilder(ClipboardHolder holder, Extent targetExtent, WorldData targetWorldData) {
      Preconditions.checkNotNull(holder);
      Preconditions.checkNotNull(targetExtent);
      Preconditions.checkNotNull(targetWorldData);
      this.clipboard = holder.getClipboard();
      this.worldData = holder.getWorldData();
      this.transform = holder.getTransform();
      this.targetExtent = targetExtent;
      this.targetWorldData = targetWorldData;
   }

   public PasteBuilder to(Vector to) {
      this.to = to;
      return this;
   }

   public PasteBuilder ignoreAirBlocks(boolean ignoreAirBlocks) {
      this.ignoreAirBlocks = ignoreAirBlocks;
      return this;
   }

   public Operation build() {
      BlockTransformExtent extent = new BlockTransformExtent(this.clipboard, this.transform, this.targetWorldData.getBlockRegistry());
      ForwardExtentCopy copy = new ForwardExtentCopy(extent, this.clipboard.getRegion(), this.clipboard.getOrigin(), this.targetExtent, this.to);
      copy.setTransform(this.transform);
      if (this.ignoreAirBlocks) {
         copy.setSourceMask(new ExistingBlockMask(this.clipboard));
      }

      return copy;
   }
}
