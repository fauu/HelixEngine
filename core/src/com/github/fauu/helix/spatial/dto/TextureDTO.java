package com.github.fauu.helix.spatial.dto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureDTO {

  private Texture setTexture;

  private TextureRegion region;

  public TextureDTO(Texture setTexture, TextureRegion region) {
    this.setTexture = setTexture;
    this.region = region;
  }

  public Texture getSetTexture() {
    return setTexture;
  }
  
  public TextureRegion getRegion() {
    return region;
  }

}
