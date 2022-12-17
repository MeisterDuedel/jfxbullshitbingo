package de.christophpircher.jfxbullshitbingo;

import java.util.List;

/**
 * A Bullshit Bingo. This class is used to fill the bingo fields with words and to set the title label.
 * Objects from this class are instantiated by retrofit2 and gson.
 * @author Christoph Pircher
 * */
public class BullshitBingo {
    /**
     * Title/name of the bullshit bingo
     * */
    private String title;

    /**
     * A List of words for the bingo fields. It has to contain at least 25 strings.
     * */
    private List<String> words;

    public String getTitle(){return title;}

    /**
     * Chooses a random word/string from the list, removes it and returns it
     * @return A random word/string from the list.
     * @throws IllegalStateException If the list is empty. This only happens if there are less than 25 words in the .json file.
     * */
    public String getRandomWord() throws IllegalStateException{
        if(words.size() > 0){
            return (words.remove((int)Math.round(Math.random()*(words.size()-1))));
        }else {
            throw new IllegalStateException("Not enough items in the List!");
        }
    }


}
