package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;

/**
 * Created by user on 01/08/2015.
 */
public class AnnMalDatabaseProxy implements DatabaseProxy {

    private AnnDatabaseProxy annDatabaseProxy;
    private MalDatabaseProxy malDatabaseProxy;

    public AnnMalDatabaseProxy() {
        annDatabaseProxy = new AnnDatabaseProxy();
        malDatabaseProxy = new MalDatabaseProxy();
    }

    public AnnMalDatabaseProxy(Parcel in) {
        this();
        setUsername(in.readString());
    }

    @Override
    public PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException {
        return annDatabaseProxy.getPeopleOfTitle(animeId);
    }

    @Override
    public Collection<Anime> getAllSeenAnime() {
        Collection<Anime> allSeenAnime = malDatabaseProxy.getAllSeenAnime();
        annDatabaseProxy.getAnnIdsForAnime(allSeenAnime);
        return allSeenAnime;
    }

    @Override
    public void setUsername(String username) {
        malDatabaseProxy.setUsername(username);
    }

    @Override
    public String getUsername() {
        return malDatabaseProxy.getUsername();
    }

    @Override
    public void setContext(Context context) {
        annDatabaseProxy.setContext(context);
        malDatabaseProxy.setContext(context);
    }

    @Override
    public void resync() {
        malDatabaseProxy.resync();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUsername());
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
