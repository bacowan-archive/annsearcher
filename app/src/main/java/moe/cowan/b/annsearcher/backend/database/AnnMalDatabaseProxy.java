package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.database.internalDatabase.SqliteDatabaseProxy;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;

/**
 * Created by user on 01/08/2015.
 */
public class AnnMalDatabaseProxy implements DatabaseProxy {

    private AnnDatabaseProxy annDatabaseProxy;
    private MalDatabaseProxy malDatabaseProxy;
    private String internalDbName;

    public AnnMalDatabaseProxy() {
        this.annDatabaseProxy = new AnnDatabaseProxy();
        this.malDatabaseProxy = new MalDatabaseProxy();
    }

    public AnnMalDatabaseProxy(AnnDatabaseProxy annProxy, MalDatabaseProxy malProxy) {
        this.annDatabaseProxy = annProxy;
        this.malDatabaseProxy = malProxy;
    }

    public AnnMalDatabaseProxy(Parcel in) {
        this();
        internalDbName = in.readString();
        setUsername(in.readString());
        setPassword(in.readString());
    }

    @Override
    public PeopleOfTitle getPeopleOfTitle(Anime anime) throws TitleNotFoundException {
        return annDatabaseProxy.getPeopleOfTitle(anime);
    }

    @Override
    public Collection<Anime> getAllSeenAnime() {
        Collection<Anime> allSeenAnime = malDatabaseProxy.getAllSeenAnime();
        annDatabaseProxy.getInternalIdsForAnime(allSeenAnime);
        annDatabaseProxy.getAnimeInformation(allSeenAnime);
        annDatabaseProxy.cacheAnime(allSeenAnime);
        return allSeenAnime;
    }

    @Override
    public void setUsername(String username) {
        internalDbName = "animeDatabase_" + username + ".db";
        malDatabaseProxy.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        malDatabaseProxy.setPassword(password);
    }

    @Override
    public String getUsername() {
        return malDatabaseProxy.getUsername();
    }

    @Override
    public void setContext(Context context) {
        annDatabaseProxy.setContext(context);
        annDatabaseProxy.setInternalProxy(new SqliteDatabaseProxy(context, internalDbName));
        malDatabaseProxy.setContext(context);
    }

    @Override
    public void resync() {
        malDatabaseProxy.resync();
    }

    @Override
    public Collection<Anime> searchAnime(String searchString) {
        Collection<Anime> searchResults = malDatabaseProxy.searchAnime(searchString);
        //annDatabaseProxy.getAnimeInformation(searchResults);
        // TODO: the above line should not execute until the information is needed.
        return searchResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(internalDbName);
        dest.writeString(getUsername());
        dest.writeString(malDatabaseProxy.getPassword());
    }

    public static final Parcelable.Creator<AnnMalDatabaseProxy> CREATOR =
            new Parcelable.Creator<AnnMalDatabaseProxy>() {

                @Override
                public AnnMalDatabaseProxy createFromParcel(Parcel source) {
                    return new AnnMalDatabaseProxy(source);
                }

                @Override
                public AnnMalDatabaseProxy[] newArray(int size) {
                    return new AnnMalDatabaseProxy[size];
                }
            };

}
