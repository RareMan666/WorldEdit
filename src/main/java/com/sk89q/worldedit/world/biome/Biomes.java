package com.sk89q.worldedit.world.biome;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.util.WeightedChoice;
import com.sk89q.worldedit.util.function.LevenshteinDistance;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import java.util.Collection;
import javax.annotation.Nullable;

public final class Biomes {
   private Biomes() {
   }

   @Nullable
   public static BaseBiome findBiomeByName(Collection<BaseBiome> biomes, String name, BiomeRegistry registry) {
      Preconditions.checkNotNull(biomes);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(registry);
      Function<String, ? extends Number> compare = new LevenshteinDistance(name, false, LevenshteinDistance.STANDARD_CHARS);
      WeightedChoice<BaseBiome> chooser = new WeightedChoice<>(Functions.compose(compare, new BiomeName(registry)), 0.0);

      for (BaseBiome biome : biomes) {
         chooser.consider(biome);
      }

      Optional<WeightedChoice.Choice<BaseBiome>> choice = chooser.getChoice();
      return choice.isPresent() && ((WeightedChoice.Choice)choice.get()).getScore() <= 1.0 ? (BaseBiome)((WeightedChoice.Choice)choice.get()).getValue() : null;
   }
}
