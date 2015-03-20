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
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.editor.screen.Overworld;
import com.github.fauu.helix.editor.state.TilePermissionListState;
import com.github.fauu.helix.editor.state.ToolbarState;
import com.github.fauu.helix.editor.ui.UI;
import com.github.fauu.helix.manager.AreaManager;
import com.google.common.eventbus.EventBus;

public class HelixEditor extends Game {

  private static HelixEditor instance;
  
  private Overworld overworld;
  
  private EventBus editorToUIEventBus;

  private EventBus editorToWorldEventBus;

  private UI ui;

  private ToolbarState toolbarState;

  private TilePermissionListState tilePermissionListState;

  @Override
  public void create() {
    instance = this;
    
    editorToUIEventBus = new EventBus();
    editorToWorldEventBus = new EventBus();
    
    overworld = new Overworld();
    setScreen(overworld);

    toolbarState = new ToolbarState();
    toolbarState.initialize(ToolType.TILE_PERMISSIONS);

    tilePermissionListState = new TilePermissionListState();
    tilePermissionListState.initialize(TilePermission.LEVEL0);

    ui = new UI();

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

  public void closeCurrentAreaAction() {
    overworld.getWorld().getManager(AreaManager.class).unloadCurrent();

    ui.setSidebarVisibility(false);
  }

  public void openAreaAction() {
    ui.showOpenAreaFileChooser();
  }

  public void loadAreaAction(String name) {
    overworld.getWorld().getManager(AreaManager.class).load(name);

    ui.setSidebarVisibility(true);

    if (toolbarState.getActiveTool() == ToolType.TILE_PERMISSIONS) {
      fadeAreaModelAction(true);
    }
  }

  public void saveAreaAction() {
    overworld.getWorld().getManager(AreaManager.class).save();
  }

  public void exitAction() {
    Gdx.app.exit();
  }

  public void fadeAreaModelAction(boolean on) {
    overworld.getSpatialIntermediary().setAreaSpatialOpacity(on ? 0.75f : 1);
  }

  public void centerCameraOnAreaAction() {
    overworld.getCameraIntermediary().centerCameraOnArea();
  }

  public void toolSelected(ToolType type) {
    boolean areaLoaded
        = overworld.getWorld().getManager(AreaManager.class).isAreaLoaded();

    if (type == ToolType.TILE_PERMISSIONS && areaLoaded) {
      fadeAreaModelAction(true);
    }
  }

  public EventBus getUIEventBus() {
    return editorToUIEventBus;
  }

  public EventBus getWorldEventBus() {
    return editorToWorldEventBus;
  }
  
  public AssetManager getAssetManager() {
    return overworld.getAssetManager();
  }

  public ToolbarState getToolbarState() {
    return toolbarState;
  }

  public TilePermissionListState getTilePermissionListState() {
    return tilePermissionListState;
  }

}
