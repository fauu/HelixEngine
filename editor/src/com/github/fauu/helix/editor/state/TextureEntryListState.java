package com.github.fauu.helix.editor.state;

import java.util.LinkedList;
import java.util.List;

import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.TextureEntry;
import com.github.fauu.helix.editor.event.TextureEntryListStateChangedEvent;

public class TextureEntryListState {
  
  private List<TextureEntry> data;
  
  public TextureEntryListState() {
    data = new LinkedList<TextureEntry>();
  }
  
  public void initialize(List<TextureEntry> entries) {
    data = entries;

    postChangeEventToUI();
  }
  
  public List<TextureEntry> getData() {
    return data;
  }
  
  public void setSelected(String name, boolean value) {
    for (TextureEntry te : data) {
      if (te.getName().equalsIgnoreCase(name)) {
        te.setSelected(value);
      } else {
        te.setSelected(!value);
      }
    }

    postChangeEventToWorld();
  }
  
  private void postChangeEventToUI() {
    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new TextureEntryListStateChangedEvent(data));
  }

  private void postChangeEventToWorld() {
    HelixEditor.getInstance()
               .getWorldEventBus()
               .post(new TextureEntryListStateChangedEvent(data));
  }

}