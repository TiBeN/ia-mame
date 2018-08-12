package org.tibennetwork.iarcade.internetarchive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tibennetwork.iarcade.internetarchive.MachineRomSet.MachineRomSetFormat;
import org.tibennetwork.iarcade.mame.MameVersion;

/**
 * Generate XML RomSet files by scraping Internet Archive zipview content
 *
 * This is a quick'n'dirty tool.
 */
public class RomSetXmlGenerator {

  public static void main(String[] args) throws Exception {

    String type = args[0];
    String path = args[1];

    String html = null;

    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      html = sb.toString();
    } finally {
      br.close();
    }

    Document zipView = Jsoup.parse(html);

    switch (type) {

      case "machine-rom":
        generateMachineRomSetXml(zipView);
        break;

      case "softwarelist-rom":
        generateSoftwareListRomSetXml(zipView);
        break;

      case "machine-chd":
        generateMachineChdSetXml(zipView);
        break;

      case "softwarelist-chd":
        generateSoftwareListChdSetXml(zipView);
        break;

      default:
        System.err.println("ERROR: Unrecognized collection type argument");

    }
  }

  /**
   * Generate XML data for MachineRomSet
   */
  public static void generateMachineRomSetXml(Document zipView)
      throws Exception {

    Pattern p;
    Matcher m;

    // Scrap collection id

    Element caption = zipView.select("caption").first();
    p = Pattern.compile("listing\\ of\\ (.+)\\.[ziptar]{3}");
    m = p.matcher(caption.text());
    if (!m.matches()) {
      throw new RuntimeException(
          "Can't parse collection id from caption. Regex does not match");
    }
    String id = m.group(1);

    Elements trs = zipView.select("table[class=archext]").first()
        .select("tr:has(td:has(a))");

    Map<String, MachineRomSetFile> files = new HashMap<>();

    for (Element tr : trs) {

      Element a = tr.child(0).child(0);

      // Scrap URL

      String url = "http:" + a.attr("href").toString();
      p = Pattern.compile(".+/(.+)\\.zip");
      m = p.matcher(a.text());

      if (!m.matches()) {
        System.err.println("Skip non rom file: " + url);
        continue;
      }

      // Scrap machine name

      String machineName = m.group(1);

      // Scrap size
      //
      // There is scraping errors sometimes. To prevent this the size of
      // the children of tr is tested. A warning to stderr is reported in case

      String size;

      if (tr.children().size() <= 3) {
        System.err.println("Warning, can't parse size: " + tr);
        size = "0";
      } else {
        size = tr.child(3).text();
      }

      MachineRomSetFile file = new MachineRomSetFile(id, machineName,
          new URL(url), Long.parseLong(size));

      files.put(machineName, file);
    }

    MameVersion version = new MameVersion("0.150"); // Don't care
    MachineRomSetFormat format = MachineRomSetFormat.MERGED;

    MachineRomSet romSet =
        new MachineRomSet(version, new HashSet<Collection>(), format, files);

    JAXB.marshal(romSet, System.out);

  }

  public static void generateSoftwareListRomSetXml(Document zipView)
      throws Exception {

    Pattern p;
    Matcher m;

    // Scrap collection id

    Element caption = zipView.select("caption").first();
    p = Pattern.compile("listing\\ of\\ (.+)\\.zip");
    m = p.matcher(caption.text());
    if (!m.matches()) {
      throw new RuntimeException(
          "Can't parse collection id from caption. Regex does not match");
    }
    String id = m.group(1);

    Elements trs = zipView.select("table[class=archext]").first()
        .select("tr:has(td:has(a))");

    Map<String, Map<String, SoftwareListRomSetFile>> files = new HashMap<>();

    for (Element tr : trs) {

      Element a = tr.child(0).child(0);

      // Scrap URL

      String url = "http:" + a.attr("href").toString();
      p = Pattern.compile(".+/(.+)/(.+)\\.zip");
      m = p.matcher(a.text());

      if (!m.matches()) {
        System.err.println("Skip non rom file: " + url);
        continue;
      }

      // Scrap softwarelist name

      String listName = m.group(1);

      // Scrap software name

      String softwareName = m.group(2);

      // Scrap size
      //
      // There is scraping errors sometimes. To prevent this the size of
      // the children of tr is tested. A warning to stderr is reported in case

      String size;

      if (tr.children().size() <= 3) {
        System.err.println("Warning, can't parse size: " + tr);
        size = "0";
      } else {
        size = tr.child(3).text();
      }

      SoftwareListRomSetFile file = new SoftwareListRomSetFile(id, listName,
          softwareName, new URL(url), Long.parseLong(size));

      if (!files.containsKey(listName)) {
        files.put(listName, new HashMap<String, SoftwareListRomSetFile>());
      }
      files.get(listName).put(softwareName, file);
    }

    MameVersion version = new MameVersion("0.150"); // Don't care

    SoftwareListRomSet romSet =
        new SoftwareListRomSet(version, new HashSet<Collection>(), files);

    JAXB.marshal(romSet, System.out);

  }

  /**
   * Generate XML data for MachineRomSet
   */
  public static void generateMachineChdSetXml(Document zipView)
      throws Exception {

    Pattern p;
    Matcher m;

    // Scrap collection id

    Element caption = zipView.select("caption").first();
    p = Pattern.compile("listing\\ of\\ (.+)\\.[tarzip]{3}");
    m = p.matcher(caption.text());
    if (!m.matches()) {
      throw new RuntimeException(
          "Can't parse collection id from caption. Regex does not match");
    }
    String id = m.group(1);

    Elements trs = zipView.select("table[class=archext]").first()
        .select("tr:has(td:has(a))");

    Map<String, Map<String, MachineChdSetFile>> files = new HashMap<>();

    for (Element tr : trs) {

      Element a = tr.child(0).child(0);

      String url = "http:" + a.attr("href").toString();
      p = Pattern.compile(".+/(.+)/(.+\\.chd)");
      m = p.matcher(a.text());

      if (!m.matches()) {
        System.err.println("Skip non rom file: " + url);
        continue;
      }

      // Scrap machine name

      String machineName = m.group(1);

      // Scrap software name

      String chd = m.group(2);

      // Scrap size
      //
      // There is scraping errors sometimes. To prevent this the size of
      // the children of tr is tested. A warning to stderr is reported in case

      String size;

      if (tr.children().size() <= 3) {
        System.err.println("Warning, can't parse size: " + tr);
        size = "0";
      } else {
        size = tr.child(3).text();
      }

      MachineChdSetFile file = new MachineChdSetFile(id, machineName, chd,
          new URL(url), Long.parseLong(size));

      if (!files.containsKey(machineName)) {
        files.put(machineName, new HashMap<String, MachineChdSetFile>());
      }

      files.get(machineName).put(chd, file);
    }

    MameVersion version = new MameVersion("0.150"); // Don't care

    MachineChdSet chdSet =
        new MachineChdSet(version, new HashSet<Collection>(), files);

    JAXB.marshal(chdSet, System.out);

  }

  /**
   * Generate XML data for SoftwareListChdSet
   */
  public static void generateSoftwareListChdSetXml(Document zipView)
      throws Exception {

    Pattern p;
    Matcher m;

    // Scrap collection id

    Element caption = zipView.select("caption").first();
    p = Pattern.compile("listing\\ of\\ (.+)\\.zip");
    m = p.matcher(caption.text());
    if (!m.matches()) {
      throw new RuntimeException(
          "Can't parse collection id from caption. Regex does not match");
    }
    String id = m.group(1);

    Elements trs = zipView.select("table[class=archext]").first()
        .select("tr:has(td:has(a))");

    Map<String, Map<String, Map<String, SoftwareListChdSetFile>>> files = new HashMap<>();

    for (Element tr : trs) {

      Element a = tr.child(0).child(0);

      String url = "http:" + a.attr("href").toString();
      p = Pattern.compile(".+_(.+)/(.+)/(.+)\\.chd");
      m = p.matcher(a.text());

      if (!m.matches()) {
        System.err.println("Skip non rom file: " + url);
        continue;
      }

      // Scrap softwarelist name

      String listName = m.group(1);

      // Scrap software name

      String softwareName = m.group(2);

      // Scrap chd name

      String chdName = m.group(3);

      // Scrap size
      //
      // There is scraping errors sometimes. To prevent this the size of
      // the children of tr is tested. A warning to stderr is reported in case

      String size;

      if (tr.children().size() <= 3) {
        System.err.println("Warning, can't parse size: " + tr);
        size = "0";
      } else {
        size = tr.child(3).text();
      }

      SoftwareListChdSetFile file = new SoftwareListChdSetFile(id, listName, softwareName, chdName,
          new URL(url), Long.parseLong(size));

      if (!files.containsKey(listName)) {
        files.put(listName, new HashMap<String, Map<String, SoftwareListChdSetFile>>());
      }

      if (!files.get(listName).containsKey(softwareName)) {
        files.get(listName).put(softwareName, new HashMap<String, SoftwareListChdSetFile>());
      }

      files.get(listName).get(softwareName).put(chdName, file);
    }

    MameVersion version = new MameVersion("0.150"); // Don't care

    SoftwareListChdSet chdSet =
        new SoftwareListChdSet(version, new HashSet<Collection>(), files);

    JAXB.marshal(chdSet, System.out);

  }

}
