package com.example.handler;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.Base64;
import java.util.zip.Inflater;

public class TmxMapLoader {
	public boolean[][] loadCollisionLayer(String tmxFilePath) throws Exception {
        File f = new File(tmxFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(f);
        doc.getDocumentElement().normalize();

        NodeList layerList = doc.getElementsByTagName("layer");

        for (int i = 0; i < layerList.getLength(); i++) {
            Element layer = (Element) layerList.item(i);
            String layerName = layer.getAttribute("name");
            if ("collision".equalsIgnoreCase(layerName)) {

                int width = Integer.parseInt(layer.getAttribute("width"));
                int height = Integer.parseInt(layer.getAttribute("height"));

                Element data = (Element) layer.getElementsByTagName("data").item(0);
                String encoding = data.getAttribute("encoding");
                String compression = data.getAttribute("compression");
                String dataText = data.getTextContent().trim();

                int[] tileGids;

                if ("base64".equalsIgnoreCase(encoding)) {
                    byte[] decoded = Base64.getDecoder().decode(dataText);
                    if ("zlib".equalsIgnoreCase(compression)) {
                        Inflater inflater = new Inflater();
                        inflater.setInput(decoded);
                        byte[] result = new byte[width * height * 4];
                        inflater.end();

                        tileGids = new int[width * height];
                        for (int j = 0; j < width * height; j++) {
                            int i0 = j * 4;
                            tileGids[j] = ((result[i0] & 0xFF)) |
                                          ((result[i0 + 1] & 0xFF) << 8) |
                                          ((result[i0 + 2] & 0xFF) << 16) |
                                          ((result[i0 + 3] & 0xFF) << 24);
                        }
                    } else {
                        throw new UnsupportedOperationException("Unsupported compression: " + compression);
                    }
                } else if ("csv".equalsIgnoreCase(encoding)) {
                    String[] parts = dataText.split(",");
                    tileGids = new int[parts.length];
                    for (int j = 0; j < parts.length; j++) {
                        tileGids[j] = Integer.parseInt(parts[j].trim());
                    }
                } else {
                    throw new UnsupportedOperationException("Unsupported encoding: " + encoding);
                }

                boolean[][] walkableGrid = new boolean[width][height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int gid = tileGids[y * width + x];
                        walkableGrid[x][y] = (gid == 0);
                    }
                }

                return walkableGrid;
            }
        }

        throw new RuntimeException("No collision layer found in TMX file");
    }
}
