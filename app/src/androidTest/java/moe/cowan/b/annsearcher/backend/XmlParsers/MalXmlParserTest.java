package moe.cowan.b.annsearcher.backend.XmlParsers;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.xml.MalXmlParser;

/**
 * Created by user on 01/08/2015.
 */
public class MalXmlParserTest extends InstrumentationTestCase {

    private MalXmlParser parser;

    public void setUp() throws Exception {
        parser = new MalXmlParser();
    }

    public void testValidSingleAnimeParse() throws Exception {
        InputStream exampleAnimeInput = getMalExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertAnimeAreRecursivelyEqual(MalExampleAnimeBuilder.buildAnime(), parsedAnime);
    }

    public void testParseSynonymsWithSemicolonInTitle() throws Exception {
        String xmlSysonyms = "; Steins;Gate";
        Collection<String> parsedSynonyms = MalXmlParser.parseSynonyms(xmlSysonyms);
        assertEquals(1, parsedSynonyms.size());
        assertEquals("Steins;Gate", parsedSynonyms.toArray()[0]);
    }

    private InputStream getMalExampleInputStream() throws Exception {
        return getInstrumentation().getContext().getResources().getAssets().open("malExample.xml");
    }

    private void assertAnimeAreRecursivelyEqual(Anime expectedAnime, Anime anime2) {
        assertEquals(expectedAnime.getTitle(), anime2.getTitle());
        assertEquals(expectedAnime.getId().getString(StringIdKey.MAL), anime2.getId().getString(StringIdKey.MAL));
        assertSynonymsEqual(expectedAnime.getSynonyms(), anime2.getSynonyms());
        assertEquals(expectedAnime.getStatus(), anime2.getStatus());
    }

    private void assertSynonymsEqual(Collection<String> expectedSynonyms, Collection<String> actualSynonyms) {
        assertEquals(expectedSynonyms.size(), actualSynonyms.size());
        assertTrue(expectedSynonyms.containsAll(actualSynonyms));
    }

}
