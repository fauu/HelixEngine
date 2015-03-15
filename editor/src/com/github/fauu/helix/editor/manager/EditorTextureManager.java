package com.github.fauu.helix.editor.manager;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.TextureEntry;

public class EditorTextureManager {

  private static final String DIRECTORY_NAME = "texture-set";
  
  private static final String EXTENSION = "atlas";
  
  public EditorTextureManager() { }

  public List<TextureEntry> getFullEntryList() {
    AssetManager assetManager = HelixEditor.getInstance().getAssetManager();

    assetManager.load("texture-set/1.atlas", TextureAtlas.class);
    assetManager.finishLoading();

    TextureAtlas textureSet = assetManager.get("texture-set/1.atlas");

    List<TextureEntry> entryList = new LinkedList<>();
    
    for (TextureAtlas.AtlasRegion region : textureSet.getRegions()) {
      TextureEntry entry = new TextureEntry();

      entry.setName(region.name);
      entry.setPreview(new TextureRegionDrawable(region));
      
      entryList.add(entry);
    }
    
    return entryList;
  }

}
