package com.sk89q.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.jchronic.Chronic;
import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.worldedit.command.tool.BlockTool;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.command.tool.SinglePickaxe;
import com.sk89q.worldedit.command.tool.Tool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionShapeEvent;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.RegionSelectorType;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.request.Request;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.snapshot.Snapshot;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class LocalSession {
   public static transient int MAX_HISTORY_SIZE = 15;
   private transient LocalConfiguration config;
   private final transient AtomicBoolean dirty = new AtomicBoolean();
   private transient RegionSelector selector = new CuboidRegionSelector();
   private transient boolean placeAtPos1 = false;
   private transient LinkedList<EditSession> history = new LinkedList<>();
   private transient int historyPointer = 0;
   private transient ClipboardHolder clipboard;
   private transient boolean toolControl = true;
   private transient boolean superPickaxe = false;
   private transient BlockTool pickaxeMode = new SinglePickaxe();
   private transient Map<Integer, Tool> tools = new HashMap<>();
   private transient int maxBlocksChanged = -1;
   private transient boolean useInventory;
   private transient Snapshot snapshot;
   private transient boolean hasCUISupport = false;
   private transient int cuiVersion = -1;
   private transient boolean fastMode = false;
   private transient Mask mask;
   private transient TimeZone timezone = TimeZone.getDefault();
   private String lastScript;
   private RegionSelectorType defaultSelector;

   public LocalSession() {
   }

   public LocalSession(@Nullable LocalConfiguration config) {
      this.config = config;
   }

   public void setConfiguration(LocalConfiguration config) {
      Preconditions.checkNotNull(config);
      this.config = config;
   }

   public void postLoad() {
      if (this.defaultSelector != null) {
         this.selector = this.defaultSelector.createSelector();
      }
   }

   public boolean isDirty() {
      return this.dirty.get();
   }

   private void setDirty() {
      this.dirty.set(true);
   }

   public boolean compareAndResetDirty() {
      return this.dirty.compareAndSet(true, false);
   }

   public TimeZone getTimeZone() {
      return this.timezone;
   }

   public void setTimezone(TimeZone timezone) {
      Preconditions.checkNotNull(timezone);
      this.timezone = timezone;
   }

   public void clearHistory() {
      this.history.clear();
      this.historyPointer = 0;
   }

   public void remember(EditSession editSession) {
      Preconditions.checkNotNull(editSession);
      if (editSession.size() != 0) {
         while (this.historyPointer < this.history.size()) {
            this.history.remove(this.historyPointer);
         }

         this.history.add(editSession);

         while (this.history.size() > MAX_HISTORY_SIZE) {
            this.history.remove(0);
         }

         this.historyPointer = this.history.size();
      }
   }

   public EditSession undo(@Nullable BlockBag newBlockBag, LocalPlayer player) {
      return this.undo(newBlockBag, (Player)player);
   }

   public EditSession undo(@Nullable BlockBag newBlockBag, Player player) {
      Preconditions.checkNotNull(player);
      this.historyPointer--;
      if (this.historyPointer >= 0) {
         EditSession editSession = this.history.get(this.historyPointer);
         EditSession newEditSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(editSession.getWorld(), -1, newBlockBag, player);
         newEditSession.enableQueue();
         newEditSession.setFastMode(this.fastMode);
         editSession.undo(newEditSession);
         return editSession;
      } else {
         this.historyPointer = 0;
         return null;
      }
   }

   public EditSession redo(@Nullable BlockBag newBlockBag, LocalPlayer player) {
      return this.redo(newBlockBag, (Player)player);
   }

   public EditSession redo(@Nullable BlockBag newBlockBag, Player player) {
      Preconditions.checkNotNull(player);
      if (this.historyPointer < this.history.size()) {
         EditSession editSession = this.history.get(this.historyPointer);
         EditSession newEditSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(editSession.getWorld(), -1, newBlockBag, player);
         newEditSession.enableQueue();
         newEditSession.setFastMode(this.fastMode);
         editSession.redo(newEditSession);
         this.historyPointer++;
         return editSession;
      } else {
         return null;
      }
   }

   public RegionSelectorType getDefaultRegionSelector() {
      return this.defaultSelector;
   }

   public void setDefaultRegionSelector(RegionSelectorType defaultSelector) {
      Preconditions.checkNotNull(defaultSelector);
      this.defaultSelector = defaultSelector;
      this.setDirty();
   }

   @Deprecated
   public RegionSelector getRegionSelector(LocalWorld world) {
      return this.getRegionSelector((World)world);
   }

   public RegionSelector getRegionSelector(World world) {
      Preconditions.checkNotNull(world);
      if (this.selector.getWorld() == null || !this.selector.getWorld().equals(world)) {
         this.selector.setWorld(world);
         this.selector.clear();
      }

      return this.selector;
   }

   @Deprecated
   public RegionSelector getRegionSelector() {
      return this.selector;
   }

   @Deprecated
   public void setRegionSelector(LocalWorld world, RegionSelector selector) {
      this.setRegionSelector((World)world, selector);
   }

   public void setRegionSelector(World world, RegionSelector selector) {
      Preconditions.checkNotNull(world);
      Preconditions.checkNotNull(selector);
      selector.setWorld(world);
      this.selector = selector;
   }

   @Deprecated
   public boolean isRegionDefined() {
      return this.selector.isDefined();
   }

   @Deprecated
   public boolean isSelectionDefined(LocalWorld world) {
      return this.isSelectionDefined((World)world);
   }

   public boolean isSelectionDefined(World world) {
      Preconditions.checkNotNull(world);
      return this.selector.getIncompleteRegion().getWorld() != null && this.selector.getIncompleteRegion().getWorld().equals(world)
         ? this.selector.isDefined()
         : false;
   }

   @Deprecated
   public Region getRegion() throws IncompleteRegionException {
      return this.selector.getRegion();
   }

   @Deprecated
   public Region getSelection(LocalWorld world) throws IncompleteRegionException {
      return this.getSelection((World)world);
   }

   public Region getSelection(World world) throws IncompleteRegionException {
      Preconditions.checkNotNull(world);
      if (this.selector.getIncompleteRegion().getWorld() != null && this.selector.getIncompleteRegion().getWorld().equals(world)) {
         return this.selector.getRegion();
      } else {
         throw new IncompleteRegionException();
      }
   }

   public World getSelectionWorld() {
      return this.selector.getIncompleteRegion().getWorld();
   }

   public ClipboardHolder getClipboard() throws EmptyClipboardException {
      if (this.clipboard == null) {
         throw new EmptyClipboardException();
      } else {
         return this.clipboard;
      }
   }

   public void setClipboard(@Nullable ClipboardHolder clipboard) {
      this.clipboard = clipboard;
   }

   public boolean isToolControlEnabled() {
      return this.toolControl;
   }

   public void setToolControl(boolean toolControl) {
      this.toolControl = toolControl;
   }

   public int getBlockChangeLimit() {
      return this.maxBlocksChanged;
   }

   public void setBlockChangeLimit(int maxBlocksChanged) {
      this.maxBlocksChanged = maxBlocksChanged;
   }

   public boolean hasSuperPickAxe() {
      return this.superPickaxe;
   }

   public void enableSuperPickAxe() {
      this.superPickaxe = true;
   }

   public void disableSuperPickAxe() {
      this.superPickaxe = false;
   }

   public boolean toggleSuperPickAxe() {
      this.superPickaxe = !this.superPickaxe;
      return this.superPickaxe;
   }

   public Vector getPlacementPosition(Player player) throws IncompleteRegionException {
      Preconditions.checkNotNull(player);
      return (Vector)(!this.placeAtPos1 ? player.getBlockIn() : this.selector.getPrimaryPosition());
   }

   public boolean togglePlacementPosition() {
      this.placeAtPos1 = !this.placeAtPos1;
      return this.placeAtPos1;
   }

   @Nullable
   public BlockBag getBlockBag(Player player) {
      Preconditions.checkNotNull(player);
      return !this.useInventory ? null : player.getInventoryBlockBag();
   }

   @Nullable
   public Snapshot getSnapshot() {
      return this.snapshot;
   }

   public void setSnapshot(@Nullable Snapshot snapshot) {
      this.snapshot = snapshot;
   }

   public BlockTool getSuperPickaxe() {
      return this.pickaxeMode;
   }

   public void setSuperPickaxe(BlockTool tool) {
      Preconditions.checkNotNull(tool);
      this.pickaxeMode = tool;
   }

   @Nullable
   public Tool getTool(int item) {
      return this.tools.get(item);
   }

   public BrushTool getBrushTool(int item) throws InvalidToolBindException {
      Tool tool = this.getTool(item);
      if (tool == null || !(tool instanceof BrushTool)) {
         tool = new BrushTool("worldedit.brush.sphere");
         this.setTool(item, tool);
      }

      return (BrushTool)tool;
   }

   public void setTool(int item, @Nullable Tool tool) throws InvalidToolBindException {
      if (item > 0 && item < 255) {
         throw new InvalidToolBindException(item, "Blocks can't be used");
      } else if (item == this.config.wandItem) {
         throw new InvalidToolBindException(item, "Already used for the wand");
      } else if (item == this.config.navigationWand) {
         throw new InvalidToolBindException(item, "Already used for the navigation wand");
      } else {
         this.tools.put(item, tool);
      }
   }

   public boolean isUsingInventory() {
      return this.useInventory;
   }

   public void setUseInventory(boolean useInventory) {
      this.useInventory = useInventory;
   }

   @Nullable
   public String getLastScript() {
      return this.lastScript;
   }

   public void setLastScript(@Nullable String lastScript) {
      this.lastScript = lastScript;
      this.setDirty();
   }

   public void tellVersion(Actor player) {
   }

   public void dispatchCUIEvent(Actor actor, CUIEvent event) {
      Preconditions.checkNotNull(actor);
      Preconditions.checkNotNull(event);
      if (this.hasCUISupport) {
         actor.dispatchCUIEvent(event);
      }
   }

   public void dispatchCUISetup(Actor actor) {
      if (this.selector != null) {
         this.dispatchCUISelection(actor);
      }
   }

   public void dispatchCUISelection(Actor actor) {
      Preconditions.checkNotNull(actor);
      if (this.hasCUISupport) {
         if (this.selector instanceof CUIRegion) {
            CUIRegion tempSel = (CUIRegion)this.selector;
            if (tempSel.getProtocolVersion() > this.cuiVersion) {
               actor.dispatchCUIEvent(new SelectionShapeEvent(tempSel.getLegacyTypeID()));
               tempSel.describeLegacyCUI(this, actor);
            } else {
               actor.dispatchCUIEvent(new SelectionShapeEvent(tempSel.getTypeID()));
               tempSel.describeCUI(this, actor);
            }
         }
      }
   }

   public void describeCUI(Actor actor) {
      Preconditions.checkNotNull(actor);
      if (this.hasCUISupport) {
         if (this.selector instanceof CUIRegion) {
            CUIRegion tempSel = (CUIRegion)this.selector;
            if (tempSel.getProtocolVersion() > this.cuiVersion) {
               tempSel.describeLegacyCUI(this, actor);
            } else {
               tempSel.describeCUI(this, actor);
            }
         }
      }
   }

   public void handleCUIInitializationMessage(String text) {
      Preconditions.checkNotNull(text);
      String[] split = text.split("\\|");
      if (split.length > 1 && split[0].equalsIgnoreCase("v")) {
         this.setCUISupport(true);

         try {
            this.setCUIVersion(Integer.parseInt(split[1]));
         } catch (NumberFormatException var4) {
            WorldEdit.logger.warning("Error while reading CUI init message: " + var4.getMessage());
         }
      }
   }

   public boolean hasCUISupport() {
      return this.hasCUISupport;
   }

   public void setCUISupport(boolean support) {
      this.hasCUISupport = support;
   }

   public int getCUIVersion() {
      return this.cuiVersion;
   }

   public void setCUIVersion(int cuiVersion) {
      this.cuiVersion = cuiVersion;
   }

   @Nullable
   public Calendar detectDate(String input) {
      Preconditions.checkNotNull(input);
      Time.setTimeZone(this.getTimeZone());
      Options opt = new Options();
      opt.setNow(Calendar.getInstance(this.getTimeZone()));
      Span date = Chronic.parse(input, opt);
      return date == null ? null : date.getBeginCalendar();
   }

   @Deprecated
   public EditSession createEditSession(LocalPlayer player) {
      return this.createEditSession((Player)player);
   }

   public EditSession createEditSession(Player player) {
      Preconditions.checkNotNull(player);
      BlockBag blockBag = this.getBlockBag(player);
      EditSession editSession = WorldEdit.getInstance()
         .getEditSessionFactory()
         .getEditSession(player.isPlayer() ? player.getWorld() : null, this.getBlockChangeLimit(), blockBag, player);
      editSession.setFastMode(this.fastMode);
      Request.request().setEditSession(editSession);
      editSession.setMask(this.mask);
      return editSession;
   }

   public boolean hasFastMode() {
      return this.fastMode;
   }

   public void setFastMode(boolean fastMode) {
      this.fastMode = fastMode;
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask mask) {
      this.mask = mask;
   }

   public void setMask(com.sk89q.worldedit.masks.Mask mask) {
      this.setMask(mask != null ? Masks.wrap(mask) : null);
   }
}
