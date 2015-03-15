package com.github.fauu.helix.editor.state;

import java.util.LinkedList;
import java.util.List;

import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;
import com.github.fauu.helix.editor.event.GeometryNameListStateChangedEvent;

public class GeometryNameListState {

  private List<GeometryNameEntry> data;
  
  public GeometryNameListState() {
    data = new LinkedList<>();
  }
  
  public void initialize(List<GeometryNameEntry> entries) {
    this.data = entries;
    
    postChangeEvent();
  }
  
  public List<GeometryNameEntry> getData() {
    return data;
  }
  
  public void setSelected(String name, boolean value) {
    for (GeometryNameEntry gne : data) {
      if (gne.getName().equalsIgnoreCase(name)) {
        gne.setSelected(value);
        
        break;
      }
    }
  }
  
  private void postChangeEvent() {
    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new GeometryNameListStateChangedEvent(data));
  }

}
