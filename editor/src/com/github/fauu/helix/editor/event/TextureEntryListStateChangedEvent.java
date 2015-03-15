package com.github.fauu.helix.editor.event;

import java.util.List;

import com.github.fauu.helix.editor.datum.TextureEntry;

public class TextureEntryListStateChangedEvent {

  private List<TextureEntry> message;
  
  public TextureEntryListStateChangedEvent(List<TextureEntry> message) {
    this.message = message;
  }
  
  public List<TextureEntry> getMessage() {
    return message;
  }

}
