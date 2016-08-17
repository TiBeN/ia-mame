package org.tibennetwork.iamame.mame;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestMameVersionParser {

    @Test
    public void testParsingMameVersion0162 () 
            throws UnhandledMameVersionPatternException {

        String versionLine 
            = "M.A.M.E. v0.162 (Jul 19 2016) - Multiple Arcade Machine Emulator";
        MameVersionParser mvp = new MameVersionParser();

        assertThat(mvp.parse(versionLine), equalTo("0.162"));

    }    

    @Test
    public void testParsingMameVersion0170 () 
            throws UnhandledMameVersionPatternException {

        String versionLine = "MAME v0.170 (Jun 16 2016)";
        MameVersionParser mvp = new MameVersionParser();

        assertThat(mvp.parse(versionLine), equalTo("0.170"));

    }

    @Test
    public void testParsingMameVersion0175 () 
            throws UnhandledMameVersionPatternException {

        String versionLine = "MAME v0.175 (mame0175)";
        MameVersionParser mvp = new MameVersionParser();

        assertThat(mvp.parse(versionLine), equalTo("0.175"));

    }

    @Test(expected = UnhandledMameVersionPatternException.class)
    public void testParsingMameWithUnhandledMameVersionPattern () 
            throws UnhandledMameVersionPatternException{

        String versionLine = "Mame unhandled pattern 0.xx";
        MameVersionParser mvp = new MameVersionParser();
        
        mvp.parse(versionLine);

    }
}
