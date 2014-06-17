package com.intel.store.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import android.content.Context;

public class FileHelper {
    
    public static String getFilesDirPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator + "pck";
    }
    
    public static void saveToFile(Context context, Object obj) {
        try {
            File directory = new File(getFilesDirPath(context));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String absolutePckFileName = getFilesDirPath(context) + File.separator + newRandomUUID() + ".pck";
            PictureItem pictureItem = (PictureItem) obj;
            pictureItem.mAbsolutePckFileName = absolutePckFileName;
            FileOutputStream fos = new FileOutputStream(absolutePckFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static PictureItem inflatePictureItemFromFile(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            PictureItem pictureItem = (PictureItem) ois.readObject();
            ois.close();
            return pictureItem;
        } catch (Exception e) {
            deleteFile(new File(filename));
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
    
    public static void deleteFile(String absolutFilePath) {
        File file = new File(absolutFilePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    private static String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }
}