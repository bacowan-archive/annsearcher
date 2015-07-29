package moe.cowan.b.annsearcher.backend.database;

import android.content.Context;

import java.util.List;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.Id;
import moe.cowan.b.annsearcher.backend.PeopleOfTitle;
import moe.cowan.b.annsearcher.backend.xml.AnnXmlParser;
import moe.cowan.b.annsearcher.backend.xml.XmlParser;
import moe.cowan.b.annsearcher.exceptions.TitleNotFoundException;

/**
 * Created by user on 27/07/2015.
 */
public class AnnDatabaseProxy implements DatabaseInfoProxy {

    private RequestGetter requestGetter;
    private XmlParser<List<Anime>> xmlParser;

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
    public PeopleOfTitle getPeopleOfTitle(Id animeId) throws TitleNotFoundException {
        return null;
    }
}
