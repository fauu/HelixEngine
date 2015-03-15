package com.github.fauu.helix.manager;

import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager extends Manager {

  protected static final String DIRECTORY_NAME = "texture-set";
  
  protected static final String EXTENSION = "atlas";

  @Wire
  protected AssetManager assetManager;
  
  private TextureAtlas textureSet;
  
  public TextureManager() { }
  
  public void loadSet(String name) {
    String path = DIRECTORY_NAME + "/" + name + "." + EXTENSION;

    assetManager.load(path, TextureAtlas.class);
    assetManager.finishLoading();

    textureSet = assetManager.get(path);
  }
    
  public TextureAtlas getTextureSet() {
    return textureSet;
  }

}
