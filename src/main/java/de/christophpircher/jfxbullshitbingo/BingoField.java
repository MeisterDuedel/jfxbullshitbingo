package de.christophpircher.jfxbullshitbingo;

import javafx.scene.control.Label;

/**
 * A bingo field on the bingo "sheet".
 *
 * @author Christoph Pircher
 * */
public class BingoField extends Label {
    /**
     * Represents the state a bingo field can be in.
     * */
    public enum State{
        /** Bingo field has not been selected yet. */ UNSELECTED,
        /** Bingo field has been selected. */ SELECTED,
        /** Bingo field is part of a bingo. */ BINGO
    }

    /**
     * X (column) and Y (row) coordinates of the fields on the bingo "sheet",
     * */
    private final int x, y;

    /** Current state of the bingo field (unselected, selected, bingo) */
    private State state = State.UNSELECTED;

    /**
     * Is the field part of the top-left to bottom-right diagonal?
     * */
    private final boolean partOfDiagonalTLBR;

    /**
     * Is the field part of the top-right to bottom-left diagonal?
     * */
    private final boolean partOfDiagonalTRBL;

    /**
     * Creates a new bingo field.
     * @param x x coordinate (column)
     * @param y y coordinate (row)
     * @param text Word(s) on the bingo field
     * @param partOfDiagonalTLBR Part of top-left -> bottom-right diagonal?
     * @param partOfDiagonalTRBL Part of top-right -> bottom-left diagonal?
     */
    public BingoField(int x, int y,String text, boolean partOfDiagonalTLBR, boolean partOfDiagonalTRBL){
        super(text);
        this.x = x;
        this.y = y;
        this.partOfDiagonalTLBR = partOfDiagonalTLBR;
        this.partOfDiagonalTRBL = partOfDiagonalTRBL;
    }

    public State getState(){return state;}

    /**
     *  Sets the state to 'UNSELECTED' and removes the CSS classes 'bingo' and 'selected' if present.
     *  */
    public void unselect(){
        state = State.UNSELECTED;
        getStyleClass().removeAll("bingo","selected");
    }

    /**
     * Sets the state to 'SELECTED', adds the 'selected' CSS class and removes the 'bingo' CSS class if present.
     * */
    public void select(){
        state = State.SELECTED;
        getStyleClass().removeAll("bingo");
        getStyleClass().add("selected");
    }

    /**
     * Sets the state to 'BINGO', adds the 'bingo' CSS class and removes the 'selected' CSS class.
     * */
    public void bingo(){
        state = State.BINGO;
        getStyleClass().removeAll("selected");
        getStyleClass().add("bingo");
    }

    public int getX(){return x;}

    public int getY(){return y;}


    public boolean isPartOfDiagonalTLBR() {
        return partOfDiagonalTLBR;
    }

    public boolean isPartOfDiagonalTRBL() {
        return partOfDiagonalTRBL;
    }
}
