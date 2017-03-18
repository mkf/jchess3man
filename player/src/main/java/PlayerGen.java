/**
 * Created by Michał Krzysztof Feiler on 18.03.17.
 */
public interface PlayerGen {
    void Start();

    Player genPlayer(String name);

    String toString();
}
