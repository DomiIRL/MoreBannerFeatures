package dev.svrt.domiirl.morebannerfeatures.model;

import net.minecraft.client.model.PlayerCapeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;

public class BannerCapeModel<T extends PlayerRenderState> extends PlayerCapeModel<T> {

  public BannerCapeModel(ModelPart modelPart) {
    super(modelPart);
  }


}
