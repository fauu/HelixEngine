package com.github.fauu.helix.editor;

public enum ToolType {
  
  TILE_EDIT_TOOL,
  TILE_PAINT_TOOL;
  
  @Override
  public String toString() {
    switch (this) {
      case TILE_EDIT_TOOL: return "Tile Edit";
      case TILE_PAINT_TOOL: return "Tile Paint";
      default: throw new IllegalArgumentException();
    }
  }
  
}
