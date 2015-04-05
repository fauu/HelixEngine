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

import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.editor.event.AreaLoadedEvent;
import com.github.fauu.helix.editor.event.AreaUnloadedEvent;
import com.github.fauu.helix.editor.screen.Overworld;
import com.github.fauu.helix.editor.state.TilePermissionListState;
import com.github.fauu.helix.editor.state.ToolbarState;
import com.github.fauu.helix.editor.ui.UI;
import com.github.fauu.helix.manager.AreaManager;
import com.google.common.eventbus.EventBus;

public class HelixEditor extends Game {

  private static HelixEditor instance;

  private EventBus uiEventBus;

  private EventBus worldEventBus;

  private UI ui;

  private ToolbarState toolbarState;

  private TilePermissionListState tilePermissionListState;

  private Overworld overworld;

  private TagManager tagManager;

  private AreaManager areaManager;

  @Override
  public void create() {
    instance = this;
    
    uiEventBus = new EventBus();
    worldEventBus = new EventBus();

    overworld = new Overworld();
    setScreen(overworld);

    toolbarState = new ToolbarState();
    toolbarState.initialize(ToolType.TILE_PERMISSIONS);

    tilePermissionListState = new TilePermissionListState();
    tilePermissionListState.initialize(TilePermission.LEVEL0);

    ui = new UI();

    Gdx.input.setInputProcessor(
        new InputMultiplexer(ui.getStage(), new EditorInputEventProcessor()));

    tagManager = overworld.getWorld().getManager(TagManager.class);
    areaManager = overworld.getWorld().getManager(AreaManager.class);

    loadAreaAction("area1");
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

  public void newAreaAction() {
    ui.showNewAreaDialog();
  }

  public void createAreaAction(String name, int width, int length) {
    areaManager.create(name, width, length);
  }

  public void openAreaAction() {
    ui.showOpenAreaFileChooser();
  }

  public void loadAreaAction(String name) {
    if (areaManager.isAreaLoaded()) {
      closeCurrentAreaAction();
    }

    areaManager.load(name);

    worldEventBus.post(new AreaLoadedEvent());

    ui.setSidebarVisibility(true);

    if (toolbarState.getActiveTool() == ToolType.TILE_PERMISSIONS) {
      fadeAreaModelAction(true);
    }
  }

  public void saveAreaAction() {
    areaManager.save();
  }

  public void closeCurrentAreaAction() {
    areaManager.unloadCurrent();

    worldEventBus.post(new AreaUnloadedEvent());

    ui.setSidebarVisibility(false);
  }


  public void exitAction() {
    Gdx.app.exit();
  }

  public void fadeAreaModelAction(boolean on) {
    overworld.getDisplayableIntermediary().setAreaDisplayableOpacity(on ? 0.6f : 1);
  }

  public void centerCameraOnAreaAction() {
    overworld.getCameraIntermediary().centerCameraOnArea();
  }

  public void toolSelected(ToolType type) {
    if (type == ToolType.TILE_PERMISSIONS &&
        areaManager != null &&
        areaManager.isAreaLoaded()) {
      fadeAreaModelAction(true);
    }
  }

  public EventBus getUIEventBus() {
    return uiEventBus;
  }

  public EventBus getWorldEventBus() {
    return worldEventBus;
  }
  
  public ToolbarState getToolbarState() {
    return toolbarState;
  }

  public TilePermissionListState getTilePermissionListState() {
    return tilePermissionListState;
  }

}
