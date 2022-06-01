package lang;

/**
 * An implementation of {@link AbstractLanguage} for English in the US
 * @author zrona
 */
public class EnglishUS implements AbstractLanguage{

	@Override
	public String appName(){ return "Zab Guitar Tab Editor"; }
	@Override
	public String file(){ return "File"; }
	@Override
	public String newFile(){ return "New"; }
	@Override
	public String save(){ return "Save"; }
	@Override
	public String saveAs(){ return "Save As"; }
	@Override
	public String saveFail(){ return this.save().concat(" failed"); }
	@Override
	public String saveSuccess(){ return this.save().concat(" successful"); }
	@Override
	public String load(){ return "Load"; }
	@Override
	public String loadFail(){ return this.load().concat(" failed"); }
	@Override
	public String loadSuccess(){ return this.load().concat(" successful"); }
	@Override
	public String export(){ return "Export"; }
	@Override
	public String exportFail(){ return this.export().concat(" failed"); }
	@Override
	public String exportSuccess(){ return this.export().concat(" successful"); }

	@Override
	public String edit(){ return "Edit"; }
	@Override
	public String undo(){ return "Undo"; }
	@Override
	public String redo(){ return "Redo"; }
	
	@Override
	public String graphics(){ return "Graphics"; }
	
	@Override
	public String theme(){ return "Theme"; }
	@Override
	public String darkTheme(){ return "Dark Theme"; }
	@Override
	public String lightTheme(){ return "Light Theme"; }
	
	@Override
	public String help(){ return "Help"; }
	@Override
	public String controls(){ return "Controls"; }

	@Override
	public String ctrlKey(){ return "ctrl"; }
	@Override
	public String shiftKey(){ return "shift"; }
	@Override
	public String altKey(){ return "alt"; }
	@Override
	public String spacebar(){ return "SPACE"; }
	@Override
	public String deleteKey(){ return "DELETE"; }
	@Override
	public String escapeKey(){ return "ESC"; }
	@Override
	public String bracketLeft(){ return "LEFT BRACKET"; }
	@Override
	public String bracketRight(){ return "RIGHT BRACKET"; }
	@Override
	public String mouse(){ return "Mouse"; }
	@Override
	public String leftClick(){ return "Left click"; }
	@Override
	public String middleClick(){ return "Middle click"; }
	@Override
	public String rightClick(){ return "Right click"; }
	@Override
	public String mouseWheel(){ return "Mouse wheel"; }
	@Override
	public String clickSelectNote(){ return "Click a note to select that note"; }
	@Override
	public String clickSelectNoteLine(){ return "Hold shift to select a line of notes"; }
	@Override
	public String createBoxSelection(){ return "Click and drag not on a note to create a box selection"; }
	@Override
	public String selectingReplaces(){ return "Selecting replaces the current selection"; }
	@Override
	public String addToSelection(){ return "Add to the selection"; }
	@Override
	public String clickToUnselect(){ return "Click a selected note to unselect it"; }
	@Override
	public String clickDragMoveSelection(){ return "Click and drag to move all selected notes"; }
	@Override
	public String clickDragMoveCamera(){ return "Click and drag to move the camera"; }
	@Override
	public String placeOpenNote(){ return "Place an open note on the tab"; }
	@Override
	public String mouseWheelZoom(){ return "Scroll to zoom in and out"; }
	@Override
	public String keyboard(){ return "Keyboard"; }
	@Override
	public String resetCamera(){ return "Reset camera"; }
	@Override
	public String unselectSelection(){ return "Unselect current selection"; }
	@Override
	public String deleteSelection(){ return "Delete current selection"; }
	@Override
	public String selectAllNotes(){ return "Select all notes"; }
	@Override
	public String cancelSelection(){ return "Cancel click and drag and selection box"; }
	@Override
	public String copy(){ return "Copy"; }
	@Override
	public String paste(){ return "Paste"; }
	@Override
	public String cut(){ return "Cut"; }
	@Override
	public String saveTab(){ return "Save tab"; }
	@Override
	public String saveAsTab(){ return "Save tab as a new file"; }
	@Override
	public String loadTab(){ return "Load a tab"; }
	@Override
	public String exportWindow(){ return "Open export window"; }
	@Override
	public String newTab(){ return "Create a new tab"; }
	@Override
	public String minus(){ return "MINUS"; }
	@Override
	public String plus(){ return "PLUS"; }
	@Override
	public String zoomOut(){ return "Zoom out"; }
	@Override
	public String zoomIn(){ return "Zoom in"; }
	@Override
	public String number(){ return "number"; }
	@Override
	public String typeTabNumber(){ return "Type a tab number on a selection"; }
	@Override
	public String subtractFret(){ return "Lower a selection by one fret"; }
	@Override
	public String addFret(){ return "Increase a selection by one fret"; }
	@Override
	public String subtractOctave(){ return "Lower a selection by an octave"; }
	@Override
	public String addOctave(){ return "Increase a selection by an octave"; }
	@Override
	public String subtractFretShort(){ return "-Fret"; }
	@Override
	public String addFretShort(){ return "+Fret"; }
	@Override
	public String subtractOctaveShort(){ return "-Octave"; }
	@Override
	public String addOctaveShort(){ return "+Octave"; }
	@Override
	public String modifiersDescription(){ return "Modifiers i.e. pull off, slide, etc."; }
	@Override
	public String pressKeyApplyModifier(){ return "Press key to apply modifier to selection"; }
	@Override
	public String combineModifiers(){ return "Combine modifiers"; }
	@Override
	public String clearModifiers(){ return "Clear modifiers"; }
	@Override
	public String pullOff(){ return "Pull off"; }
	@Override
	public String hammerOn(){ return "Hammer on"; }
	@Override
	public String slideUp(){ return "Slide up"; }
	@Override
	public String slideDown(){ return "Slide down"; }
	@Override
	public String bend(){ return "Bend"; }
	@Override
	public String pinchHarmonic(){ return "Pinch Harmonic"; }
	@Override
	public String vibrato(){ return "Vibrato"; }
	@Override
	public String tap(){ return "Tap"; }
	@Override
	public String ghostNote(){ return "Ghost note"; }
	@Override
	public String naturalHarmonic(){ return "Natural harmonic"; }
	@Override
	public String cameraZooming(){ return "Camera zooming"; }
	@Override
	public String zoomOnlyX(){ return "Zoom only on x axis"; }
	@Override
	public String zoomOnlyY(){ return "Zoom only on y axis"; }
	
	@Override
	public String zabExtensionDescription(){ return "Zab stringed instrument tablature"; }
	@Override
	public String noPathSelected(){ return "No path selected"; }
	
	@Override
	public String workNotSavedTitle(){ return "Work not saved"; }
	@Override
	public String workNotSavedDescription(){ return "You have unsaved work, are you sure you want to continue?"; }
}
