package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.interfaces.InsanityTargetAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
	public MobEntityRendererMixin(EntityRenderDispatcher dispatcher, M model, float shadowRadius) {
		super(dispatcher, model, shadowRadius);
	}
	
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void shouldRender(T mobEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> callbackInfo) {
		InsanityTargetAccessor.of(mobEntity).ifPresent(insanityTargetAccessor -> {
			if (!MinecraftClient.getInstance().player.getUuid().equals(insanityTargetAccessor.getInsanityTargetUUID().orElse(null))) {
				callbackInfo.setReturnValue(false);
			}
		});
	}
}
