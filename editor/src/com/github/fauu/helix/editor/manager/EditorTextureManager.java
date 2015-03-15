/*
 * Copyright (C) 2014, 2015 Helix Engine Developers
 * (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.editor.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.TextureEntry;

import java.util.LinkedList;
import java.util.List;

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
