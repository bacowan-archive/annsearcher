package moe.cowan.b.annsearcher.backend.XmlParsers;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.Collection;

import moe.cowan.b.annsearcher.backend.Anime;
import moe.cowan.b.annsearcher.backend.Ids.StringIdKey;
import moe.cowan.b.annsearcher.backend.xml.MalAnimeXmlParser;
import moe.cowan.b.annsearcher.backend.xml.MalListXmlParser;

/**
 * Created by user on 01/08/2015.
 */
public class MalAnimeXmlParserTest extends InstrumentationTestCase {

    private MalAnimeXmlParser parser;

    public void setUp() throws Exception {
        parser = new MalAnimeXmlParser();
    }

    public void test_ValidSingleAnimeParse() throws Exception {
        InputStream exampleAnimeInput = getMalExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertAnimeAreRecursivelyEqual(MalExampleAnimeBuilder.buildAnimeFromAnimeExample(), parsedAnime);
    }

    public void test_EnglishTitleAddedToSynonyms() throws Exception {
        InputStream exampleAnimeInput = getMalExampleWithEnglishTitleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertTrue(parsedAnime.getSynonyms().contains("B"));
    }

    public void test_ParseWithMdash() throws Exception {
        InputStream exampleAnimeInput = getMdashExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertEquals(parsedAnime.getTitle(), "-");
    }

    public void test_ParseWithLdquo() throws Exception {
        InputStream exampleAnimeInput = getLdquoExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertEquals(parsedAnime.getTitle(), "\"");
    }

    public void test_ParseWithRdquo() throws Exception {
        InputStream exampleAnimeInput = getRdquoExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertEquals(parsedAnime.getTitle(), "\"");
    }

    public void test_ParseWithHellip() throws Exception {
        InputStream exampleAnimeInput = getHellipExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertEquals(parsedAnime.getTitle(), "...");
    }

    public void test_ParseWithRsquo() throws Exception {
        InputStream exampleAnimeInput = getRsquoExampleInputStream();
        Anime parsedAnime = parser.parse(exampleAnimeInput).get(0);
        assertEquals(parsedAnime.getTitle(), "'");
    }

    private InputStream getMalExampleInputStream() throws Exception {
        return getInputStream("malAnimeExample.xml");
    }

    private InputStream getMalExampleWithEnglishTitleInputStream() throws Exception {
        return getInputStream("malAnimeExampleWithEnglishTitle.xml");
    }

    private InputStream getMdashExampleInputStream() throws Exception {
        return getInputStream("malMdashExample.xml");
    }

    private InputStream getLdquoExampleInputStream() throws Exception {
        return getInputStream("malLdquoExample.xml");
    }

    private InputStream getRdquoExampleInputStream() throws Exception {
        return getInputStream("malRdquoExample.xml");
    }

    private InputStream getHellipExampleInputStream() throws Exception {
        return getInputStream("malHellipExample.xml");
    }

    private InputStream getRsquoExampleInputStream() throws Exception {
        return getInputStream("malRsquoExample.xml");
    }

    private InputStream getInputStream(String streamName) throws Exception {
        return getInstrumentation().getContext().getResources().getAssets().open(streamName);
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
