package net.evolvingreaims.evolvingrealms.registry;

import net.evolvingreaims.evolvingrealms.EvolvingRealms;
import net.evolvingreaims.evolvingrealms.fluid.AcidFluid;
import net.evolvingreaims.evolvingrealms.fluid.LiquidSulfurFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registers custom fluids for Evolving Realms.
 */
public final class ModFluids {

    public static final FlowableFluid ACID_SOURCE  = register("acid",         new AcidFluid.Source());
    public static final FlowableFluid ACID_FLOWING = register("flowing_acid", new AcidFluid.Flowing());

    public static final FlowableFluid LIQUID_SULFUR_SOURCE  = register("liquid_sulfur",         new LiquidSulfurFluid.Source());
    public static final FlowableFluid LIQUID_SULFUR_FLOWING = register("flowing_liquid_sulfur", new LiquidSulfurFluid.Flowing());

    private static <T extends Fluid> T register(String name, T fluid) {
        return Registry.register(Registries.FLUID, Identifier.of(EvolvingRealms.MOD_ID, name), fluid);
    }

    public static void register() {
        EvolvingRealms.LOGGER.debug("Registering fluids for Evolving Realms");
    }
}
