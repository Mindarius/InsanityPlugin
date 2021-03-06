package me.Minecraftmage113.InsanityPlugin.helpers.objects;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Cat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Merchant;
import org.bukkit.potion.PotionEffect;

public class InsanityEntity {
	final static String[] metaKeys = new String[] {};
	/**
	 * Takes an entity and copies <b>all</b> it's important stuff onto a different entity.
	 * breaks if the entities are not of the same type.
	 * Assumes a blank entity for <b>to</b>, and does not change positioning.
	 */
	public static void clone(Entity from, Entity to) {
		to.setCustomName(from.getCustomName());
		to.setCustomNameVisible(from.isCustomNameVisible());
		to.setFallDistance(from.getFallDistance());
		to.setFireTicks(from.getFireTicks());
		to.setGlowing(from.isGlowing());
		to.setGravity(from.hasGravity());
		to.setInvulnerable(from.isInvulnerable());
		to.setLastDamageCause(from.getLastDamageCause());
		to.setOp(from.isOp());
		to.setPersistent(from.isPersistent());
		to.setPortalCooldown(from.getPortalCooldown());
		to.setSilent(from.isSilent());
		to.setTicksLived(from.getTicksLived());
		for(Entity passenger : to.getPassengers()) { to.removePassenger(passenger); } //Clears any passengers. Lassos store 1 entity at a time.
		for(String tag : from.getScoreboardTags()) { to.addScoreboardTag(tag); }
		for(String key : metaKeys) { 
			if(from.hasMetadata(key)&&from.getMetadata(key)!=null&&from.getMetadata(key).size()!=0) {
				to.setMetadata(key, from.getMetadata(key).get(0));
			}
		}
		if(from instanceof Attributable && to instanceof Attributable) {
			cloneAttributes((Attributable) from, (Attributable) to);
		}
		if(from instanceof Damageable && to instanceof Damageable) {
			cloneDamageableStats((Damageable) from, (Damageable) to);
		}
		if(from instanceof Ageable && to instanceof Ageable) {
			cloneAgeableStats((Ageable) from, (Ageable) to);
		}
		if(from instanceof Breedable && to instanceof Breedable) {
			cloneBreedableStats((Breedable) from, (Breedable) to);
		}
		if(from instanceof Mob && to instanceof Mob) {
			((Mob) to).setAware(((Mob) from).isAware());
		}
		if(from instanceof LivingEntity && to instanceof LivingEntity) {
			cloneLivingStats((LivingEntity) from, (LivingEntity) to);
		}
		if(to instanceof Bat) { ((Bat) to).setAwake(true); }
		if(from instanceof AbstractVillager && to instanceof AbstractVillager) {
			((AbstractVillager) to).getInventory().setContents(((AbstractVillager) from).getInventory().getContents());
		}
		if(from instanceof Villager && to instanceof Villager) {
			cloneVillager((Villager) from, (Villager) to);
		}
		if(from instanceof Animals && to instanceof Animals) {
			cloneAnimal((Animals) from, (Animals) to);
		}
		if(from instanceof Bee && to instanceof Bee) {
			cloneBee((Bee) from, (Bee) to);
		}
		if(from instanceof MushroomCow && to instanceof MushroomCow) {
			((MushroomCow) to).setVariant(((MushroomCow) from).getVariant());
		}
		if(from instanceof Fox && to instanceof Fox) {
			cloneFox((Fox) from, (Fox) to);
		}
		if(from instanceof Hoglin && to instanceof Hoglin) {
			cloneHoglin((Hoglin) from, (Hoglin) to);
		}
		if(from instanceof Ocelot && to instanceof Ocelot) {
			((Ocelot) to).setCatType(((Ocelot) from).getCatType());
		}
		if(from instanceof Panda && to instanceof Panda) {
			clonePanda((Panda) from, (Panda) to);
		}
		if(from instanceof Rabbit && to instanceof Rabbit) {
			((Rabbit) to).setRabbitType(((Rabbit) from).getRabbitType());
		}
		if(from instanceof Sheep && to instanceof Sheep) {
			((Sheep) to).setSheared(((Sheep) from).isSheared());
		}
		if(from instanceof Steerable && to instanceof Steerable) {
			cloneSteerable((Steerable) from, (Steerable) to);
		}
		if(from instanceof Strider && to instanceof Strider) {
			((Strider) to).setShivering(((Strider) from).isShivering());
		}
		if(from instanceof Tameable && to instanceof Tameable) {
			cloneTameable((Tameable) from, (Tameable) to);
		}
		if(from instanceof AbstractHorse && to instanceof AbstractHorse) {
			cloneAbstractHorse((AbstractHorse) from, (AbstractHorse) to);
		}
		if(from instanceof ChestedHorse && to instanceof ChestedHorse) {
			((ChestedHorse) to).setCarryingChest(((ChestedHorse) from).isCarryingChest());
		}
		if(from instanceof Llama && to instanceof Llama) {
			cloneLlama((Llama) from, (Llama) to);
		}
		if(from instanceof Horse && to instanceof Horse) {
			cloneHorse((Horse) from, (Horse) to);
		}
		if(from instanceof Cat && to instanceof Cat) {
			cloneCat((Cat) from, (Cat) to);
		}
		if(from instanceof Parrot && to instanceof Parrot) {
			((Parrot) to).setVariant(((Parrot) from).getVariant());
		}
		if(from instanceof Wolf && to instanceof Wolf) {
			cloneWolf((Wolf) from, (Wolf) to);
		}
		if(from instanceof PiglinAbstract && to instanceof PiglinAbstract) {
			clonePiglinAbstract((PiglinAbstract) from, (PiglinAbstract) to);
		}
		if(from instanceof Piglin && to instanceof Piglin) {
			((Piglin) to).setIsAbleToHunt(((Piglin) from).isAbleToHunt());
		}
		if(from instanceof Zombie && to instanceof Zombie) {
			((Zombie) to).setConversionTime(((Zombie) from).getConversionTime());
		}
		if(from instanceof Husk && to instanceof Husk) {
			((Husk) to).setConversionTime(((Husk) from).getConversionTime());
		}
		if(from instanceof PigZombie && to instanceof PigZombie) {
			clonePigZombie((PigZombie) from, (PigZombie) to);
		}
		if(from instanceof ZombieVillager && to instanceof ZombieVillager) {
			cloneZombieVillager((ZombieVillager) from, (ZombieVillager) to);
		}
		if(from instanceof IronGolem && to instanceof IronGolem) {
			((IronGolem) to).setPlayerCreated(((IronGolem) from).isPlayerCreated());
		}
		if(from instanceof Snowman && to instanceof Snowman) {
			((Snowman) to).setDerp(((Snowman) from).isDerp());
		}
		if(from instanceof ZombieVillager && to instanceof ZombieVillager) {
			cloneZombieVillager((ZombieVillager) from, (ZombieVillager) to);
		}
		if(from instanceof Creeper && to instanceof Creeper) {
			cloneCreeper((Creeper) from, (Creeper) to);
		}
		if(from instanceof Enderman && to instanceof Enderman) {
			cloneEnderman((Enderman) from, (Enderman) to);
		}
		if(from instanceof Endermite && to instanceof Endermite) {
			((Endermite) to).setPlayerSpawned(((Endermite) from).isPlayerSpawned());
		}
		if(from instanceof Raider && to instanceof Raider) {
			cloneRaider((Raider) from, (Raider) to);
		}
		if(from instanceof Vex && to instanceof Vex) {
			((Vex) to).setCharging(((Vex) from).isCharging());
		}
		if(from instanceof PufferFish && to instanceof PufferFish) {
			((PufferFish) to).setPuffState(((PufferFish) from).getPuffState());
		}
		if(from instanceof TropicalFish && to instanceof TropicalFish) {
			cloneTropicalFish((TropicalFish) from, (TropicalFish) to);
		}
		if(from instanceof Phantom && to instanceof Phantom) {
			((Phantom) to).setSize(((Phantom) from).getSize());
		}
		if(from instanceof Slime && to instanceof Slime) {
			((Slime) to).setSize(((Slime) from).getSize());
		}
		if(from instanceof Merchant && to instanceof Merchant) {
			cloneMerchant((Merchant) from, (Merchant) to);
		}
		
		
//		Merchant, Villager classers
//		Tameable
//		set to no mount or passengers I guess.
	}

	public static void cloneAttributes(Attributable from, Attributable to) {
		for(Attribute attribute : Attribute.values()) {
			if(to.getAttribute(attribute)==null) { continue; }
			to.getAttribute(attribute).setBaseValue(from.getAttribute(attribute).getBaseValue());
			for(AttributeModifier modifier : from.getAttribute(attribute).getModifiers()) {
				to.getAttribute(attribute).addModifier(modifier);
			}
		}
	}
	public static void cloneDamageableStats(Damageable from, Damageable to) {
		to.setAbsorptionAmount(from.getAbsorptionAmount());
		to.setHealth(from.getHealth());
	}
	public static void cloneAgeableStats(Ageable from, Ageable to) {
		if(from.isAdult()) {
			to.setAdult();
		} else {
			to.setBaby();
		}
		to.setAge(from.getAge());
	}
	public static void cloneBreedableStats(Breedable from, Breedable to) {
		to.setAgeLock(from.getAgeLock());
		to.setBreed(from.canBreed());
	}
	public static void cloneLivingStats(LivingEntity from, LivingEntity to) {
		to.setRemainingAir(from.getRemainingAir());
		to.setMaximumAir(from.getMaximumAir());
		to.setArrowCooldown(from.getArrowCooldown());
		to.setArrowsInBody(from.getArrowsInBody());
		to.setMaximumNoDamageTicks(from.getMaximumNoDamageTicks());
		to.setLastDamage(from.getLastDamage());
		to.setNoDamageTicks(from.getNoDamageTicks());
		for(PotionEffect effect : from.getActivePotionEffects()) { to.addPotionEffect(effect); }
		to.setRemoveWhenFarAway(from.getRemoveWhenFarAway());
		cloneEquipment(from.getEquipment(), to.getEquipment());
		to.setCanPickupItems(from.getCanPickupItems());
		to.setAI(from.hasAI());
		to.setCollidable(from.isCollidable());
		to.setInvisible(from.isInvisible());
	}
	public static void cloneEquipment(EntityEquipment from, EntityEquipment to) {
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			to.setItem(slot, from.getItem(slot));
		}
		to.setHelmetDropChance(from.getHelmetDropChance());
		to.setChestplateDropChance(from.getChestplateDropChance());
		to.setLeggingsDropChance(from.getLeggingsDropChance());
		to.setBootsDropChance(from.getBootsDropChance());
		to.setItemInMainHandDropChance(from.getItemInMainHandDropChance());
		to.setItemInOffHandDropChance(from.getItemInOffHandDropChance());
	}
	private static void cloneVillager(Villager from, Villager to) {
//		for(MemoryKey key : MemoryKey.values()) {
//			to.setMemory(key, from.getMemory(key));
//		}
		to.setVillagerType(from.getVillagerType());
		to.setVillagerLevel(from.getVillagerLevel());
		to.setVillagerExperience(from.getVillagerExperience());
		to.setProfession(from.getProfession());
	}
	private static void cloneAnimal(Animals from, Animals to) {
		to.setBreedCause(from.getBreedCause());
		to.setLoveModeTicks(from.getLoveModeTicks());
	}
	private static void cloneBee(Bee from, Bee to) {
		to.setAnger(from.getAnger());
		to.setCannotEnterHiveTicks(from.getCannotEnterHiveTicks());
		to.setFlower(from.getFlower());
		to.setHasNectar(from.hasNectar());
		to.setHasStung(from.hasStung());
		to.setHive(from.getHive());
	}
	private static void cloneFox(Fox from, Fox to) {
		to.setFirstTrustedPlayer(from.getFirstTrustedPlayer());
		to.setFoxType(from.getFoxType());
		to.setSecondTrustedPlayer(from.getSecondTrustedPlayer());
		to.setSleeping(from.isSleeping());
	}
	private static void cloneHoglin(Hoglin from, Hoglin to) {
		if(from.isConverting()) {to.setConversionTime(from.getConversionTime());}
		to.setImmuneToZombification(from.isImmuneToZombification());
		to.setIsAbleToBeHunted(from.isAbleToBeHunted());
	}
	private static void clonePanda(Panda from, Panda to) {
		to.setMainGene(from.getMainGene());
		to.setHiddenGene(from.getHiddenGene());
	}
	private static void cloneSteerable(Steerable from, Steerable to) {
		to.setBoostTicks(from.getBoostTicks());
		to.setCurrentBoostTicks(from.getCurrentBoostTicks());
		to.setSaddle(from.hasSaddle());
	}
	private static void cloneTameable(Tameable from, Tameable to) {
		to.setOwner(from.getOwner());
		to.setTamed(from.isTamed());
	}
	private static void cloneAbstractHorse(AbstractHorse from, AbstractHorse to) {
		to.setDomestication(from.getDomestication());
		to.setJumpStrength(from.getJumpStrength());
		to.setMaxDomestication(from.getMaxDomestication());
		to.getInventory().setContents(from.getInventory().getContents());
	}
	private static void cloneLlama(Llama from, Llama to) {
		to.setColor(from.getColor());
		to.setStrength(from.getStrength());
		to.getInventory().setContents(from.getInventory().getContents());
	}
	private static void cloneHorse(Horse from, Horse to) {
		to.setColor(from.getColor());
		to.setStyle(from.getStyle());
		to.getInventory().setContents(from.getInventory().getContents());
	}
	private static void cloneCat(Cat from, Cat to) {
		to.setCatType(from.getCatType());
		to.setCollarColor(from.getCollarColor());
	}
	private static void cloneWolf(Wolf from, Wolf to) {
		to.setCollarColor(from.getCollarColor());
		to.setAngry(from.isAngry());
	}
	private static void clonePiglinAbstract(PiglinAbstract from, PiglinAbstract to) {
		if(from.isConverting()) {to.setConversionTime(from.getConversionTime());}
		to.setImmuneToZombification(from.isImmuneToZombification());
	}
	private static void clonePigZombie(PigZombie from, PigZombie to) {
		if(from.isConverting()) {to.setConversionTime(from.getConversionTime());}
		to.setAnger(from.getAnger());
		to.setAngry(from.isAngry());
	}
	private static void cloneZombieVillager(ZombieVillager from, ZombieVillager to) {
		if(from.isConverting()) {to.setConversionPlayer(from.getConversionPlayer());
		to.setConversionTime(from.getConversionTime());}
		to.setVillagerProfession(from.getVillagerProfession());
		to.setVillagerType(from.getVillagerType());
	}
	private static void cloneCreeper(Creeper from, Creeper to) {
		to.setExplosionRadius(from.getExplosionRadius());
		to.setMaxFuseTicks(from.getMaxFuseTicks());
		to.setPowered(from.isPowered());
	}
	private static void cloneEnderman(Enderman from, Enderman to) {
		to.setCarriedBlock(from.getCarriedBlock());
//		to.setCarriedMaterial(from.getCarriedMaterial());
	}
	private static void cloneRaider(Raider from, Raider to) {
		to.setCanJoinRaid(from.isCanJoinRaid());
		to.setPatrolLeader(from.isPatrolLeader());
		to.setPatrolTarget(from.getPatrolTarget());
	}
	private static void cloneTropicalFish(TropicalFish from, TropicalFish to) {
		to.setBodyColor(from.getBodyColor());
		to.setPattern(from.getPattern());
		to.setPatternColor(from.getPatternColor());
	}
	private static void cloneMerchant(Merchant from, Merchant to) {
		to.setRecipes(from.getRecipes());
	}
}
