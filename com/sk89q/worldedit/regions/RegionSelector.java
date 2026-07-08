package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.List;
import javax.annotation.Nullable;

public interface RegionSelector {
   @Nullable
   World getWorld();

   void setWorld(@Nullable World var1);

   boolean selectPrimary(Vector var1, SelectorLimits var2);

   boolean selectSecondary(Vector var1, SelectorLimits var2);

   void explainPrimarySelection(Actor var1, LocalSession var2, Vector var3);

   void explainSecondarySelection(Actor var1, LocalSession var2, Vector var3);

   void explainRegionAdjust(Actor var1, LocalSession var2);

   BlockVector getPrimaryPosition() throws IncompleteRegionException;

   Region getRegion() throws IncompleteRegionException;

   Region getIncompleteRegion();

   boolean isDefined();

   int getArea();

   void learnChanges();

   void clear();

   String getTypeName();

   List<String> getInformationLines();
}
