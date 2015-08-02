package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import org.mockito.verification.Timeout;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.Ids.StringIdGetter;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.Ids.StringIdSetter;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.xml.AnnXmlParser;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;
import moe.cowan.b.annsearcher.exceptions.DatabaseRuntimeException;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;
import moe.cowan.b.annsearcher.exceptions.XmlParserException;

/**
 * Created by user on 27/07/2015.
 */
public class AnnDatabaseProxy implements DatabaseInfoProxy {

    private RequestGetter requestGetter;
    private XmlParser<List<Anime>> xmlParser;
    private static final StringIdGetter idGetter = new StringIdGetter(StringIdKey.ANN);
    private static final StringIdSetter idSetter = new StringIdSetter(StringIdKey.ANN);

    public AnnDatabaseProxy() {
        this(null);
    }

    public AnnDatabaseProxy(Context context) {
        this(
                new BasicRequestGetter(context),
                new AnnXmlParser()
        );
    }

    public AnnDatabaseProxy(RequestGetter requestGetter, XmlParser<List<Anime>> xmlParser) {
        this.requestGetter = requestGetter;
        this.xmlParser = xmlParser;
    }

    @Override
    public void setContext(Context context) {
        requestGetter.setContext(context);
    }

    @Override
    public PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException {
        String xml = getResponse(animeId);
        Anime thisAnime = parseSingleAnimeResponse(xml);
        return thisAnime.getPeopleOfTitle();
    }

    private String getResponse(Id animeId) {
        try {
            return requestGetter.getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime=" + idGetter.getStringId(animeId));
        } catch (ExecutionException|InterruptedException|TimeoutException e) {
            throw new DatabaseRuntimeException(e);
        }
    }

    private Anime parseSingleAnimeResponse(String xml) {
        List<Anime> allAnime;
        try {
            allAnime = xmlParser.parse(xml);
        } catch (XmlPullParserException|IOException e) {
            throw new XmlParserException(e);
        }
        return allAnime.get(0);
    }

    public void getAnnIdsForAnime(Collection<Anime> animes) {
        for (Anime anime : animes)
            getAnnIdsForAnime(anime);
    }

    private void getAnnIdsForAnime(Anime anime) {
        Collection<String> malSynonyms = anime.getSynonyms();
        malSynonyms.add(anime.getTitle());
        String xml;

        for (String malAnimeSynonym : malSynonyms) {
            try {
                xml = requestGetter.getRequestByUrl("http://cdn.animenewsnetwork.com/encyclopedia/api.xml?title=~" + formatForUrl(malAnimeSynonym));
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                throw new DatabaseRuntimeException(e);
            }
            List<Anime> potentialAnnMatches;
            try {
                potentialAnnMatches = xmlParser.parse(xml);
            } catch (XmlPullParserException | IOException e) {
                throw new XmlParserException(e);
            }
            for (Anime potentialAnnMatch : potentialAnnMatches) {
                Collection<String> potentialAnnSynonyms = potentialAnnMatch.getSynonyms();
                potentialAnnSynonyms.add(potentialAnnMatch.getTitle());
                if (potentialAnnSynonyms.contains(malAnimeSynonym)) {
                    idSetter.setString(anime.getId(), potentialAnnMatch.getId().getString(StringIdKey.ANN));
                    return;
                }
                /*for (String malSynonym : malSynonyms) {
                    if (potentialAnnSynonyms.contains(malSynonym)) {
                        idSetter.setString(anime.getId(), potentialAnnMatch.getId().getString(StringIdKey.MAL));
                        return;
                    }
                }*/
            }
        }
    }

    private String formatForUrl(String str) {
        return str.replaceAll(" ", "%20");
    }

}
