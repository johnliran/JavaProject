package model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Serializer {
    public void serializeToXML(Object objectToSerialize, String xmlFileName) throws Exception {
        FileOutputStream os = new FileOutputStream(xmlFileName);
        XMLEncoder encoder = new XMLEncoder(os);
        encoder.writeObject(objectToSerialize);
        encoder.close();
    }

    public Object deserializeXML(String xmlFileName) throws Exception {
        FileInputStream os = new FileInputStream(xmlFileName);
        XMLDecoder decoder = new XMLDecoder(os);
        Object deSerializedObject = decoder.readObject();
        decoder.close();
        return deSerializedObject;
    }
}
