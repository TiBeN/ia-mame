package org.tibennetwork.iamame.mame;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestMameVersion {

  @Test
  public void testParsingMameVersionParser0162()
      throws UnhandledMameVersionPatternException {

    String output =
        "M.A.M.E. v0.162 (Jul 19 2016) - Multiple Arcade Machine Emulator";
    MameVersion version = MameVersion.parseFromHelpOutput(output);

    assertThat(version.toString(), equalTo("0.162"));

  }

  @Test
  public void testParsingMameVersionParser0170()
      throws UnhandledMameVersionPatternException {

    String output = "MAME v0.170 (Jun 16 2016)";
    MameVersion version = MameVersion.parseFromHelpOutput(output);

    assertThat(version.toString(), equalTo("0.170"));

  }

  @Test
  public void testParsingMameVersionParser0175()
      throws UnhandledMameVersionPatternException {

    String output = "MAME v0.175 (mame0175)";
    MameVersion version = MameVersion.parseFromHelpOutput(output);

    assertThat(version.toString(), equalTo("0.175"));

  }

  @Test(expected = UnhandledMameVersionPatternException.class)
  public void testParsingMameWithUnhandledMameVersionParserPattern()
      throws UnhandledMameVersionPatternException {

    String output = "Mame unhandled pattern 0.xx";
    @SuppressWarnings("unused")
    MameVersion version = MameVersion.parseFromHelpOutput(output);

  }

  @Test
  public void testCompareToWithInferiorMinorVersion() {
    MameVersion version1 = new MameVersion("0.149");
    MameVersion version2 = new MameVersion("0.78");
    assertTrue(version1.compareTo(version2) > 0);
  }

  @Test
  public void testCompareToWithSuperiorMinorVersion() {
    MameVersion version1 = new MameVersion("0.78");
    MameVersion version2 = new MameVersion("0.149");
    assertTrue(version1.compareTo(version2) < 0);
  }

  @Test
  public void testCompareToWithEqualVersion() {
    MameVersion version1 = new MameVersion("0.149");
    MameVersion version2 = new MameVersion("0.149");
    assertTrue(version1.compareTo(version2) == 0);
  }

  @Test
  public void testCompareToWithInferiorMajorVersion() {
    MameVersion version1 = new MameVersion("1.0");
    MameVersion version2 = new MameVersion("0.149");
    assertTrue(version1.compareTo(version2) > 0);
  }

  @Test
  public void testCompareToWithSuperiorMajorVersion() {
    MameVersion version1 = new MameVersion("0.149");
    MameVersion version2 = new MameVersion("1.0");
    assertTrue(version1.compareTo(version2) < 0);
  }

}
