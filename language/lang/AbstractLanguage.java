package lang;

import appMain.gui.ZabTheme;

/**
 * An interface used to return all text values as displayed to a user for compatibility with other spoken languages. 
 * This simply defines a method for each piece of text which gets displayed to the user. 
 * The default implementation is for English in the US, {@link EnglishUS}, so use that as a reference for making new language. 
 * @author zrona
 */
public interface AbstractLanguage{

	// Misc terms
	/** Get the text for displaying the name of the Zab Application */
	public String appName();
	
	// Menu bar terms
	
	// File menu
	/** Get the text for displaying a title label which means a file, a directory in a computer */
	public String file();
	/** Get the text for displaying a title label which means to create a new file*/
	public String newFile();
	/** Get the text for displaying a title label which means to save something */
	public String save();
	/** Get the text for displaying a title label which means to save something with a file name */
	public String saveAs();
	/** Get the text for displaying when saving to a file fails */
	public String saveFail();
	/** Get the text for displaying when saving to a file succeeds */
	public String saveSuccess();
	/** Get the text for displaying a title label which means to load something */
	public String load();
	/** Get the text for displaying when loading from a file fails */
	public String loadFail();
	/** Get the text for displaying when loading from a file succeeds */
	public String loadSuccess();
	/** Get the text for displaying a title label which means to export something */
	public String export();
	/** Get the text for displaying when exporting to a file fails */
	public String exportFail();
	/** Get the text for displaying when exporting to a file succeeds */
	public String exportSuccess();
	
	// Edit menu
	/** Get the text for displaying a title label which means to edit something */
	public String edit();
	/** Get the text for displaying a title label which means to undo an action */
	public String undo();
	/** Get the text for displaying a title label which means to redo an action */
	public String redo();
	
	// Graphics menu
	/** Get the text for displaying a title label which means graphics related settings */
	public String graphics();
	
	// Theme menu
	/** Get the text for displaying a title label which means refers to a {@link ZabTheme}, or simply a theme */
	public String theme();
	/** Get the text for displaying a title label which refers to the default dark {@link ZabTheme} */
	public String darkTheme();
	/** Get the text for displaying a title label which refers to the default light {@link ZabTheme} */
	public String lightTheme();
	
	// Help menu
	/** Get the text for displaying a title label which means to aid the user by giving them information about the application */
	public String help();
	/** Get the text for displaying a title label which means to tell the user about how the can control the application, i.e. how to use the mouse and keyboard */
	public String controls();
	
	// Controls dialog
	/** Get the text for the ctrl key */
	public String ctrlKey();
	/** Get the text for the shift key */
	public String shiftKey();
	/** Get the text for the alt key */
	public String altKey();
	/** Get the text for the space bar key */
	public String spacebar();
	/** Get the text for the delete key */
	public String deleteKey();
	/** Get the text for the escape key */
	public String escapeKey();
	/** Get the text for the left bracket key */
	public String bracketLeft();
	/** Get the text for the right bracket key */
	public String bracketRight();
	/** Get the text for the mouse */
	public String mouse();
	/** Get the text for the left button on a mouse */
	public String leftClick();
	/** Get the text for the middle button on a mouse */
	public String middleClick();
	/** Get the text for the right button on a mouse */
	public String rightClick();
	/** Get the text for the mouse wheel part of a mouse */
	public String mouseWheel();
	/** Get the text describing clicking to select a note */
	public String clickSelectNote();
	/** Get the text describing clicking to select a line of notes */
	public String clickSelectNoteLine();
	/** Get the text describing clicking not on a note creating a box selection */
	public String createBoxSelection();
	/** Get the text describing that selecting replaces the current selection */
	public String selectingReplaces();
	/** Get the text describing that holding ctrl will add to a selection */
	public String addToSelection();
	/** Get the text describing that clicking a selected note will unselect it */
	public String clickToUnselect();
	/** Get the text describing that clicking a selection and dragging it will move it */
	public String clickDragMoveSelection();
	/** Get the text describing that clicking and dragging pans the camera */
	public String clickDragMoveCamera();
	/** Get the text describing placing an open note in a tab */
	public String placeOpenNote();
	/** Get the text describing how a mouse wheel zooms in and out */
	public String mouseWheelZoom();
	/** Get the text for a keyboard */
	public String keyboard();
	/** Get the text describing resetting the camera to a position */
	public String resetCamera();
	/** Get the text describing unselected every selected note */
	public String unselectSelection();
	/** Get the text describing deleting every selected note */
	public String deleteSelection();
	/** Get the text describing selecting every note in the tab */
	public String selectAllNotes();
	/** Get the text describing canceling a selection box and selection movement */
	public String cancelSelection();
	/** Get the text describing copying a selection */
	public String copy();
	/** Get the text describing pasting a copied a selection */
	public String paste();
	/** Get the text describing copying, then immediately deleting, a selection */
	public String cut();
	/** Get the text describing saving a tab */
	public String saveTab();
	/** Get the text describing saving a tab as a new file */
	public String saveAsTab();
	/** Get the text describing loading a tab */
	public String loadTab();
	/** Get the text describing opening the export window */
	public String exportWindow();
	/** Get the text describing creating a new tab */
	public String newTab();
	/** Get the text for the word for a the minus button */
	public String minus();
	/** Get the text for the word for a the plus button */
	public String plus();
	/** Get the text describing zooming out with the camera */
	public String zoomOut();
	/** Get the text describing zooming in with the camera */
	public String zoomIn();
	/** Get the text for the word for a numerical key */
	public String number();
	/** Get the text for describing typing the number of a note on a tab */
	public String typeTabNumber();
	/** Get the text for describing subtracting the value of the fret numbers by one for the selected notes on a tab */
	public String subtractFret();
	/** Get the text for describing increasing the value of the fret numbers by one for the selected notes on a tab */
	public String addFret();
	/** Get the text for describing subtracting an octave from the selected notes on a tab */
	public String subtractOctave();
	/** Get the text for describing adding an octave from the selected notes on a tab */
	public String addOctave();
	/** A version of {@link #subtractFret()} with fewer characters for a menu */
	public String subtractFretShort();
	/** A version of {@link #addFret()} with fewer characters for a menu */
	public String addFretShort();
	/** A version of {@link #subtractOctave()} with fewer characters for a menu */
	public String subtractOctaveShort();
	/** A version of {@link #addOctave()} with fewer characters for a menu */
	public String addOctaveShort();
	/** Get the text for describing what a tab modifier is */
	public String modifiersDescription();
	/** Get the text for describing pressing a key and that applies a modifier to a selection */
	public String pressKeyApplyModifier();
	/** Get the text for describing combining modifiers, i.e. not removing the old ones */
	public String combineModifiers();
	/** Get the text for describing clearing modifiers */
	public String clearModifiers();
	/** Get the text for describing a pull off note */
	public String pullOff();
	/** Get the text for describing a hammer on note */
	public String hammerOn();
	/** Get the text for describing sliding up to a note */
	public String slideUp();
	/** Get the text for describing sliding down to a note */
	public String slideDown();
	/** Get the text for describing bending to a note */
	public String bend();
	/** Get the text for describing a pinch harmonic */
	public String pinchHarmonic();
	/** Get the text for describing vibrato */
	public String vibrato();
	/** Get the text for describing a tap note */
	public String tap();
	/** Get the text for describing a ghost note */
	public String ghostNote();
	/** Get the text for describing a natural harmonic */
	public String naturalHarmonic();
	/** Get the text for a title describing a camera zooming in */
	public String cameraZooming();
	/** Get the text for describing zooming with the camera, only on the x axis */
	public String zoomOnlyX();
	/** Get the text for describing zooming with the camera, only on the y axis */
	public String zoomOnlyY();
	
	// File terms
	/** Get the text for describing the file type .zab, which is the file extension for saved, not exported, versions of tabs */
	public String zabExtensionDescription();
	/** Get the text for describing that a file path needs to be selected, and one has not yet been selected */
	public String noPathSelected();
	
	// Pop up warning terms
	/** Get the text for the title of a pop up box asking if the user wants to continue their action, as it would mean they'd lose work */
	public String workNotSavedTitle();
	/** Get the text for the description of a pop up box asking if the user wants to continue their action, as it would mean they'd lose work */
	public String workNotSavedDescription();
	
}
