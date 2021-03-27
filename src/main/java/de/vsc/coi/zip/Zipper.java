package de.vsc.coi.zip;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vsc.coi.zip.ZipAdapter.ZipException;

public class Zipper {

    private static final Logger LOGGER = LogManager.getLogger(Zipper.class);
    private static final List<ZipAdapter> zipAdapter = List.of(new SevenZipAdapter(), new JavaZipAdapter());

    private String getOs() {
        return System.getProperty("os.name").toLowerCase();
    }

    public void zip(final File input, final File outputDir, final String zipName) throws ZipException {
        final String os = getOs();
        final ZipAdapter zipAdapter = Zipper.zipAdapter.stream()
                .filter(x -> x.operatingSystemMatches(os))
                .filter(ZipAdapter::toolIsAvailable)
                .findFirst()
                .orElseThrow(() -> new ZipException("No matching zip adapter found."));

        LOGGER.info("Using: " + zipAdapter.toolName());
        zipAdapter.execute(input, outputDir, zipName);
    }

}
