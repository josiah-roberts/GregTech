package gregtech.api.unification.material.type;

import com.google.common.collect.ImmutableList;
import gregtech.common.cable.WireProperties;
import gregtech.api.unification.Element;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.stack.MaterialStack;

import javax.annotation.Nullable;

import static gregtech.api.unification.material.type.DustMaterial.MatFlags.GENERATE_PLATE;
import static gregtech.api.unification.material.type.DustMaterial.MatFlags.SMELT_INTO_FLUID;
import static gregtech.api.util.GTUtility.createFlag;
import static gregtech.api.unification.material.type.MetalMaterial.MatFlags.*;
import static gregtech.api.unification.material.type.SolidMaterial.MatFlags.GENERATE_ROD;

public class MetalMaterial extends SolidMaterial {

    public static final class MatFlags {

        public static final long GENERATE_FOIL = createFlag(25);
        public static final long GENERATE_BOLT_SCREW = createFlag(26);
        public static final long GENERATE_RING = createFlag(27);
        public static final long GENERATE_SPRING = createFlag(28);
        public static final long GENERATE_FINE_WIRE = createFlag(29);
        public static final long GENERATE_ROTOR = createFlag(30);
        public static final long GENERATE_SMALL_GEAR = createFlag(31);
        public static final long GENERATE_DENSE = createFlag(32);
        public static final long GENERATE_SPRING_SMALL = createFlag(33);

        /**
         * Add this to your Material if you want to have its Ore Calcite heated in a Blast Furnace for more output. Already listed are:
         * Iron, Pyrite, PigIron, WroughtIron.
         */
        public static final long BLAST_FURNACE_CALCITE_DOUBLE = createFlag(35);
        public static final long BLAST_FURNACE_CALCITE_TRIPLE = createFlag(36);

    }

    /**
     * Specifies a material into which this material parts turn when heated
     */
    public MetalMaterial smeltInto;

    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    public MetalMaterial arcSmeltInto;

    /**
     * Material which obtained when this material is polarized
     */
    @Nullable
    public MetalMaterial magneticMaterial;

    /**
     * Blast furnace temperature of this material
     * Equal to zero if material doesn't use blast furnace
     * If below 1000C, primitive blast furnace recipes will be also added
     */
    public final int blastFurnaceTemperature;

    /**
     * If set, cable will be generated for this material with base stats
     * specified by this field
     */
    @Nullable
    public WireProperties cableProperties;

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, int toolDurability, int blastFurnaceTemperature) {
        super(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, toolSpeed, toolDurability);
        this.blastFurnaceTemperature = blastFurnaceTemperature;
        this.smeltInto = this;
        this.arcSmeltInto = this;
        add(SMELT_INTO_FLUID);
    }

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, Element element) {
        this(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, 0, 0, 0);
    }

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, Element element, int blastFurnaceTemperature) {
        this(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, 0, 0, blastFurnaceTemperature);
    }

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, Element element, float toolSpeed, int toolDurability) {
        this(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, element, toolSpeed, toolDurability, 0);
    }

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags, float toolSpeed, int toolDurability) {
        this(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, toolSpeed, toolDurability, 0);
    }

    public MetalMaterial(int metaItemSubId, String name, int materialRGB, MaterialIconSet materialIconSet, int harvestLevel, ImmutableList<MaterialStack> materialComponents, long materialGenerationFlags) {
        this(metaItemSubId, name, materialRGB, materialIconSet, harvestLevel, materialComponents, materialGenerationFlags, null, 0, 0, 0);
    }

    @Override
    protected void initMaterial(String name) {
        super.initMaterial(name);
        if(blastFurnaceTemperature > 0) {
            setFluidTemperature(blastFurnaceTemperature);
        }
    }

    @Override
    protected long verifyMaterialBits(long generationBits) {
        if((generationBits & GENERATE_DENSE) > 0) {
            generationBits |= GENERATE_PLATE;
        }
        if((generationBits & GENERATE_ROTOR) > 0) {
            generationBits |= GENERATE_BOLT_SCREW;
            generationBits |= GENERATE_RING;
            generationBits |= GENERATE_PLATE;
        }
        if((generationBits & GENERATE_SMALL_GEAR) > 0) {
            generationBits |= GENERATE_PLATE;
        }
        if((generationBits & GENERATE_FOIL) > 0) {
            generationBits |= GENERATE_PLATE;
        }
        if((generationBits & GENERATE_RING) > 0) {
            generationBits |= GENERATE_ROD;
        }
        if((generationBits & GENERATE_BOLT_SCREW) > 0) {
            generationBits |= GENERATE_ROD;
        }
        return super.verifyMaterialBits(generationBits);
    }

    public MetalMaterial setSmeltingInto(MetalMaterial smeltInto) {
        this.smeltInto = smeltInto;
        return this;
    }

    public MetalMaterial setArcSmeltingInto(MetalMaterial arcSmeltingInto) {
        this.arcSmeltInto = arcSmeltingInto;
        return this;
    }

    public MetalMaterial setCableProperties(long voltage, int baseAmperage, int lossPerBlock) {
        this.cableProperties = new WireProperties((int) voltage, baseAmperage, lossPerBlock);
        return this;
    }

}
