package com.github.fauu.helix.editor.event;

import java.util.List;

import com.github.fauu.helix.editor.datum.GeometryNameEntry;

public class GeometryNameListStateChangedEvent {
  
  private List<GeometryNameEntry> message;
  
  public GeometryNameListStateChangedEvent(List<GeometryNameEntry> message) {
    this.message = message;
  }
  
  public List<GeometryNameEntry> getMessage() {
    return message;
  }

}
