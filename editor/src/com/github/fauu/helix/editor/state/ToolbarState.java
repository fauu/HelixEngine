package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.editor.ToolType;

public class ToolbarState {
  
  private ToolType activeTool;
  
  public ToolbarState() { }
  
  public void initialize(ToolType type) {
    activeTool = type;
  }
  
  public ToolType getActiveTool() {
    return activeTool;
  }
  
  public void setActiveTool(ToolType type) {
    activeTool = type;
  }

}
