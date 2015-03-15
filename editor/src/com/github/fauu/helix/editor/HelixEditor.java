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

package com.github.fauu.helix.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.github.fauu.helix.editor.manager.EditorGeometryManager;
import com.github.fauu.helix.editor.manager.EditorTextureManager;
import com.github.fauu.helix.editor.screen.MainScreen;
import com.github.fauu.helix.editor.state.GeometryNameListState;
import com.github.fauu.helix.editor.state.TextureEntryListState;
import com.github.fauu.helix.editor.state.TilePropertiesState;
import com.github.fauu.helix.editor.state.ToolbarState;
import com.github.fauu.helix.editor.ui.UI;
import com.github.fauu.helix.manager.MapRegionManager;
import com.google.common.eventbus.EventBus;

public class HelixEditor extends Game {

  private static HelixEditor instance;
  
  private MainScreen mainScreen;
  
  private EventBus editorToUIEventBus;

  private EventBus editorToWorldEventBus;

  private UI ui;
  
  private ToolbarState toolbarState;
  
  private TilePropertiesState tilePropertiesState;
  
  private GeometryNameListState geometryNameListState;
  
  private TextureEntryListState textureEntryListState;
  
  private EditorGeometryManager editorGeometryManager;
  
  private EditorTextureManager editorTextureManager;
  
  @Override
  public void create() {
    instance = this;
    
    editorToUIEventBus = new EventBus();
    editorToWorldEventBus = new EventBus();
    
    mainScreen = new MainScreen();
    setScreen(mainScreen);
    
    toolbarState = new ToolbarState();
    tilePropertiesState = new TilePropertiesState();
    geometryNameListState = new GeometryNameListState();
    textureEntryListState = new TextureEntryListState();
    
    ui = new UI();
    
    editorGeometryManager = new EditorGeometryManager();
    editorTextureManager = new EditorTextureManager();
    editorTextureManager.getFullEntryList();
    
    toolbarState.initialize(ToolType.TILE_EDIT_TOOL);
    tilePropertiesState.initialize();
    geometryNameListState.initialize(editorGeometryManager.getAllNameEntries());
    textureEntryListState.initialize(editorTextureManager.getFullEntryList());

    Gdx.input.setInputProcessor(
        new InputMultiplexer(ui.getStage(), new EditorInputEventProcessor()));
  }

  public static HelixEditor getInstance() {
    return instance;
  }
  
  @Override
  public void render() {
    super.render();
    ui.render(Gdx.graphics.getDeltaTime());
  }
  
  @Override
  public void dispose() {
    super.dispose();
    ui.dispose();
  }
  
  public void loadMapRegion(FileHandle file) {
    mainScreen.getWorld()
              .getManager(MapRegionManager.class)
              .loadFromFile(file);
  }
  
  public void createAndOpenMapRegion(int width, int length) {
    mainScreen.getWorld()
              .getManager(MapRegionManager.class)
              .create(width, length);
  }
  
  public void closeCurrentMapRegion() {
    mainScreen.getWorld().getManager(MapRegionManager.class).unloadAll();
  }

  public void newMapRegionAction() {
    ui.showNewMapRegionDialog();
  }

  public void openMapRegionAction() {
    ui.showOpenMapRegionFileChooser();
  }

  public void exitAction() {
    Gdx.app.exit();
  }

  public EventBus getUIEventBus() {
    return editorToUIEventBus;
  }

  public EventBus getWorldEventBus() {
    return editorToWorldEventBus;
  }
  
  public AssetManager getAssetManager() {
    return mainScreen.getAssetManager();
  }
  
  public ToolbarState getToolbarState() {
    return toolbarState;
  }
  
  public TilePropertiesState getTilePropertiesState() {
    return tilePropertiesState;
  }
  
  public GeometryNameListState getGeometryNameListState() {
    return geometryNameListState;
  }
  
  public TextureEntryListState getTextureEntryListState() {
    return textureEntryListState;
  }
  
}
