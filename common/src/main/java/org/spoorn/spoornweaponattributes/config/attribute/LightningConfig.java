package org.spoorn.spoornweaponattributes.config.attribute;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class LightningConfig {

    @Comment("Chance for a weapon to have lightning damage [0 = never, 1 = always] [default = 0.03]")
    public double attributeChance = 0.03;

    @Comment("Chance to strike the target with lightning [0 = no lightning strikes] [default = 0.5]")
    public double lightningStrikeChance = 0.5;

    @Comment("Minimum bonus damage [default = 1.0]")
    public float minDamage = 1;

    @Comment("Maximum bonus damage [default = 30.0]")
    public float maxDamage = 30;

    @Comment("True if damage should be calculated using a Gaussian distribution, else it will be a linearly random\n" +
            "value between the minDamage and maxDamage [default = true]")
    public boolean useGaussian = true;

    // The default mean and sd makes it so  there's a ~5.48% chance of getting above 5 damage
    // Use https://onlinestatbook.com/2/calculators/normal_dist.html
    @Comment("Average damage [default = 1]")
    public int mean = 1;

    @Comment("Standard deviation for the distribution [default = 2.5]")
    public double standardDeviation = 2.5;
}
