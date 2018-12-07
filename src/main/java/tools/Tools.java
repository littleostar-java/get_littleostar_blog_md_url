package tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.MdBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class Tools {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void do_write_json_to_file(LinkedList<String> list, HashMap<String, LinkedList<MdBean>> hashMap) {
        String json_array = gson.toJson(list);
        String json_map = gson.toJson(hashMap, new TypeToken<HashMap<String, LinkedList<MdBean>>>() {
        }.getType());

//        System.out.println(json_array);
//        System.out.println(json_map);
        write_text_to_file("md-array.json", json_array);
        write_text_to_file("md-map.json", json_map);
    }

    private static void write_text_to_file(String fileName, String text) {
        try {
            String path_parent = System.getProperty("user.dir") + File.separator + "json_";
            File file_parent = new File(path_parent);
            if (!file_parent.exists()) {
                file_parent.mkdir();
            }
//            String dir = System.getProperty("user.dir") + File.separator + "json_" + File.separator + fileName;
            File file = new File(file_parent, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(text);
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(fileName + "write success ... ");
        }
    }

}
