package edu.usc.softarch.disco.util;

import java.io.*;
import java.net.URL;
import java.net.JarURLConnection;
import java.util.jar.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.ArrayList;

/**
    * Runtime Subclass Identification.
    * Utility class to determine what classes implement or inherit 
    * froma given superclass. Requires that .class files for the 
    * current application are available either via the filesystem or 
    * a JAR file.
    *
    * Based off of sample code provided at <a href="http://www.javaworld.com/javaworld/javatips/jw-javatip113.html">
    * http://www.javaworld.com/javaworld/javatips/jw-javatip113.html</a>.
    *
    * Originally written by Daniel Le Berre.
    * Modified by Trevor Johns.
    * 
    * @author <a href="mailto:daniel@satlive.org">Daniel Le Berre</a>
    * @author <a href="mailto:tjohns@usc.edu">Trevor Johns</a>
    * @version 1.0
    */
public final class RTSI {
    /**
        * Display all the classes inheriting or implementing a given
        * class in the currently loaded packages.
        * @param tosubclassname the name of the class to inherit from
        */
    public static ArrayList<Class> find(String tosubclassname) {
        ArrayList<Class> current;
        ArrayList<Class> results = new ArrayList<Class>();
        
        try {
            Class tosubclass = Class.forName(tosubclassname);
            Package [] pcks = Package.getPackages();
            System.out.println("Length2: " + pcks.length);
            for (int i=0; i<pcks.length; i++) {
                System.out.println("Bob!");
                current = find(pcks[i].getName(), tosubclass);
                results.addAll(current);
            }
        } catch (ClassNotFoundException ex) {
            // No matches found.
            System.err.println("Error: RTSI superclass not found.");
            ex.printStackTrace();
        }
        return results;
    }

    /**
        * Display all the classes inheriting or implementing a given
        * class in a given package.
        * @param pckgname the fully qualified name of the package
        * @param tosubclass the name of the class to inherit from
        */
    public static ArrayList<Class> find(String pckname, String tosubclassname) {
        try {
            Class tosubclass = Class.forName(tosubclassname);
            return find(pckname, tosubclass);
        } catch (ClassNotFoundException ex) {
            // No matches found
            System.err.println("Error: RTSI superclass not found.");
            ex.printStackTrace();
            return new ArrayList<Class>();
        }
    }

    /**
        * Display all the classes inheriting or implementing a given
        * class in a given package.
        * @param pckgname the fully qualified name of the package
        * @param tosubclass the Class object to inherit from
        */
    public static ArrayList<Class> find(String pckgname, Class tosubclass) {
        ArrayList<Class> results = new ArrayList<Class>();
        
        // Code from JWhich
        // ======
        // Translate the package name into an absolute path
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }	
        name = name.replace('.', '/');

        // Get a File object for the package
        URL url = RTSI.class.getResource(name);
        // URL url = tosubclass.getResource(name);
        // URL url = ClassLoader.getSystemClassLoader().getResource(name);
        // System.out.println(name+"->"+url);

        // Happens only if the jar file is not well constructed, i.e.
        // if the directories do not appear alone in the jar file like here:
        // 
        //          meta-inf/
        //          meta-inf/manifest.mf
        //          commands/                  <== IMPORTANT
        //          commands/Command.class
        //          commands/DoorClose.class
        //          commands/DoorLock.class
        //          commands/DoorOpen.class
        //          commands/LightOff.class
        //          commands/LightOn.class
        //          RTSI.class
        //
        if (url==null) return results;

        File directory = new File(url.getFile());

        // New code
        // ======
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String [] files = directory.list();
            for (int i=0; i<files.length; i++) {

                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String classname = files[i].substring(0, files[i].length()-6);
                    try {
                        // Try to create an instance of the object
                        Class c = Class.forName(pckgname+"."+classname);
                        Object o = c.newInstance();
                        if (tosubclass.isInstance(o)) {
                            results.add(c);
                            //System.out.println(classname);
                        }
                    } catch (ClassNotFoundException cnfex) {
                        System.err.println("Error: RTSI superclass not found.");
                        cnfex.printStackTrace();
                    } catch (InstantiationException iex) {
                        // We try to instanciate an interface
                        // or an object that does not have a 
                        // default constructor
                    } catch (IllegalAccessException iaex) {
                        // The class is not public
                    }
                }
            }
        } else {
            try {
                // It does not work with the filesystem: we must
                // be in the case of a package contained in a jar file.
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                String starts = conn.getEntryName();
                JarFile jfile = conn.getJarFile();
                Enumeration e = jfile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    String entryname = entry.getName();
                    if (entryname.startsWith(starts)
                        && (entryname.lastIndexOf('/') <= starts.length())
                    && entryname.endsWith(".class")) {
                        String classname = entryname.substring(0, entryname.length()-6);
                        if (classname.startsWith("/")) 
                            classname = classname.substring(1);
                        classname = classname.replace('/', '.');
                        try {
                            // Try to create an instance of the object
                            Class c = Class.forName(classname);
                            Object o = c.newInstance();
                            if (tosubclass.isInstance(o)) {
                                results.add(c);
                                //System.out.println(classname.substring(classname.lastIndexOf('.')+1));
                            }
                        } catch (ClassNotFoundException cnfex) {
                            System.err.println("Error: RTSI superclass not found.");
                            cnfex.printStackTrace();
                        } catch (InstantiationException iex) {
                            // We try to instanciate an interface
                            // or an object that does not have a 
                            // default constructor
                        } catch (IllegalAccessException iaex) {
                            // The class is not public
                        }
                    }
                }
            } catch (IOException ioex) {
                System.err.println(ioex);
            }	
        }
        
        return results;
    }
}
