package com.github.fauu.helix.editor.event;

import com.github.fauu.helix.datum.Tile;

public class TilePropertiesStateChangedEvent {
  
  private Tile message;
  
  public TilePropertiesStateChangedEvent(Tile message) {
    this.message = message;
  }
  
  public Tile getMessage() {
    return message;
  }

}
