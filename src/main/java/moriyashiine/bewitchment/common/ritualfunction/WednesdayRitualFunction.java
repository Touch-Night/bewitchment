package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class WednesdayRitualFunction extends RitualFunction {
	public WednesdayRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.wednesday";
	}
	
	@Override
	public boolean isValid(ServerWorld world, BlockPos pos, Inventory inventory) {
		return LocalDateTime.now().getDayOfWeek() == DayOfWeek.WEDNESDAY;
	}
	
	@Override
	public void tick(World world, BlockPos pos) {
		if (!world.isClient) {
			if (world.getTime() % 20 == 0) {
				for (int i = 0; i < world.random.nextInt(4) + 1; i++) {
					ToadEntity entity = BWEntityTypes.TOAD.create(world);
					if (entity != null) {
						entity.initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
						entity.refreshPositionAndAngles(pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -3, 3), pos.getY() + 3, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -3, 3), 0, world.random.nextInt(360));
						entity.addStatusEffect(new StatusEffectInstance(BWStatusEffects.WEDNESDAY, world.random.nextInt(100)));
						entity.isFromWednesdayRitual = true;
						world.spawnEntity(entity);
					}
				}
			}
		}
	}
}