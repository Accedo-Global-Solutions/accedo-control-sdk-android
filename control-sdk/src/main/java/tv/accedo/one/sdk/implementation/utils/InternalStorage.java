/*
 * Copyright (c) 2016 - present Accedo Broadband AB. All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package tv.accedo.one.sdk.implementation.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This util can be used to persistently store serializable objects in the device's internal private storage.
 *
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
public class InternalStorage {

    /**
     * Serializes an object into a file, stored in the application's private file storage.
     *
     * @param context  Needed for opening the application's private file storage.
     * @param object   The object to serialize.
     * @param filename The filename to write into.
     */
    public static boolean write(Context context, Object object, String filename) {
        if (object == null) {
            return false;
        }

        ObjectOutputStream oos = null;
        FileOutputStream fos = null;

        try {
            fos = context.getApplicationContext().openFileOutput(filename, Activity.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            fos.getFD().sync();
        } catch (Exception e) {
            Utils.log(e);
            delete(context, filename);
            return false;
        } finally {
            Utils.closeQuietly(oos);
            Utils.closeQuietly(fos);
        }

        return true;
    }

    /**
     * Deserializes and returns an object from a given file, stored in the application's private file storage.
     *
     * @param context  Needed for opening the application's private file storage.
     * @param filename The filename to read from.
     * @return The deserialized object, or null if unsuccessful.
     */
    public static Object read(Context context, String filename) {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        Object result = null;

        try {
            fis = context.getApplicationContext().openFileInput(filename);
            ois = new ObjectInputStream(fis);
            result = ois.readObject();
        } catch (FileNotFoundException e) {
            Utils.log(Log.INFO, "ObjectToFile: File not found: " + filename);
        } catch (Exception e) {
            Utils.log(e);
            delete(context, filename);
        } finally {
            Utils.closeQuietly(ois);
            Utils.closeQuietly(fis);
        }

        return result;
    }

    /**
     * Deletes a file, stored in the application's private file storage, and
     * returns if the deletion was successful or no.
     *
     * @param context  Needed for opening the application's private file storage.
     * @param filename The filename to delete.
     * @return boolean telling if the deletion was successful or no.
     */
    public static boolean delete(Context context, String filename) {
        return context.getApplicationContext().deleteFile(filename);
    }

    /**
     * Checks if a file already exists or no.
     *
     * @param context  Needed for opening the application's private file storage.
     * @param filename The filename to check.
     * @return boolean telling if the file already exists or no.
     */
    public static boolean exists(Context context, String filename) {
        try {
            context.getApplicationContext().openFileInput(filename).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}