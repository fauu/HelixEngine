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

package com.github.fauu.helix.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;
import com.github.fauu.helix.editor.datum.TextureEntry;
import com.github.fauu.helix.editor.event.GeometryNameListStateChangedEvent;
import com.github.fauu.helix.editor.event.TextureEntryListStateChangedEvent;
import com.github.fauu.helix.editor.event.TilePropertiesStateChangedEvent;
import com.google.common.eventbus.Subscribe;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class HETileCustomizationPanel extends VisTable {
  
  private TextButton selectOrientationButton;
  
  private List<String> geometryList;
  
  private Table textureList;
  
  private VisSlider elevationSlider;
  
  public HETileCustomizationPanel() {
    super(true);
    
    HelixEditor.getInstance().getUIEventBus().register(this);

//    add(new VisLabel("Tile Customization")).center().colspan(2);
//    
//    row().expandX();
    
    add(new VisLabel("Tile orientation:")).left().padLeft(10).padBottom(-5).colspan(2);

    row();
    
    selectOrientationButton = new VisTextButton("");
    selectOrientationButton.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HEOrientationSelectionPopout orientationSelectionPopout
                = new HEOrientationSelectionPopout(selectOrientationButton);

            getStage().addActor(orientationSelectionPopout.fadeIn());
          }
        });

    add(selectOrientationButton).colspan(2);
    
    row();

    add(new VisLabel("Tile geometry:")).left().padLeft(10).padBottom(-5).colspan(2);

    row();
    
    geometryList = new VisList<String>();
    geometryList.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance()
                       .getGeometryNameListState()
                       .setSelected(geometryList.getSelected(), true);
          }
        });
    ScrollPane geometryListScrollPane = new VisScrollPane(geometryList);
    add(geometryListScrollPane).colspan(2);

    row();

    add(new VisLabel("Tile texture:")).left().padLeft(10).padBottom(-5).colspan(2);

    row();

    // TODO: Abstract this out into a widget
    textureList = new VisTable();
    ScrollPane textureListScrollPane = new VisScrollPane(textureList);
    add(textureListScrollPane).colspan(2);
    
    row();

    add(new VisLabel("Tile elevation:")).left().padLeft(10).padBottom(-5).colspan(2);

    row();
    
    final Label elevationLabel = new VisLabel("");

    elevationSlider = new VisSlider(-16, 16, 1, false);
    elevationSlider.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance()
                       .getTilePropertiesState()
                       .setElevation((int) elevationSlider.getValue());

            elevationLabel.setText(
                String.valueOf((int) elevationSlider.getValue()));
          }
        });
    add(elevationSlider);

    add(elevationLabel).width(30).padLeft(5);
    
    row();

    pack();
  }

  @Subscribe
  public void tilePropertiesStateChanged(TilePropertiesStateChangedEvent e) {
    selectOrientationButton.setText(e.getMessage().getOrientation().toString());
    
    elevationSlider.setValue(e.getMessage().getElevation());
  }

  @Subscribe
  public void geometryNameListStateChanged(GeometryNameListStateChangedEvent e) {
    java.util.List<GeometryNameEntry> entries = e.getMessage();
    
    Array<String> names = new Array<>();
    for (GeometryNameEntry gne : entries) {
      names.add(gne.getName());
    }

    geometryList.setItems(names);
  }
  
  @Subscribe
  public void textureEntryListStateChanged(TextureEntryListStateChangedEvent e) {
    textureList.clearChildren();
    
    java.util.List<TextureEntry> entries = e.getMessage();
    
    ButtonGroup<Button> selectTextureButtonGroup = new ButtonGroup<Button>();
    
    for (TextureEntry te : entries) {
      Image preview = new VisImage(te.getPreview());

      Button selectTextureButton = new VisImageButton("toggle");
      selectTextureButton.setName(te.getName());
      selectTextureButton.add(preview).size(32, 32);
      selectTextureButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              if (((Button) actor).isChecked()) {
                HelixEditor.getInstance()
                           .getTextureEntryListState()
                           .setSelected(((Button) actor).getName(), true);
              }
            }
          });
      selectTextureButtonGroup.add(selectTextureButton);

      textureList.add(selectTextureButton).width(38).height(38).padRight(3);
    }
  }

}
