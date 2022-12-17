package de.christophpircher.jfxbullshitbingo;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit API for loading the bullshit bingo.
 *
 * @author Christoph Pircher
 */
public interface BullshitBingoAPI {
    /**
     * Creates a call for loading the bullshit bingo.
     * @param ID File name of the bullshit bingo on the server. Appends '.json' to the filename automatically.
     * @return A call for loading the bullshit bingo.
     * */
    @GET("{ID}.json")
    Call<BullshitBingo> getBullshitBingo(@Path("ID") String ID);
}
