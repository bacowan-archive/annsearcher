package moe.cowan.b.annsearcher.backend.Database;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.database.AnnDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.AnnMalDatabaseProxy;
import moe.cowan.b.annsearcher.backend.database.MalDatabaseProxy;

import static org.mockito.Mockito.*;

/**
 * Created by Brendan on 11/20/2015.
 */
public class AnnMalDatabaseProxyTest extends InstrumentationTestCase {

    private AnnMalDatabaseProxy proxy;
    private AnnDatabaseProxy annDatabaseProxy;
    private MalDatabaseProxy malDatabaseProxy;

    public void setUp() {
        Utils.setupDexmaker(getInstrumentation());
        annDatabaseProxy = mock(AnnDatabaseProxy.class);
        malDatabaseProxy = mock(MalDatabaseProxy.class);
        proxy = new AnnMalDatabaseProxy(annDatabaseProxy, malDatabaseProxy);
    }

    public void test_Resync_CallsMal() {
        proxy.resync();
        verify(malDatabaseProxy).resync();
    }

    public void test_Search_CallsMal() {
        String searchString = "string";

        proxy.searchAnime(searchString);

        verify(malDatabaseProxy).searchAnime(searchString);
    }

    public void test_Search_QuerriesAnnForIds() {
        String searchString = "string";
        Anime anime = Utils.createBlankAnime();
        Collection<Anime> searchResults = new ArrayList<>();
        searchResults.add(anime);
        when(malDatabaseProxy.searchAnime(anyString())).thenReturn(searchResults);

        proxy.searchAnime(searchString);

        verify(annDatabaseProxy).getAnimeInformation(searchResults);
    }

    public void test_GetAllSeenAnime_CachesToInternalDatabase() {
        Anime anime = Utils.createBlankAnime();
        Collection<Anime> results = new ArrayList<>();
        results.add(anime);
        when(malDatabaseProxy.getAllSeenAnime()).thenReturn(results);

        proxy.getAllSeenAnime();

        verify(annDatabaseProxy).cacheAnime(results);
    }
}
