package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.Direction;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;
import com.github.fauu.helix.editor.datum.TextureEntry;
import com.github.fauu.helix.editor.event.GeometryNameListStateChangedEvent;
import com.github.fauu.helix.editor.event.TextureEntryListStateChangedEvent;
import com.github.fauu.helix.editor.event.TilePropertiesStateChangedEvent;
import com.google.common.eventbus.Subscribe;

public class TilePropertiesState {
  
  private Tile data;
  
  public TilePropertiesState() {
    data = new Tile();
  }
  
  public void initialize() {
    HelixEditor.getInstance().getWorldEventBus().register(this);

    data.setOrientation(Direction.SOUTH);
    data.setGeometryName("flat");
    data.setTextureName("grass1");
    data.setElevation(0);
    
    postChangeEventToUI();
  }
  
  public Tile getData() {
    return data;
  }
  
  public void setOrientation(Direction direction) {
    data.setOrientation(direction);
    
    postChangeEventToUI();
  }
  
  public void setElevation(int value) {
    data.setElevation(value);
  }
  
  private void postChangeEventToUI() {
    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new TilePropertiesStateChangedEvent(data));
  }

  private void postChangeEventToWorld() {
    HelixEditor.getInstance()
               .getWorldEventBus()
               .post(new TilePropertiesStateChangedEvent(data));
  }
  
  @Subscribe
  public void geometryNameListStateChanged(GeometryNameListStateChangedEvent e) {
    for (GeometryNameEntry gne : e.getMessage()) {
      if (gne.isSelected()) {
        data.setGeometryName(gne.getName());
        
        break;
      }
    }
  }

  @Subscribe
  public void textureEntryListStateChanged(TextureEntryListStateChangedEvent e) {
    for (TextureEntry te : e.getMessage()) {
      if (te.isSelected()) {
        data.setTextureName(te.getName());
        
        break;
      }
    }
    
    postChangeEventToWorld();
  }

}
