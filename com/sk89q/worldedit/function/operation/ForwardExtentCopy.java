package com.sk89q.worldedit.function.operation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.CombinedRegionFunction;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.block.ExtentBlockCopy;
import com.sk89q.worldedit.function.entity.ExtentEntityCopy;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.visitor.EntityVisitor;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.transform.Identity;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.Region;
import java.util.List;

public class ForwardExtentCopy implements Operation {
   private final Extent source;
   private final Extent destination;
   private final Region region;
   private final Vector from;
   private final Vector to;
   private int repetitions = 1;
   private Mask sourceMask = Masks.alwaysTrue();
   private boolean removingEntities;
   private boolean copyingEntities = true;
   private RegionFunction sourceFunction = null;
   private Transform transform = new Identity();
   private Transform currentTransform = null;
   private RegionVisitor lastVisitor;
   private int affected;

   public ForwardExtentCopy(Extent source, Region region, Extent destination, Vector to) {
      this(source, region, region.getMinimumPoint(), destination, to);
   }

   public ForwardExtentCopy(Extent source, Region region, Vector from, Extent destination, Vector to) {
      Preconditions.checkNotNull(source);
      Preconditions.checkNotNull(region);
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(destination);
      Preconditions.checkNotNull(to);
      this.source = source;
      this.destination = destination;
      this.region = region;
      this.from = from;
      this.to = to;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public void setTransform(Transform transform) {
      Preconditions.checkNotNull(transform);
      this.transform = transform;
   }

   public Mask getSourceMask() {
      return this.sourceMask;
   }

   public void setSourceMask(Mask sourceMask) {
      Preconditions.checkNotNull(sourceMask);
      this.sourceMask = sourceMask;
   }

   public RegionFunction getSourceFunction() {
      return this.sourceFunction;
   }

   public void setSourceFunction(RegionFunction function) {
      this.sourceFunction = function;
   }

   public int getRepetitions() {
      return this.repetitions;
   }

   public void setRepetitions(int repetitions) {
      Preconditions.checkArgument(repetitions >= 0, "number of repetitions must be non-negative");
      this.repetitions = repetitions;
   }

   public boolean isCopyingEntities() {
      return this.copyingEntities;
   }

   public void setCopyingEntities(boolean copyingEntities) {
      this.copyingEntities = copyingEntities;
   }

   public boolean isRemovingEntities() {
      return this.removingEntities;
   }

   public void setRemovingEntities(boolean removingEntities) {
      this.removingEntities = removingEntities;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      if (this.lastVisitor != null) {
         this.affected = this.affected + this.lastVisitor.getAffected();
         this.lastVisitor = null;
      }

      if (this.repetitions > 0) {
         this.repetitions--;
         if (this.currentTransform == null) {
            this.currentTransform = this.transform;
         }

         ExtentBlockCopy blockCopy = new ExtentBlockCopy(this.source, this.from, this.destination, this.to, this.currentTransform);
         RegionMaskingFilter filter = new RegionMaskingFilter(this.sourceMask, blockCopy);
         RegionFunction function = (RegionFunction)(this.sourceFunction != null ? new CombinedRegionFunction(filter, this.sourceFunction) : filter);
         RegionVisitor blockVisitor = new RegionVisitor(this.region, function);
         this.lastVisitor = blockVisitor;
         this.currentTransform = this.currentTransform.combine(this.transform);
         if (this.copyingEntities) {
            ExtentEntityCopy entityCopy = new ExtentEntityCopy(this.from, this.destination, this.to, this.currentTransform);
            entityCopy.setRemoving(this.removingEntities);
            List<? extends Entity> entities = this.source.getEntities(this.region);
            EntityVisitor entityVisitor = new EntityVisitor(entities.iterator(), entityCopy);
            return new DelegateOperation(this, new OperationQueue(blockVisitor, entityVisitor));
         } else {
            return new DelegateOperation(this, blockVisitor);
         }
      } else {
         return null;
      }
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
   }
}
